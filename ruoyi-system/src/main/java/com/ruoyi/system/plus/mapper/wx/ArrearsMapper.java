package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.wx.Arrears;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Jandmla
 * @version 1.0.0
 * @Description TODO
 * @data 2022/3/15 14:46
 */
@Mapper
@Component
public interface ArrearsMapper extends BaseMapper<Arrears> {

    /**
     * 根据 用户 ID 获取 未支付订单信息
     * @param id 用户 ID
     * @return 未支付订单
     */
    Arrears getUserArre(Integer id);

    /**
     * 根据 用户 ID 获取 未支付订单 信息集合
     * @param userId 用户 ID
     * @return 未支付订单 集合
     */
    @MapKey("id")
    List<Map<String,Object>> getArrearsMap(String userId);
}
