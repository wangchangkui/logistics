package com.myxiaowang.logistics.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月19日 21:58:00
 */
@Data
@TableName("message")
public class Message {
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("content")
    private String content;
    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    @TableField("pic")
    private String pic;
    @TableField("nick_name")
    private String nickName;
}
