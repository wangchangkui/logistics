package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月21日 16:23:00
 */
@Data
@TableName("business")
public class Business {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("business_id")
    private String businessId;
    @TableField("business_name")
    private String businessName;
    @TableField("business_address")
    private String businessAddress;
    @TableField("business_phone")
    private String businessPhone;
    @TableField("business_person")
    private String person;
    @TableField("url")
    private String url;
}
