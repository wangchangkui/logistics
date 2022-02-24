package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.validation.annotation.ValidationAnnotationUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 16:05:00
 */
@Data
@TableName("pay_order")
public class PayOrder {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "order_id")
    private String orderId;
    @TableField(value = "pay_type")
    private String payType;
    @TableField(value = "user_id")
    private String userId;
    @TableField(value = "username")
    private String userName;
    @TableField(value = "create_time")
    private Timestamp createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "money")
    private BigDecimal money;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "status")
    private Integer status;
    @TableField(exist = false)
    private String image;
}
