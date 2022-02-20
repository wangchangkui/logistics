package com.myxiaowang.logistics.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myxiaowang.logistics.pojo.Message;
import com.myxiaowang.logistics.pojo.QueryDto.QueryDto;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.WebSocketService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:39:00
 */
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/getMessage")
    public ResponseResult<Page<Message>> getMessage(@RequestBody QueryDto<Message,Message> queryDto){
        return webSocketService.getMessageAll(queryDto);
    }

    @GetMapping("/onlineUser")
    public ResponseResult<List<User>> getOnlineUser(){
        return webSocketService.getOnlineUserInfo();
    }

    @GetMapping("/online")
    public ResponseResult<Integer> getOnlineSocket(){
        return webSocketService.getOnline();
    }
}
