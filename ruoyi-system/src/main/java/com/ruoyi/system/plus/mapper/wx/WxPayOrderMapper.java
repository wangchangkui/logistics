package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.PayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Jandmla
 * @version 1.0.0
 * @Description TODO
 * @data 2022/3/15 14:46
 */
@Mapper
@Component
public interface WxPayOrderMapper extends BaseMapper<PayOrder> {

}
