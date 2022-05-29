package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 微信用户信息对象 user
 * 
 * @author 23909
 * @date 2022-03-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("wx_user")
public class WxUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 创建时间 */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新时间 */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 用户名 */
    @TableField("username")
    @Excel(name = "用户名")
    private String username;

    /** 密码 */
    @TableField("password")
    private String password;

    /** 电话 */
    @TableField("phone")
    @Excel(name = "电话")
    private String phone;

    /** 用户id */
    @TableField("user_id")
    @Excel(name = "用户id")
    private String userId;

    /** 用户头像数据 */
    @TableField("header_img")
    @Excel(name = "用户头像数据")
    private String headerImage;

    /** 性别 */
    @TableField("grande")
    @Excel(name = "性别")
    private Long grande;

    /** 金额 */
    @TableField("money")
    @Excel(name = "金额")
    private BigDecimal money;

    /** 版本号 */
    @TableField("version")
    private Long version;

    /** 身份证信息 */
    @TableField("idcard")
    @Excel(name = "身份证信息")
    private String idcard;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal famount;

    @TableField("famount")
    private BigDecimal decimals;

    /** 真实名称 */
    @TableField("name")
    @Excel(name = "真实名称")
    private String name;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setHeaderImg(String headerImg) 
    {
        this.headerImage = headerImg;
    }

    public String getHeaderImg() 
    {
        return headerImage;
    }
    public void setGrande(Long grande) 
    {
        this.grande = grande;
    }

    public Long getGrande() 
    {
        return grande;
    }
    public void setMoney(BigDecimal money) 
    {
        this.money = money;
    }

    public BigDecimal getMoney() 
    {
        return money;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }
    public void setIdcard(String idcard) 
    {
        this.idcard = idcard;
    }

    public String getIdcard() 
    {
        return idcard;
    }
    public void setFamount(BigDecimal famount) 
    {
        this.famount = famount;
    }

    public BigDecimal getFamount() 
    {
        return famount;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setIdCard(String idCard){
        this.idcard = idCard;
    }

    public String getIdCard(){
        return this.idcard;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("phone", getPhone())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("headerImg", getHeaderImg())
            .append("grande", getGrande())
            .append("money", getMoney())
            .append("version", getVersion())
            .append("idcard", getIdcard())
            .append("famount", getFamount())
            .append("name", getName())
            .toString();
    }
}
