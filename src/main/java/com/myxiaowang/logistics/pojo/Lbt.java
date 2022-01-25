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
 * @createTime 2022年01月18日 14:36:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "lbt")
public class Lbt {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "header_url")
    private String headerUrl;
    @TableField(value = "isShow")
    private Integer isShow;
}
