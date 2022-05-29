package com.ruoyi.system.domain.wx;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.system.domain.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jandmla
 * @version 1.0
 * @description 个人订单表 实体类
 * @data 2022/3/15 14:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "logistics")
public class Logistics {
    /**
     * 主键 id 自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户 ID
     */
    @TableField(value = "user_id",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String userId;

    /**
     * 订单 ID
     */
    private String orderId;
    /**
     * 物流 ID
     */
    @TableField(value = "logistics_id",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String logistics;
    /**
     *  商品 ID
     */
    @TableField(value = "goods",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String goods;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
    /**
     * 结束时间
     */
    @TableField(value = "over_time",fill = FieldFill.INSERT)
    private Date overTime;
    /**
     * 金额
     */
    @TableField(value = "money")
    private BigDecimal money;
    /**
     * 接单用户 ID
     */
    @TableField(value = "get_user_id")
    private String userMeId;
    /**
     * 订单状态
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 交易编号
     */
    @TableField(value = "code")
    private String code;
    /**
     * 订单信息
     */
    @TableField(exist = false)
    private Orders order;
}
