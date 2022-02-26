package com.myxiaowang.logistics.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.common.RabbitMq.Produce;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.myxiaowang.logistics.dao.AddressMapper;
import com.myxiaowang.logistics.dao.ArrearsMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.Arrears;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.UserService;
import com.myxiaowang.logistics.util.Annotation.LoginAop;
import com.myxiaowang.logistics.util.FileUtils;
import com.myxiaowang.logistics.util.HttpRequest.HttpRequest;
import com.myxiaowang.logistics.util.OSS.OSSClient;
import com.myxiaowang.logistics.util.OSS.OssUtil;
import com.myxiaowang.logistics.util.RedisUtil.RedisPool;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:41:00
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * http请求的错误信息
     */
    private static final String ERROR="Invalid Input - wrong category";

    @Autowired
    private OssUtil ossUtil;

    @Autowired
    private ArrearsMapper arrearsMapper;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> overArrea(Integer arreaId) {
        Arrears userArre = arrearsMapper.getUserArre(arreaId);
        // 获取被欠费人的信息
        User arrUser = getOne(new QueryWrapper<User>().eq("user_id", userArre.getArrUserid()));
        // 如果被欠费的人余额不足
        if(userArre.getMoney().compareTo(arrUser.getDecimals())>0){
            return ResponseResult.error("余额不足");
        }
        // 用户被欠费的
        BigDecimal decimals  = arrUser.getDecimals();
        arrUser.setDecimals(decimals.subtract(userArre.getMoney()));
        Integer version = arrUser.getVersion();
        arrUser.setVersion(version+1);
        update(arrUser,new QueryWrapper<User>().eq("user_id", arrUser.getUserid()).eq("money",decimals).eq("version",version));
        //我的信息
        User me = userArre.getMe();
        BigDecimal decimals1 = me.getDecimals();
        me.setDecimals(decimals1.add(userArre.getMoney()));
        Integer version1 = me.getVersion();
        me.setVersion(version1+1);
        update(me,new QueryWrapper<User>().eq("user_id", me.getUserid()).eq("money",decimals1).eq("version",version1));
        // 删除欠费信息
        arrearsMapper.deleteById(arreaId);
        return ResponseResult.success("缴费完成");
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getUserArre(String userId) {
        return ResponseResult.success( userMapper.getUserArre(userId) );
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getArreInfo(String userId) {
      return  ResponseResult.success( arrearsMapper.getArrearsMap(userId) );
    }

    @Override
    public ResponseResult<User> getUserInfo(String userId) {

        return null;
    }

    @LoginAop(login = "check")
    @Override
    public ResponseResult<User> passWordLogin(String username, String password) {
        password = DigestUtils.md5Hex(password.getBytes(StandardCharsets.UTF_8));
        User one = getOne(new QueryWrapper<User>().eq("username", username).eq("password", password));
        if(ObjectUtils.isNull(one)){
            return ResponseResult.error(ResultInfo.NO_RESULT);
        }
        try(Jedis jedis=redisPool.getConnection()){
            jedis.lpush("userList",JSON.toJSONString(one));
        }
       return ResponseResult.success(one);
    }

    @Override
    public ResponseResult<String> checkCard(String filePath, String name, String card,String userId) {
        // 比对身份证的正面照数据
        HashMap<String, String> header = new HashMap<>(8);
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
        OSSClient ossClient = ossUtil.getClient();
        ossClient.deleteFile(config.getBUKKET_NAME(),filePath.substring(filePath.lastIndexOf("/")+1));
        if(ERROR.equals(s1)){
            return ResponseResult.error("上传的身份证照片不正确");
        }
        JSONObject res = JSON.parseObject(s1);
        // 得到结果后还需要删除照片
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
        OSSClient ossClient = ossUtil.getClient();
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
            addressMapper.update(temp,new QueryWrapper<Address>().eq("user_id",address.getUserId()));
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
        addressMapper.update(address,new QueryWrapper<Address>().eq("user_id",userId));
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
        List<Address> userId1 = addressMapper.selectList(new QueryWrapper<Address>().eq("user_id", userId));
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
        OSSClient ossClient = ossUtil.getClient();
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
        try (Jedis jedis=redisPool.getConnection()){
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
        // 设置密码
        user.setPassword(Md5Crypt.md5Crypt(user.getPassword().getBytes(),"$1$myxiaowang"));
        user.setPhone("");
        int insert = userMapper.insert(user);
        return ResponseResult.success(""+insert);
    }

    @Override
    public ResponseResult<Address> getUserAddress(String openId) {
        Address address = addressMapper.selectOne(new QueryWrapper<Address>().eq("user_id", openId).and(z -> {
            z.eq("is_check", 1);
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
