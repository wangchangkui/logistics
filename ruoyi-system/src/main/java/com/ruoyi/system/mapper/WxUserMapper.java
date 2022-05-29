package com.ruoyi.system.mapper;

import java.sql.Timestamp;
import java.util.List;
import com.ruoyi.system.domain.WxUser;

/**
 * 微信用户信息Mapper接口
 * 
 * @author 23909
 * @date 2022-03-10
 */
public interface WxUserMapper 
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
     * 删除微信用户信息
     * 
     * @param id 微信用户信息主键
     * @return 结果
     */
    public int deleteWxUserById(Long id);

    /**
     * 批量删除微信用户信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWxUserByIds(String[] ids);

    /**
     * 查询一定时间到现在的所有用户数量
     *
     * @param timestamp 时间上限
     * @return 查询结果
     */
    int selectAllNum(Timestamp time);
}
