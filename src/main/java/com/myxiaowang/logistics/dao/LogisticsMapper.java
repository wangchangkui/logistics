package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myxiaowang.logistics.pojo.Logistics;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:54:00
 */
@Mapper
public interface LogisticsMapper extends BaseMapper<Logistics> {

    /**
     * 根据type类型 返回用户抢到的订单
     * @param userId 用户id
     * @param type 类型
     * @return 订单即集合
     */
    List<Logistics> getUserLogistics(@Param("userid")String userId,@Param("type") int type);
}
