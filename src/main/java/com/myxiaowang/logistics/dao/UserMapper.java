package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myxiaowang.logistics.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:52:00
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取用户数据
     * @param openId openId
     * @return 结果
     */
    User getUser(String openId);
}
