package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单管理对象 orders
 * 
 * @author ruoyi
 * @date 2022-02-23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("orders")
public class Orders extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 用户id */
    @TableField(value = "user_id")
    private String userId;

    /** 商品名称 */
    @TableField(value = "goods_name")
    @Excel(name = "商品名称")
    private String goodsName;

    /** 订单状态 */
    @TableField(value = "status")
    @Excel(name = "订单状态")
    private Integer status;

    /** 订单金额 */
    @TableField(value = "money")
    @Excel(name = "订单金额")
    private BigDecimal money;

    /** 订单id */
    @TableField(value = "order_id")
    @Excel(name = "订单id")
    private String orderId;

    /** 乐观锁 */
    @TableField(value = "version")
    private Long version;

    /** 联系电话 */
    @TableField(value = "phone")
    @Excel(name = "联系电话")
    private String phone;

    /** 联系地址 */
    @TableField(value = "address")
    @Excel(name = "联系地址")
    private String address;

    /** 联系地址 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Timestamp createTime;

    /** 结束时间 */
    @TableField(value = "over_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date overTime;

    /** 取货码 */
    @TableField(value = "code")
    @Excel(name = "取货码")
    private String code;

    /** 发起订单人的名称 */
    @TableField(value = "user_name")
    @Excel(name = "发起订单人的名称")
    private String userName;

    /** 取件地址 */
    @TableField(value = "goods_address")
    @Excel(name = "取件地址")
    private String goodsAddress;

    @TableField("accept_user_id")
    @Excel(name = "接收订单人ID")
    private String acceptUserId;

    @Override
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }
    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setOverTime(Date overTime) 
    {
        this.overTime = overTime;
    }

    public Date getOverTime() 
    {
        return overTime;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setGoodsAddress(String goodsAddress) 
    {
        this.goodsAddress = goodsAddress;
    }

    public String getGoodsAddress() 
    {
        return goodsAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("goodsName", getGoodsName())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .append("money", getMoney())
            .append("orderId", getOrderId())
            .append("version", getVersion())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("overTime", getOverTime())
            .append("code", getCode())
            .append("userName", getUserName())
            .append("goodsAddress", getGoodsAddress())
            .toString();
    }
}
