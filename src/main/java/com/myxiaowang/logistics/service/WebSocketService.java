package com.myxiaowang.logistics.service;

import com.myxiaowang.logistics.pojo.User;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:41:00
 */
public interface WebSocketService {


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
