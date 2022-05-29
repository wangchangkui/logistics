package com.ruoyi.system.service.wx;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.common.core.domain.resutl.ResultInfo;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.Address;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 16:40:00
 */

public interface UserService extends IService<WxUser> {


    /**
     * 欠费缴费接口
     * @param arreaId 欠费id
     * @return 操作结果
     */
    ResponseResult<String> overArrea(Integer arreaId);

    /**
     * 获取用户的欠费信息
     * @param userId 用户值
     * @return map
     */
    ResponseResult<List<Map<String, Object>>> getUserArre(String userId);

    /**
     * 获取欠费余额原因
     * @param userId 被欠费用户id
     * @return map
     */
    ResponseResult<List<Map<String, Object>>> getArreInfo(String userId);

    /**
     * 根据userId获取用户的数据
     * @param userId userId
     * @return 返回结果
     */
    ResponseResult<WxUser> getUserInfo(String userId);

    /**
     * 根据账号密码登录
     * @param username 用户名
     * @param password 密码
     * @return 操作
     */
    ResponseResult<WxUser> passWordLogin(String username,String password);

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
    ResponseResult<String>  updateUser(WxUser user);

    /**
     * 上传头像地址
     * @param file web文件
     * @param userId 头像id
     * @return 返回值
     */
    ResponseResult<String> uploadHeader(String userId, MultipartFile file);

    /**
     * 登录验证
     * @param phone id
     * @param sms 验证码
     * @return 返回结果
     */
    ResponseResult<WxUser> checkSms(String phone,String sms);

    /**
     * 获取登录用户
     * @param openId 用户id
     * @return User
     */
     ResponseResult<WxUser> getUser(String openId);

    /**
     * 注册用户
     * @param user user
     * @return 注册结果
     */
     ResponseResult<String> signUser(WxUser user);

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

    /**
     * 注册或者登录
     * @param user User
     * @return 返回值
     */
    ResponseResult<WxUser> signOrLogin(WxUser user);
}
