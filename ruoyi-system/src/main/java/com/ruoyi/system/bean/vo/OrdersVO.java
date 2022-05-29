package com.ruoyi.system.bean.vo;

import com.ruoyi.system.domain.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/3/16 21:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Accessors(chain = true)
public class OrdersVO extends Orders {

    Integer type;

}
