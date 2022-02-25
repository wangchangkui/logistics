package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:16:00
 */
@Data
@TableName("tb_tx")
public class Tx {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private String userId;
    @TableField("money")
    private BigDecimal money;
    @TableField("alipay")
    private String aliPay;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("status")
    private Integer status;
}
