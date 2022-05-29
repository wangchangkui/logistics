package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.WxUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Jandmla
 * @version 1.0.0
 * @Description TODO
 * @data 2022/3/15 14:46
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<WxUser> {

    /**
     * 获取用户的欠费信息
     * @param userId 用户id
     * @return 返回值
     */
    List<Map<String,Object>> getUserArre(String userId);
    /**
     * 获取用户数据
     * @param openId openId
     * @return 结果
     */
    WxUser getUser(String openId);

    /**
     * 根据 opendId 获取用户基本信息
     * @param openId opendId
     * @return 查询结果
     */
    WxUser selectUserBuOpenId(String openId);
}
