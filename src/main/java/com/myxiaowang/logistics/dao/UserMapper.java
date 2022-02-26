package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myxiaowang.logistics.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:52:00
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

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
    User getUser(String openId);
}
