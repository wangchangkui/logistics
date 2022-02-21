package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月20日 17:03:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Order {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "userid")
    private String userId;
    @TableField(value = "goodsName")
    private String goodsName;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "status")
    private Integer status;
    @TableField(value = "money")
    private BigDecimal money;
    @TableField(value = "orderid")
    private String orderId;
    @TableField(value = "version")
    private Integer version;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "address")
    private String address;
    @TableField(value = "over_time")
    private Date overTime;
    @TableField(value = "code")
    private String code;
    @TableField(value = "username")
    private String userName;
    @TableField(value = "goods_address")
    private String goodsAddress;
}
