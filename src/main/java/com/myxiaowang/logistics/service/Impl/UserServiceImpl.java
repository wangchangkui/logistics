package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.common.RabbitMq.Produce;
import com.myxiaowang.logistics.common.TxSms.SendSms;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.AddressMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.UserService;
import com.myxiaowang.logistics.util.FileUtils;
import com.myxiaowang.logistics.util.HttpRequest.HttpRequest;
import com.myxiaowang.logistics.util.OSS.OSSClient;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:41:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private OSSClient.OSSBuilder ossBuilder;

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private Produce produce;

    @Autowired
    private PropertiesConfig propertiesConfig;


    @Autowired
    private PropertiesConfig config;
    
    @Value("${filePath.path}")
    private String filePath;
    @Value("${filePath.osspath}")
    private String ossPath;

    @Override
    public ResponseResult<String> checkCard(String filePath, String name, String card,String userId) {
        // 比对身份证的正面照数据
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json; charset=UTF-8");
        header.put("Authorization",propertiesConfig.getAuthentication());
        HashMap<String, String> json = new HashMap<>(8);
        json.put("side","face");
        HashMap<String, Object> query = new HashMap<>(8);
        query.put("image",filePath);
        query.put("configure",json);
        String s = JSONObject.toJSONString(query);
        String s1 = null;
        try {
            s1 = HttpRequest.postRequest(propertiesConfig.getAuthUrl(), s, header);
        } catch (ExecutionException | InterruptedException e) {
          log.error(e.getMessage());
        }
        JSONObject res = JSON.parseObject(s1);
        if(res.containsKey("name")){
            if(res.get("name").equals(name) && res.get("num").equals(card)){
                // 读取用户信息
                try(Jedis jedis=redisPool.getConnection()){
                    String user = jedis.get("userId");
                    User myUser;
                    if(Objects.isNull(user)){
                        myUser=getOne(new QueryWrapper<User>().eq("userid",userId));
                    }else{
                        myUser=JSON.parseObject(user,User.class);
                    }
                    if(Objects.isNull(user)){
                        return ResponseResult.error("用户不存在");
                    }
                    myUser.setIdCard(card);
                    myUser.setName(name);
                    userMapper.updateById(myUser);
                }
                return ResponseResult.success("认证成功");
            }
            return ResponseResult.error("用户不匹配");
        }
        return ResponseResult.error("身份证证件照不匹配");
    }

    @Override
    public ResponseResult<String> uploadFileCheck(MultipartFile file) {
        File target = FileUtils.multipartFileTransFileOnFix(file, filePath + "/");
        OSSClient.OSSBuilder ossBuilder = this.ossBuilder.setEND_POINT(config.getEND_POINT())
                .setACCESS_KEY_ID(config.getACCESS_KEY_ID())
                .setACCESS_KEY_SECRET(config.getACCESS_KEY_SECRET());
        OSSClient ossClient = ossBuilder.buidOSS(ossBuilder);
        String fileName = IdUtil.simpleUUID().substring(0, 8)+".png";
        ossClient.uploadFile(config.getBUKKET_NAME(),fileName,target);
        // 删除文件
        try {
            org.apache.commons.io.FileUtils.delete(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(ossPath+fileName);
    }

    @Override
    public ResponseResult<Address> getAddressById(String id) {
        return ResponseResult.success(addressMapper.selectById(id));
    }

    @Override
    public ResponseResult<String> addAddress(Address address) {
        // 如果是设置默认值，需要把其他设置了默认值的数据给干掉
        if(address.getIsCheck()==1){
            Address temp = new Address();
            temp.setIsCheck(0);
            addressMapper.update(temp,new QueryWrapper<Address>().eq("userId",address.getUserId()));
        }
        if(Objects.isNull(address.getId())){
            addressMapper.insert(address);
        }else{
            addressMapper.updateById(address);
        }

        return ResponseResult.success("插入成功");
    }

    @Override
    public ResponseResult<String> setCheck(String id,String userId) {
        Address address = new Address();
        address.setIsCheck(0);
        addressMapper.update(address,new QueryWrapper<Address>().eq("userId",userId));
        address.setIsCheck(1);
        addressMapper.update(address,new QueryWrapper<Address>().eq("id",id));
        return ResponseResult.success("更新默认值成功");
    }

    @Override
    public ResponseResult<String> deleteUserAddress(String id) {
        addressMapper.deleteById(id);
        return ResponseResult.success(ResultInfo.SUCCESS.getMessage());
    }

    @Override
    public ResponseResult<List<Address>> getUserAddressList(String userId) {
        List<Address> userId1 = addressMapper.selectList(new QueryWrapper<Address>().eq("userId", userId));
        return ResponseResult.success(userId1);
    }

    @Override
    public ResponseResult<String> updateUser(User user) {
        userMapper.updateById(user);
        return ResponseResult.success(ResultInfo.SUCCESS.getMessage());
    }

    @Override
    public ResponseResult<String> uploadHeader(String userid,MultipartFile file) {
        // 上传图片文件
        File transFile = FileUtils.multipartFileTransFileOnFix(file, filePath+"/");
        OSSClient.OSSBuilder ossBuilder = this.ossBuilder.setEND_POINT(config.getEND_POINT())
                .setACCESS_KEY_ID(config.getACCESS_KEY_ID())
                .setACCESS_KEY_SECRET(config.getACCESS_KEY_SECRET());
        OSSClient ossClient = ossBuilder.buidOSS(ossBuilder);
        // 在更新前删除用户原来的头像
        ossClient.deleteFile(propertiesConfig.getBUKKET_NAME(),userid+".png");
        // 上传图片
        ossClient.uploadFile(propertiesConfig.getBUKKET_NAME(), userid+".png",transFile);
        return ResponseResult.success(ossPath+userid+".png");
    }

    @Override
    public ResponseResult<User> checkSms(String phone, String sms) {
        User user=getOne(new QueryWrapper<User>().eq("phone", phone));
        if(Objects.isNull(user)){
            return ResponseResult.error("用户不存在");
        }
        try(Jedis jedis=redisPool.getConnection()){
            String yzm = jedis.get(phone);
            if(Objects.isNull(yzm) ){
                return ResponseResult.error("验证码已过期,或者不存在");
            }
            if(!Objects.equals(yzm,sms)){
                return ResponseResult.error("验证码不匹配");
            }
            // 删除验证蚂
            jedis.del(phone);
        }
        return ResponseResult.success(user);
    }

    @Override
    public ResponseResult<User> getUser(String openId) {
        User loginUser=null;
        try (Jedis jedis=redisPool.getConnection();){
            String user = jedis.get("openid");
            // redis 不存在的情况下
            if(Objects.isNull(user)){
                loginUser = userMapper.getUser(openId);
                // 数据库不存在的情况下
                if(Objects.isNull(loginUser)){
                    return ResponseResult.error(ResultInfo.NO_RESULT);
                }
                // 设置redis参数
                SetParams setParams=new SetParams();
                setParams.nx().ex(86400);
                jedis.set(openId, JSON.toJSONString(loginUser),setParams);
            }
            JSON.parseObject(user,User.class);
        }
        return ResponseResult.success(loginUser);
    }

    @Override
    public ResponseResult<String> signUser(User user) {
        user.setPassword(IdUtil.fastSimpleUUID());
        user.setPhone("");
        int insert = userMapper.insert(user);
        return ResponseResult.success(""+insert);
    }

    @Override
    public ResponseResult<Address> getUserAddress(String openId) {
        Address address = addressMapper.selectOne(new QueryWrapper<Address>().eq("userid", openId).and(z -> {
            z.eq("isCheck", 1);
        }));
        return ResponseResult.success(address);
    }

    @Override
    public ResponseResult<ResultInfo> sendSms(String phone) {
        // 先去查询一下手机号
        User phoneIs = getOne(new QueryWrapper<User>().eq("phone", phone));
        if(Objects.isNull(phoneIs)){
            return ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 设置redis参数
        SetParams setParams=new SetParams();
        // 60秒过期
        setParams=setParams.nx().ex(120);
        try(Jedis jedis=redisPool.getConnection()){

            Random random=new Random();
            int code = random.nextInt(999999);
            // 发送短息 测试环境下面请注释这段代码 因为费钱
            //sendSms.sendSms(phone, String.valueOf(code));
            jedis.set(phone, String.valueOf(code),setParams);
            // 队列发送验证码
            produce.sendMsg(phone);
        }
       return ResponseResult.success(ResultInfo.SUCCESS);
    }

}
