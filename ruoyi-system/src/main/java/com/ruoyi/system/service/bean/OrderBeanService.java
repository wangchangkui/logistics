package com.ruoyi.system.service.bean;

import com.ruoyi.system.bean.OrderBean;
import com.ruoyi.system.bean.vo.OrderVO;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 11:34
 */
public interface OrderBeanService {

    /**
     * 初始化页面服务数据查询
     * @param time 查询时间
     * @return 查询数据结果
     */
    OrderBean initOrderBean(String time);

    /**
     * 初始化统计图数据
     * @param time 时间
     * @return 数据结果集
     */
    List<OrderVO> initOrderVO(String time);
}
