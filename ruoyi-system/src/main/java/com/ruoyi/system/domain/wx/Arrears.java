package com.ruoyi.system.domain.wx;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.system.domain.WxUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author Jandmla
 * @version 1.0
 * @description 微信未支付订单 信息实体类
 * @data 2022/3/15 14:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "arrears")
public class Arrears {
    /**
     * ID 主键 自增
     */
    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;
    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private String userId;
    /**
     * 钱
     */
    @TableField(value = "money")
    private BigDecimal money;
    /**
     * 交易名称
     */
    @TableField(value = "goods_name")
    private String goodsName;
    /**
     * 订单 ID
     */
    @TableField(value = "order_id")
    private String orderId;
    /**
     * 订单 联系人 昵称
     */
    @TableField(value = "username")
    private String username;
    /**
     * 订单创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;
    /**
     * 完成订单人 ID
     */
    @TableField(value = "arruser_id")
    private String arrUserid;

    @TableField(exist = false)
    private WxUser me;
}
