package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 提现管理对象 tb_tx
 * 
 * @author 23909
 * @date 2022-03-10
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("wx_tx")
@Accessors(chain = true)
public class Withdraw extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id 主键 自增 */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 提现用户Id */
    @TableField("user_id")
    @Excel(name = "提现用户Id")
    private String userId;

    /** 提现金额 */
    @TableField("money")
    @Excel(name = "提现金额")
    private BigDecimal money;

    /** 支付宝账号 */
    @TableField("alipay")
    @Excel(name = "支付宝账号")
    private String alipay;

    /**
     * 创建 时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_time")
    private Timestamp createTime;

    /** 提现状态 */
    @TableField("status")
    @Excel(name = "提现状态")
    private Integer status;

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
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }
    public void setAlipay(String alipay) 
    {
        this.alipay = alipay;
    }

    public String getAlipay() 
    {
        return alipay;
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
            .append("userId", getUserId())
            .append("money", getMoney())
            .append("alipay", getAlipay())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .toString();
    }
}
