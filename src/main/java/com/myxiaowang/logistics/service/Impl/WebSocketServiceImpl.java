package com.myxiaowang.logistics.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.common.WebSocket.WebSocket;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.WebSocketService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:41:00
 */

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier(value = "webSocketByUser")
    private WebSocket webSocket;

    @Override
    public ResponseResult<List<User>> getOnlineUserInfo() {
        Set<String> onlineUserId = webSocket.getOnlineUserId();
        if(onlineUserId.isEmpty()){
            return ResponseResult.error("暂时无人上线");
        }
        return ResponseResult.success(userMapper.selectList(new QueryWrapper<User>().in("userid",onlineUserId)));
    }

    @Override
    public ResponseResult<Integer> getOnline() {
        return ResponseResult.success(webSocket.getWebsocketSize());
    }
}
