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
 * @createTime 2022年02月22日 11:00:00
 */
@Data
@TableName(value = "log")
public class Log {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("user_name")
    private String userName;
    @TableField("method")
    private String method;
    @TableField("url")
    private String url;
    @TableField("args")
    private String args;
    @TableField("request_class")
    private String requestClass;
}
