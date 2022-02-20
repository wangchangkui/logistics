package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.Message;
import com.myxiaowang.logistics.pojo.QueryDto.QueryDto;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:41:00
 */
public interface WebSocketService extends IService<Message> {

    /**
     * 获取时间内的消息
     * @param messageQueryDto 请求产出
     * @return 返回结果
     */
    ResponseResult<Page<Message>> getMessageAll(QueryDto<Message,Message> messageQueryDto);
    /**
     * 获取在线用户的信息
     * @return 集合
     */
    ResponseResult<List<User>> getOnlineUserInfo();
    /**
     * 获取登录用户
     * @return 返回在线用户数量
     */
    ResponseResult<Integer> getOnline();
}
