package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:30:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO )
    private Integer id;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "user_id")
    private String userid;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableField(value = "header_img")
    private String headerImage;
    @TableField(value = "grande")
    private Integer sex;
    @TableField(exist = false)
    private String address;
    @TableField(value = "money")
    private BigDecimal decimals;
    @TableField(value = "version")
    private Integer version;
    @TableField(value = "idcard")
    private String idCard;
    @TableField(value = "famount")
    private BigDecimal famount;
    @TableField(value = "name")
    private String name;
    @TableField(exist = false)
    private BigDecimal arre;
}
