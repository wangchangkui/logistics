package com.ruoyi.system.plus.mapper.wx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.wx.ChartMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jandmla
 * @version 1.0
 * @description 聊天信息 数据层
 * @data 2022/3/15 14:41
 */
@Mapper
@Component
public interface ChartMessageMapper extends BaseMapper<ChartMessage> {

    /**
     * 查询聊天记录
     * @param page 页面信息
     * @param date 数据信息
     * @return 查询结果
     */
    Page<ChartMessage> getMessageByPage(@Param("page") Page<ChartMessage> page, @Param("date") String date);

    /**
     * 批量插入聊天信息
     * @param messageList 聊天信息列表
     */
    void insertList(@Param("messageList") List<ChartMessage> messageList);
}
