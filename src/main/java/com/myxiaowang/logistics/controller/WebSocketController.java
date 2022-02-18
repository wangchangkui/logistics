package com.myxiaowang.logistics.controller;

import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.WebSocketService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/onlineUser")
    public ResponseResult<List<User>> getOnlineUser(){
        return webSocketService.getOnlineUserInfo();
    }

    @GetMapping("/online")
    public ResponseResult<Integer> getOnlineSocket(){
        return webSocketService.getOnline();
    }
}
