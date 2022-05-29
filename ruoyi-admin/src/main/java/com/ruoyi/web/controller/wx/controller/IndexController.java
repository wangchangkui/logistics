package com.ruoyi.web.controller.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.resutl.ResponseResult;
import com.ruoyi.system.domain.Carousel;
import com.ruoyi.system.service.wx.LbtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 14:40:00
 */
@RestController
@RequestMapping("/wx/index")
public class IndexController {
    @Autowired
    private LbtService lbtService;

    @GetMapping("/lbt")
    public ResponseResult<List<Carousel>> getLbt(){
//        return ResponseResult.success(lbtService.list(new QueryWrapper<Carousel>().eq("isShow", 1)),"请求成功");
        LambdaQueryWrapper<Carousel> eq = new LambdaQueryWrapper<Carousel>().eq(Carousel::getShows, 1);
        return ResponseResult.success(lbtService.list(eq),"请求成功");
    }

}
