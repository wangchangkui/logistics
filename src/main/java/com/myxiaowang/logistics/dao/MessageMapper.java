package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myxiaowang.logistics.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月19日 22:09:00
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {


    Page<Message> getMessageByPage(@Param("page") Page<Message> page,@Param("date") String date);

    void insertList(@Param("messageList") List<Message> messageList);
}
