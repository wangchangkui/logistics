package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月27日 15:39:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "takeparts")
public class TakeParts {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "code")
    private String code;
    @TableField(value = "userid")
    private String userid;
    @TableField(value = "goodsName")
    private String goodsName;
    @TableField(value = "orderid")
    private String orderid;
}
