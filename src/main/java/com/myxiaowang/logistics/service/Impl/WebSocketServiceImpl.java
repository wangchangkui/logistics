package com.myxiaowang.logistics.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.common.WebSocket.WebSocket;
import com.myxiaowang.logistics.dao.MessageMapper;
import com.myxiaowang.logistics.dao.UserMapper;
import com.myxiaowang.logistics.pojo.Message;
import com.myxiaowang.logistics.pojo.QueryDto.QueryDto;
import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.service.WebSocketService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:41:00
 */

@Service
public class WebSocketServiceImpl extends ServiceImpl<MessageMapper, Message> implements WebSocketService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    @Qualifier(value = "webSocketByUser")
    private WebSocket webSocket;

    @Override
    public ResponseResult<Page<Message>> getMessageAll(QueryDto<Message,Message> messageQueryDto) {
        Timestamp createTime = messageQueryDto.getEntity().getCreateTime();
        String timestamp=null;
        if(createTime != null){
            timestamp=createTime.toString();
        }
        Page<Message> messageByPage = messageMapper.getMessageByPage(messageQueryDto.getPage(), timestamp);
        // 如果没有传日期
        if(ObjectUtils.isNull(messageQueryDto.getEntity().getCreateTime())){
            ArrayList<Message> myMessages = WebSocket.theMessage;
            if(myMessages.size()>0){
                ArrayList<Message> collect = new ArrayList<>(messageByPage.getRecords());
                collect.addAll(myMessages);
                collect = collect.stream().sorted(Comparator.comparing(Message::getCreateTime)).collect(Collectors.toCollection(ArrayList::new));
                messageByPage.setRecords(collect);
            }
        }
        return ResponseResult.success(messageByPage);
    }

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
