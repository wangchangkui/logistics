package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WxUserMapper;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.service.IWxUserService;
import com.ruoyi.common.core.text.Convert;

/**
 * 微信用户信息Service业务层处理
 * 
 * @author 23909
 * @date 2022-03-10
 */
@Service
public class WxUserServiceImpl implements IWxUserService 
{
    @Autowired
    private WxUserMapper wxUserMapper;

    /**
     * 查询微信用户信息
     * 
     * @param id 微信用户信息主键
     * @return 微信用户信息
     */
    @Override
    public WxUser selectWxUserById(Long id)
    {
        return wxUserMapper.selectWxUserById(id);
    }

    /**
     * 查询微信用户信息列表
     * 
     * @param wxUser 微信用户信息
     * @return 微信用户信息
     */
    @Override
    public List<WxUser> selectWxUserList(WxUser wxUser)
    {
        return wxUserMapper.selectWxUserList(wxUser);
    }

    /**
     * 新增微信用户信息
     * 
     * @param wxUser 微信用户信息
     * @return 结果
     */
    @Override
    public int insertWxUser(WxUser wxUser)
    {
        wxUser.setCreateTime(DateUtils.getNowDate());
        return wxUserMapper.insertWxUser(wxUser);
    }

    /**
     * 修改微信用户信息
     * 
     * @param wxUser 微信用户信息
     * @return 结果
     */
    @Override
    public int updateWxUser(WxUser wxUser)
    {
        wxUser.setUpdateTime(DateUtils.getNowDate());
        return wxUserMapper.updateWxUser(wxUser);
    }

    /**
     * 批量删除微信用户信息
     * 
     * @param ids 需要删除的微信用户信息主键
     * @return 结果
     */
    @Override
    public int deleteWxUserByIds(String ids)
    {
        return wxUserMapper.deleteWxUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除微信用户信息信息
     * 
     * @param id 微信用户信息主键
     * @return 结果
     */
    @Override
    public int deleteWxUserById(Long id)
    {
        return wxUserMapper.deleteWxUserById(id);
    }
}
