package com.ruoyi.system.bean;

import com.ruoyi.system.bean.vo.OrderVO;
import com.ruoyi.system.domain.Orders;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/2/23 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class OrderBean {

    private BigDecimal totalMoney;

    private Integer totalOrders;

    private Integer visitorNum;
    
    private List<Orders> ordersList;

    private List<OrderVO> orderVO;

}
