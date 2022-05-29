package com.ruoyi.web.controller.wx.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jandmla
 * @version 1.0
 * @description 微信端公告接口
 * @data 2022/3/23 13:43
 */
@RestController
@RequestMapping("/wx/notice")
public class NoticeController {

    private final ISysNoticeService noticeService;

    public NoticeController(ISysNoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 展示通知列表
     */
    @PostMapping("/noticeList")
    public ResponseResult<IPage<SysNotice>> noticeList(@RequestBody Page<SysNotice> noticePage){
        System.out.println("noticePage.toString() = " + noticePage.toString());
        return ResponseResult.success(noticeService.noticeList(noticePage));
    }

    /**
     * 展示通知列表
     */
    @PostMapping("/noticeById/{id}")
    public ResponseResult<SysNotice> noticeById(@PathVariable("id") Integer id){
        System.out.println("id = " + id);
        return ResponseResult.success(noticeService.selectNoticeById((long) id));
    }
}
