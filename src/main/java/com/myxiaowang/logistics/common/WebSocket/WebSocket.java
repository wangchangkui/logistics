package com.myxiaowang.logistics.common.WebSocket;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



/**
 * @author Myxiaowang
 */
@ServerEndpoint(value = "/live",encoders = WebSocketEncoder.class)
@Component(value = "webSocketByUser")
@Data
public class WebSocket {

    private static final Logger logger = LoggerFactory.getLogger(WebSocket.class);
    /**
     * 连接缓存
     */
    private static Map<String, Session> webSocket = new ConcurrentHashMap<>(16);

    private String userId;
    private static final String USERKEY="userId";


    public Set<String> getOnlineUserId(){
        return  webSocket.keySet();
    }

    public int getWebsocketSize(){
        return webSocket.size();
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws IOException {
        logger.info(session.getRequestParameterMap().get("userName").get(0)+"已经连接上websocket");
        List<String> userId = session.getRequestParameterMap().get("id");
        this.userId=userId.get(0);
        webSocket.put(userId.get(0),session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info(session.getRequestParameterMap().get("userName").get(0)+"断开了连接");
        webSocket.remove(session.getRequestParameterMap().get("id").get(0));
        session.close();
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage(maxMessageSize = 56666)
    public void onMessage(String message) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(message);
        // 代表要发送的用户 默认携带from
        String to = jsonObject.getString(USERKEY);
        if(StringUtils.isNotEmpty(to)){
            webSocket.get(to).getBasicRemote().sendText(message);
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
