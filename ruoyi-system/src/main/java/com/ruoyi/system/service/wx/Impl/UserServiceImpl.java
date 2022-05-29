package com.ruoyi.system.service.wx.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.config.PropertiesConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.core.domain.resutl.ResultInfo;
import com.ruoyi.common.utils.rabbit.Produce;
import com.ruoyi.common.utils.redis.RedisPool;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.Address;
import com.ruoyi.system.domain.wx.Arrears;
import com.ruoyi.system.domain.wx.User;
import com.ruoyi.system.plus.mapper.wx.AddressMapper;
import com.ruoyi.system.plus.mapper.wx.ArrearsMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.service.wx.UserService;
import com.ruoyi.system.utils.file.FileUtils;
import com.ruoyi.system.utils.http.HttpRequest;
import com.ruoyi.system.utils.oos.OSSClient;
import com.ruoyi.system.utils.oos.OssUtil;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, WxUser> implements UserService {

    /**
     * http请求的错误信息
     */
    private static final String ERROR = "Invalid Input - wrong category";

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
        WxUser arrUser = getOne(new QueryWrapper<WxUser>().eq("user_id", userArre.getArrUserid()));
        // 如果被欠费的人余额不足
        if (userArre.getMoney().compareTo(arrUser.getDecimals()) > 0) {
            return ResponseResult.error("余额不足");
        }
        // 用户被欠费的
        BigDecimal decimals = arrUser.getDecimals();
        arrUser.setDecimals(decimals.subtract(userArre.getMoney()));
        Integer version = Math.toIntExact(arrUser.getVersion());
        arrUser.setVersion((long) (version + 1));
        update(arrUser, new QueryWrapper<WxUser>().eq("user_id", arrUser.getUserId()).eq("money", decimals).eq("version", version));
        //我的信息
        WxUser me = userArre.getMe();
        BigDecimal decimals1 = me.getDecimals();
        me.setDecimals(decimals1.add(userArre.getMoney()));
        Integer version1 = Math.toIntExact(me.getVersion());
        me.setVersion((long) (version1 + 1));
        update(me, new QueryWrapper<WxUser>().eq("user_id", me.getUserId()).eq("money", decimals1).eq("version", version1));
        // 删除欠费信息
        arrearsMapper.deleteById(arreaId);
        return ResponseResult.success("缴费完成");
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getUserArre(String userId) {
        return ResponseResult.success(userMapper.getUserArre(userId));
    }

    @Override
    public ResponseResult<List<Map<String, Object>>> getArreInfo(String userId) {
        return ResponseResult.success(arrearsMapper.getArrearsMap(userId));
    }

    @Override
    public ResponseResult<WxUser> getUserInfo(String userId) {

        return null;
    }

    //    @LoginAop(login = "check")
    @Override
    public ResponseResult<WxUser> passWordLogin(String username, String password) {
        password = DigestUtils.md5Hex(password.getBytes(StandardCharsets.UTF_8));
        WxUser one = getOne(new QueryWrapper<WxUser>().eq("username", username).eq("password", password));
        if (ObjectUtils.isNull(one)) {
            return ResponseResult.error(ResultInfo.NO_RESULT);
        }
        try (Jedis jedis = redisPool.getConnection()) {
            jedis.lpush("userList", JSON.toJSONString(one));
        }
        return ResponseResult.success(one);
    }

    @Override
    public ResponseResult<String> checkCard(String filePath, String name, String card, String userId) {
        // 比对身份证的正面照数据
        HashMap<String, String> header = new HashMap<>(8);
        header.put("Content-Type", "application/json; charset=UTF-8");
        header.put("Authorization", propertiesConfig.getAuthentication());
        HashMap<String, String> json = new HashMap<>(8);
        json.put("side", "face");
        HashMap<String, Object> query = new HashMap<>(8);
        query.put("image", filePath);
        query.put("configure", json);
        String s = JSONObject.toJSONString(query);
        String s1 = null;
        try {
            s1 = HttpRequest.postRequest(propertiesConfig.getAuthUrl(), s, header);
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
        }
        OSSClient ossClient = ossUtil.getClient();
        ossClient.deleteFile(config.getBUKKET_NAME(), filePath.substring(filePath.lastIndexOf("/") + 1));
        if (ERROR.equals(s1)) {
            return ResponseResult.error("上传的身份证照片不正确");
        }
        JSONObject res = JSON.parseObject(s1);
        // 得到结果后还需要删除照片
        if (res.containsKey("name")) {
            if (res.get("name").equals(name) && res.get("num").equals(card)) {
                // 读取用户信息
                try (Jedis jedis = redisPool.getConnection()) {
                    String user = jedis.get(Constants.OPEN_ID+userId);
                    WxUser myUser;
                    if (Objects.isNull(user)) {
                        myUser = getOne(new QueryWrapper<WxUser>().eq("user_id", userId));
                    } else {
                        myUser = JSON.parseObject(user, WxUser.class);
                    }
                    if (Objects.isNull(myUser)) {
                        return ResponseResult.error("用户不存在");
                    }
                    myUser.setIdCard(card);
                    myUser.setName(name);
                    userMapper.updateById(myUser);
                    jedis.set(Constants.OPEN_ID+userId, JSON.toJSONString(myUser));
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
        String fileName = IdUtil.simpleUUID().substring(0, 8) + ".png";
        ossClient.uploadFile(config.getBUKKET_NAME(), fileName, target);
        // 删除文件
        try {
            org.apache.commons.io.FileUtils.delete(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(ossPath + fileName);
    }

    @Override
    public ResponseResult<Address> getAddressById(String id) {
        return ResponseResult.success(addressMapper.selectById(id));
    }

    @Override
    public ResponseResult<String> addAddress(Address address) {
        // 如果是设置默认值，需要把其他设置了默认值的数据给干掉
        if (address.getIsCheck() == 1) {
            Address temp = new Address();
            temp.setIsCheck(0);
            addressMapper.update(temp, new QueryWrapper<Address>().eq("user_id", address.getUserId()));
        }
        if (Objects.isNull(address.getId())) {
            addressMapper.insert(address);
        } else {
            addressMapper.updateById(address);
        }

        return ResponseResult.success("插入成功");
    }

    @Override
    public ResponseResult<String> setCheck(String id, String userId) {
        Address address = new Address();
        address.setIsCheck(0);
        addressMapper.update(address, new QueryWrapper<Address>().eq("user_id", userId));
        address.setIsCheck(1);
        addressMapper.update(address, new QueryWrapper<Address>().eq("id", id));
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
    public ResponseResult<String> updateUser(WxUser user) {
        System.out.println(user.getUserId());
        user.setUpdateTime(new Date());
        // 删除缓存对象 下一次请求会加回来
        try (Jedis jedis = redisPool.getConnection()) {
            jedis.del(Constants.OPEN_ID + user.getUserId());
        }
        int res = userMapper.update(user, new QueryWrapper<WxUser>().eq("user_id", user.getUserId()));
        // 修复一个前端设置空值的BUG
        if (res == 1) {
            return ResponseResult.success(ResultInfo.SUCCESS.getMessage());
        }
        return ResponseResult.error("更新失败");
    }

    @Override
    public ResponseResult<String> uploadHeader(String userid, MultipartFile file) {
        // 上传图片文件
        File transFile = FileUtils.multipartFileTransFileOnFix(file, filePath + "/");
        OSSClient ossClient = ossUtil.getClient();
        // 在更新前删除用户原来的头像
        ossClient.deleteFile(propertiesConfig.getBUKKET_NAME(), userid + ".png");
        // 上传图片
        ossClient.uploadFile(propertiesConfig.getBUKKET_NAME(), userid + ".png", transFile);
        return ResponseResult.success(ossPath + userid + ".png");
    }

    @Override
    public ResponseResult<WxUser> checkSms(String phone, String sms) {
        WxUser user = getOne(new QueryWrapper<WxUser>().eq("phone", phone));
        if (Objects.isNull(user)) {
            return ResponseResult.error("用户不存在");
        }
        try (Jedis jedis = redisPool.getConnection()) {
            String yzm = jedis.get(phone);
            if (Objects.isNull(yzm)) {
                return ResponseResult.error("验证码已过期,或者不存在");
            }
            if (!Objects.equals(yzm, sms)) {
                return ResponseResult.error("验证码不匹配");
            }
            // 删除验证蚂
            jedis.del(phone);
        }
        return ResponseResult.success(user);
    }

    @Override
    public ResponseResult<WxUser> getUser(String openId) {
        System.out.println(openId);
        WxUser loginUser = null;
        try (Jedis jedis = redisPool.getConnection()) {
            String user = jedis.get(Constants.OPEN_ID + openId);
            // redis 不存在的情况下
            if (Objects.isNull(user)) {
                loginUser = userMapper.selectUserBuOpenId(openId);
                // 数据库不存在的情况下
                if (Objects.isNull(loginUser)) {
                    return ResponseResult.error(ResultInfo.NO_RESULT);
                }
                // 设置redis参数
                SetParams setParams = new SetParams();
                setParams.nx().ex(86400);
                jedis.set(Constants.OPEN_ID + openId, JSON.toJSONString(loginUser), setParams);
            } else {
                loginUser = JSON.parseObject(user, WxUser.class);
            }
            return ResponseResult.success(loginUser);
        }
    }

    @Override
    public ResponseResult<String> signUser(WxUser user) {
        // 设置初始密码 但是用不到了
        user.setPassword("123456");
        user.setPassword(Md5Crypt.md5Crypt(user.getPassword().getBytes(), "$1$myxiaowang"));
        user.setPhone("");
        // 设置名称
        user.setName("");
        int insert = userMapper.insert(user);
        // 注册成功后往redis中存入数据
        try (Jedis jedis = redisPool.getConnection()) {
            jedis.set(user.getUserId(), JSON.toJSONString(user));
        }
        return ResponseResult.success("" + insert);
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
        WxUser phoneIs = getOne(new QueryWrapper<WxUser>().eq("phone", phone));
        if (Objects.isNull(phoneIs)) {
            return ResponseResult.error(ResultInfo.NO_RESULT);
        }
        // 设置redis参数
        SetParams setParams = new SetParams();
        // 60秒过期
        setParams = setParams.nx().ex(12000);
        try (Jedis jedis = redisPool.getConnection()) {

            Random random = new Random();
            int code = random.nextInt(999999);
            // 发送短息 测试环境下面请注释这段代码 因为费钱
            //sendSms.sendSms(phone, String.valueOf(code));
            jedis.set(phone, String.valueOf(code), setParams);
            // 队列发送验证码
            produce.sendMsg(phone);
        }
        return ResponseResult.success(ResultInfo.SUCCESS);
    }

    @Override
    public ResponseResult<WxUser> signOrLogin(WxUser user) {
        try (Jedis jedis = redisPool.getConnection()) {
            // 查询redis中是否存在用户
            String userId = jedis.get(user.getUserId());
            if (Objects.isNull(userId)) {
                // 查询数据库
                WxUser loginUser = getOne(new QueryWrapper<WxUser>().eq("user_id", user.getUserId()));
                if (Objects.isNull(loginUser)) {
                    // 注册用户
                    signUser(user);
                    // 并且往redis里面存入数据
                    jedis.set(user.getUserId(), JSON.toJSONString(user));
                    return ResponseResult.success(user);
                }
                // 往redis里面存入数据
                jedis.set(user.getUserId(), JSON.toJSONString(loginUser));
                return ResponseResult.success(loginUser);
            }
            // 取redis里面的用户数据
            return ResponseResult.success(JSON.parseObject(userId, WxUser.class));
        }
    }

}
