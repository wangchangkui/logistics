package com.ruoyi.system.utils.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @类名 WebSocketEncoder
 * @描述 在WebSocket中使用 sendObject() 方法，在此对Object对象进行处理
 * @author 23909
 * @创建日期 2021/12/1 16:42
 * @版本号 1.0
 */
@Component
public class WebSocketEncoder implements Encoder.Text<String>{

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(String object) throws EncodeException {
        return object;
    }
}
