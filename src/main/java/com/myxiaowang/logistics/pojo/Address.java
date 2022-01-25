package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:31:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "address")
public class Address {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "userid")
    private String userId;
    @TableField(value = "address")
    private String address;
    @TableField(value = "isCheck")
    private Integer isCheck;
    @TableField(value = "username")
    private String username;
    @TableField(value = "phone")
    private String phone;
}
