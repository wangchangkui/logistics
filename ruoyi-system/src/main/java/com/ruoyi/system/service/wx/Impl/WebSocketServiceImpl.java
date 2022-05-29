package com.ruoyi.system.service.wx.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.ChartMessage;
import com.ruoyi.system.plus.mapper.wx.ChartMessageMapper;
import com.ruoyi.system.plus.mapper.wx.UserMapper;
import com.ruoyi.system.service.wx.WebSocketService;
import com.ruoyi.system.utils.websocket.WebSocket;
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
public class WebSocketServiceImpl extends ServiceImpl<ChartMessageMapper, ChartMessage> implements WebSocketService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChartMessageMapper messageMapper;

    @Autowired
    @Qualifier(value = "webSocketByUser")
    private WebSocket webSocket;

    @Override
    public ResponseResult<Page<ChartMessage>> getMessageAll(QueryDto<ChartMessage,ChartMessage> messageQueryDto) {
        Timestamp createTime = messageQueryDto.getEntity().getCreateTime();
        String timestamp=null;
        if(createTime != null){
            timestamp=createTime.toString();
        }
        Page<ChartMessage> messageByPage = messageMapper.getMessageByPage(messageQueryDto.getPage(), timestamp);
        // 如果没有传日期
        if(ObjectUtils.isNull(messageQueryDto.getEntity().getCreateTime())){
            ArrayList<ChartMessage> myMessages = WebSocket.theMessage;
            if(myMessages.size()>0){
                ArrayList<ChartMessage> collect = new ArrayList<>(messageByPage.getRecords());
                collect.addAll(myMessages);
                collect = collect.stream().sorted(Comparator.comparing(ChartMessage::getCreateTime)).collect(Collectors.toCollection(ArrayList::new));
                messageByPage.setRecords(collect);
            }
        }
        return ResponseResult.success(messageByPage);
    }

    @Override
    public ResponseResult<List<WxUser>> getOnlineUserInfo() {
        Set<String> onlineUserId = webSocket.getOnlineUserId();
        if(onlineUserId.isEmpty()){
            return ResponseResult.error("暂时无人上线");
        }
        return ResponseResult.success(userMapper.selectList(new QueryWrapper<WxUser>().in("user_id",onlineUserId)));
    }

    @Override
    public ResponseResult<Integer> getOnline() {
        return ResponseResult.success(webSocket.getWebsocketSize());
    }
}
