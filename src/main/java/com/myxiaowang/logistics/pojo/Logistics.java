package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 09:31:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "logistics")
public class Logistics {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "userId",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String userId;
    @TableField(value = "logisticsid",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String logistics;
    @TableField(value = "goods",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String goods;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "over_time",fill = FieldFill.INSERT)
    private Date overTime;
    @TableField(value = "money")
    private BigDecimal money;
    @TableField(value = "getuser")
    private String getUser;
    @TableField(value = "status")
    private Integer status;
    @TableField(value = "code")
    private String code;
}
