package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WxUser;

/**
 * 微信用户信息Service接口
 * 
 * @author 23909
 * @date 2022-03-10
 */
public interface IWxUserService 
{
    /**
     * 查询微信用户信息
     * 
     * @param id 微信用户信息主键
     * @return 微信用户信息
     */
    public WxUser selectWxUserById(Long id);

    /**
     * 查询微信用户信息列表
     * 
     * @param wxUser 微信用户信息
     * @return 微信用户信息集合
     */
    public List<WxUser> selectWxUserList(WxUser wxUser);

    /**
     * 新增微信用户信息
     * 
     * @param wxUser 微信用户信息
     * @return 结果
     */
    public int insertWxUser(WxUser wxUser);

    /**
     * 修改微信用户信息
     * 
     * @param wxUser 微信用户信息
     * @return 结果
     */
    public int updateWxUser(WxUser wxUser);

    /**
     * 批量删除微信用户信息
     * 
     * @param ids 需要删除的微信用户信息主键集合
     * @return 结果
     */
    public int deleteWxUserByIds(String ids);

    /**
     * 删除微信用户信息信息
     * 
     * @param id 微信用户信息主键
     * @return 结果
     */
    public int deleteWxUserById(Long id);
}
