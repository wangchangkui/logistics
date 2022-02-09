package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.Address;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import com.myxiaowang.logistics.util.Reslut.ResultInfo;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:40:00
 */

public interface UserService extends IService<User> {

    /**
     * 身份证必对
     * @param filePath 身份证地址
     * @param name 名称
     * @param card 身份证号码
     * @param userId 用户ID
     * @return ResponseResult
     */
    ResponseResult<String> checkCard(String filePath,String name,String card,String userId);

    /**
     * 仅使用验证身份证的时候使用此接口
     * @param file 文件
     * @return 上传结果的url
     */
    ResponseResult<String> uploadFileCheck(MultipartFile file);

    /**
     * 根据id获取数据
     * @param id id
     * @return Address
     */
    ResponseResult<Address> getAddressById(String id);

    /**
     * 更新地址
     * @param address 对象
     * @return 返回值
     */
    ResponseResult<String> addAddress(Address address);


    /**
     * 设置用户的默认地址
     * @param id id
     * @return 返回值
     */
    ResponseResult<String> setCheck(String id,String userId);

    /**
     * 根据id删除用户id
     * @param id 用户id�
     * @return 返回值
     */
    ResponseResult<String> deleteUserAddress(String id);

    /**
     * 获取用户的所有地址
     * @param userId 用户的id
     * @return 返回值
     */
    ResponseResult<List<Address>> getUserAddressList(String userId);

    /**
     * 更新用户
     * @param user 用户对象
     * @return 返回值
     */
    ResponseResult<String>  updateUser(User user);

    /**
     * 上传头像地址
     * @param file web文件
     * @param userId 头像id
     * @return 返回值
     */
    ResponseResult<String> uploadHeader(String userId,MultipartFile file);

    /**
     * 登录验证
     * @param phone id
     * @param sms 验证码
     * @return 返回结果
     */
    ResponseResult<User> checkSms(String phone,String sms);

    /**
     * 获取登录用户
     * @param openId 用户id
     * @return User
     */
     ResponseResult<User> getUser(String openId);

    /**
     * 注册用户
     * @param user user
     * @return 注册结果
     */
     ResponseResult<String> signUser(User user);

    /**
     * 获取用户的默认地址
     * @param openId openId
     * @return 返回值
     */
     ResponseResult<Address> getUserAddress(String openId);

    /**
     * 登录的验证码发送
     * @param phone 手机号
     * @return 发送验证码
     */
     ResponseResult<ResultInfo> sendSms(String phone);

}
