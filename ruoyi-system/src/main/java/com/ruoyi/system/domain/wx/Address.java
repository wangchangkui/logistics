package com.ruoyi.system.domain.wx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jandmla
 * @version 1.0
 * @description 小程序的 地址实体类
 * @data 2022/3/15 13:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "wx_address")
public class Address {
    /**
     * ID 主键 自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 地址信息
     */
    @TableField(value = "address")
    private String address;
    /**
     * 是非被设为默认地址
     */
    @TableField(value = "is_check")
    private Integer isCheck;
    /**
     * 联系人名称
     */
    @TableField(value = "username")
    private String username;
    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;
}
