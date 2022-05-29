package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Jandmla
 * @version 1.0
 * @description 微信端 获取
 * @data 2022/3/15 14:59
 */
@Mapper
@Component
public interface OrderMapper  extends BaseMapper<Orders> {
}
