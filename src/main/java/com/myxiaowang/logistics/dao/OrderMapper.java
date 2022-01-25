package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myxiaowang.logistics.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月20日 17:07:00
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
