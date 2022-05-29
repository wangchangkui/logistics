package com.ruoyi.system.domain.wx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Jandmla
 * @version 1.0
 * @description 聊天室信息
 * @data 2022/3/15 14:31
 */
@Data
@TableName("message")
public class ChartMessage {
    /**
     * ID 主键 自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    /**
     * 发送信息的 用户 ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 聊天信息
     */
    @TableField("content")
    private String content;
    /**
     * 发送信息时间
     */
    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    /**
     * 发送消息的人的头像地址
     */
    @TableField("pic")
    private String pic;
    /**
     * 发消息人 昵称
     */
    @TableField("nick_name")
    private String nickName;
}
