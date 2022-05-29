package com.ruoyi.system.utils.websocket;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ruoyi.system.domain.wx.ChartMessage;
import com.ruoyi.system.plus.mapper.wx.ChartMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



/**
 * @author Myxiaowang
 */
@ServerEndpoint(value = "/wx/live",encoders = WebSocketEncoder.class)
@Component(value = "webSocketByUser")
public class WebSocket {

    private ChartMessageMapper messageMapper;
    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    /**
     * 连接缓存
     */
    private static final Map<String, Session> WEB_SOCKET = new ConcurrentHashMap<>(16);
    public static ArrayList<ChartMessage> theMessage=new ArrayList<>(100);
    private static final int SIZE=100;
    /**
     * 心跳监测
     */
    private static final String PONG="ping";



    public void setMapper(ChartMessageMapper messageMapper){
        this.messageMapper=messageMapper;
    }


    public Set<String> getOnlineUserId(){
        return  WEB_SOCKET.keySet();
    }

    public int getWebsocketSize(){
        return WEB_SOCKET.size();
    }

    @Scheduled(cron = "${Scheduled.message}")
    public void insertMessage(){
        if(ObjectUtils.isNull(messageMapper)){
            messageMapper=SpringUtil.getBean(ChartMessageMapper.class);
        }
        if(WebSocket.theMessage.size()>0){
            messageMapper.insertList(WebSocket.theMessage);
            WebSocket.theMessage=new ArrayList<>(100);
        }
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session)  {
        logger.info(session.getRequestParameterMap().get("userName").get(0)+"已经连接上websocket");
        if(ObjectUtils.isNull(messageMapper)){
            messageMapper=SpringUtil.getBean(ChartMessageMapper.class);
        }
        List<String> userId = session.getRequestParameterMap().get("id");
        WEB_SOCKET.put(userId.get(0),session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info(session.getRequestParameterMap().get("userName").get(0)+"断开了连接");
        WEB_SOCKET.remove(session.getRequestParameterMap().get("id").get(0));
        session.close();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage(maxMessageSize = 56666)
    public void onMessage(String message) throws IOException {
        // 往数据库存入消息
        ChartMessage sendMessage = JSONObject.parseObject(message, ChartMessage.class);
        if(PONG.equals(sendMessage.getContent())){
            logger.info("收到心跳请求....");
            WEB_SOCKET.get(sendMessage.getUserId()).getBasicRemote().sendText("pong");
            return;
        }
        if(theMessage.size()>=SIZE){
            // 往数据库里面存入消息
            messageMapper.insertList(theMessage);
            // 清空arrayList
            theMessage= new ArrayList<>(100);
        }
        theMessage.add(sendMessage);
        // 香所有人发送消息
        for (Map.Entry<String, Session> send : WEB_SOCKET.entrySet()) {
            if(send.getKey().equals(sendMessage.getUserId())){
                continue;
            }
            send.getValue().getBasicRemote().sendText(message);
        }

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        error.printStackTrace();
        onClose(session);
    }
}
