package com.ruoyi.web.controller.wx.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.ChartMessage;
import com.ruoyi.system.service.wx.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2022年02月18日 14:39:00
 */
@RestController
@RequestMapping("/wx/websocket")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/getMessage")
    public ResponseResult<Page<ChartMessage>> getMessage(@RequestBody QueryDto<ChartMessage,ChartMessage> queryDto){
        return webSocketService.getMessageAll(queryDto);
    }

    @GetMapping("/onlineUser")
    public ResponseResult<List<WxUser>> getOnlineUser(){
        return webSocketService.getOnlineUserInfo();
    }

    @GetMapping("/online")
    public ResponseResult<Integer> getOnlineSocket(){
        return webSocketService.getOnline();
    }
}
