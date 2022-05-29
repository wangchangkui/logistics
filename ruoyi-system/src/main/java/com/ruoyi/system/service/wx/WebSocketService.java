package com.ruoyi.system.service.wx;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.QueryDto.QueryDto;
import com.ruoyi.system.domain.WxUser;
import com.ruoyi.system.domain.wx.ChartMessage;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月18日 14:41:00
 */
public interface WebSocketService extends IService<ChartMessage> {

    /**
     * 获取时间内的消息
     * @param messageQueryDto 请求产出
     * @return 返回结果
     */
    ResponseResult<Page<ChartMessage>> getMessageAll(QueryDto<ChartMessage,ChartMessage> messageQueryDto);
    /**
     * 获取在线用户的信息
     * @return 集合
     */
    ResponseResult<List<WxUser>> getOnlineUserInfo();
    /**
     * 获取登录用户
     * @return 返回在线用户数量
     */
    ResponseResult<Integer> getOnline();
}
