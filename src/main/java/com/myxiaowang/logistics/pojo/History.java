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
@TableName(value = "history")
public class History {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "goods",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String userId;
    @TableField(value = "goods",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String goods;
    @TableField(value = "address",insertStrategy = FieldStrategy.NOT_EMPTY,updateStrategy = FieldStrategy.NOT_EMPTY,whereStrategy = FieldStrategy.NOT_EMPTY)
    private String address;
}
