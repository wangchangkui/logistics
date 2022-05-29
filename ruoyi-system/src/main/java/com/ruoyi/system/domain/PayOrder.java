package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 支付管理对象 pay_order
 * 
 * @author 23909
 * @date 2022-02-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pay_order")
public class PayOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id 主键 自增 */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /** 订单ID 次订单是支付订单 */
    @TableField(value = "order_id")
    @Excel(name = "订单ID 次订单是支付订单")
    private String orderId;

    /** 支付方式 */
    @TableField(value = "pay_type")
    @Excel(name = "支付方式")
    private String payType;

    /** 支付用户id */
    @TableField(value = "user_id")
    @Excel(name = "支付用户id")
    private String userId;

    /** 支付名称 */
    @TableField(value = "username")
    @Excel(name = "支付名称")
    private String username;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    /** 支付金额 */
    @TableField(value = "money")
    @Excel(name = "支付金额")
    private BigDecimal money;

    /** 手机号 */
    @TableField(value = "phone")
    @Excel(name = "手机号")
    private String phone;

    /**
     * 支付图片地址
     */
    @TableField(exist = false)
    private String image;

    /** 支付状态 默认1 未支付 2已支付 3已过期 */
    @TableField(value = "status")
    @Excel(name = "支付状态")
    private Integer status;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }
    public void setPayType(String payType) 
    {
        this.payType = payType;
    }

    public String getPayType() 
    {
        return payType;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("payType", getPayType())
            .append("userId", getUserId())
            .append("username", getUsername())
            .append("createTime", getCreateTime())
            .append("money", getMoney())
            .append("phone", getPhone())
            .append("status", getStatus())
            .toString();
    }
}
