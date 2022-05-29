package com.ruoyi.system.mapper.bean;

import com.ruoyi.system.bean.OrderBean;
import com.ruoyi.system.bean.vo.OrderVO;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 11:23
 */

public interface OrderBeanMapper {

    /**
     * 页面初始化 获取数据
     * @return 页面展示结果
     * @param timestamp 查询时间上限
     */
    OrderBean initOrderBean(Timestamp timestamp);

    /**
     * 查询统计图必要的参数
     * @param timestamp 时间限制
     * @return 数据集（时间、金额）
     */
    List<OrderVO>  initOrderVO(Timestamp timestamp);
}
