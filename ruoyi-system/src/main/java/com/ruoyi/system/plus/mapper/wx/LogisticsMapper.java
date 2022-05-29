package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.bean.vo.OrdersVO;
import com.ruoyi.system.domain.wx.Logistics;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jandmla
 * @version 1.0.0
 * @Description TODO
 * @data 2022/3/15 14:46
 */
@Mapper
@Component
public interface LogisticsMapper extends BaseMapper<Logistics> {

    /**
     * 根据type类型 返回用户抢到的订单
     * @param vo 用户id userId , 订单类型 type
     * @return 订单即集合
     */
    List<Logistics> getUserLogistics(@Param("vo") OrdersVO vo);
}
