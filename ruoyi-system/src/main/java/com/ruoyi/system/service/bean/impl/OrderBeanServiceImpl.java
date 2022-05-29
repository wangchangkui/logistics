package com.ruoyi.system.service.bean.impl;

import com.ruoyi.system.bean.OrderBean;
import com.ruoyi.system.bean.vo.OrderVO;
import com.ruoyi.system.mapper.OrdersMapper;
import com.ruoyi.system.mapper.WxUserMapper;
import com.ruoyi.system.mapper.bean.OrderBeanMapper;
import com.ruoyi.system.service.bean.OrderBeanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 11:35
 */
@Service
public class OrderBeanServiceImpl implements OrderBeanService {

    private final Logger LOG = LoggerFactory.getLogger("OrderBeanServiceImpl");

    private final OrderBeanMapper beanMapper;

    private final OrdersMapper ordersMapper;

    private final WxUserMapper wxUserMapper;

    public OrderBeanServiceImpl(OrderBeanMapper beanMapper, OrdersMapper ordersMapper, WxUserMapper wxUserMapper) {
        this.beanMapper = beanMapper;
        this.ordersMapper = ordersMapper;
        this.wxUserMapper = wxUserMapper;
    }

    @Override
    public OrderBean initOrderBean(String time) {
        Timestamp timestamp = null;
        if (null == time || "".equals(time)){
             LocalDate now = LocalDate.now();
             LocalDateTime localDateTime = LocalDateTime.of(2022,1,1,1,1);
//             LocalDateTime localDateTime = now.minusDays(now.getDayOfMonth()-1).atTime(0, 0, 0, 0);
//            final LocalDateTime localDateTime = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());
            timestamp = Timestamp.valueOf(localDateTime);

        }else {
            timestamp = Timestamp.valueOf(time);
        }
        OrderBean orderBean = beanMapper.initOrderBean(timestamp);
        orderBean.setVisitorNum(wxUserMapper.selectAllNum(timestamp));
        orderBean.setOrdersList(ordersMapper.selectOrdersListByCreateTime(timestamp));
        return orderBean;
    }

    @Override
    public List<OrderVO> initOrderVO(String time) {
        return beanMapper.initOrderVO(null).stream().map(t -> {
            String[] split = t.getTime().split("/");
            t.setTime("20"+t.getTime());
            t.setYear("20"+split[0])
                    .setMoneyNum(Integer.valueOf(t.getMoney().toString().split("\\.")[0]))
                    .setMonth(split[1])
                    .setDay(split[2]);
            final LocalDate of = LocalDate.of(Integer.parseInt(t.getYear()), Integer.parseInt(t.getMonth()), Integer.parseInt(t.getDay()));
            return t.setTimestamp(String.valueOf(of.toEpochDay()));
        }).collect(Collectors.toList());
    }
}
