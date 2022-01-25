package com.myxiaowang.logistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.pojo.Lbt;
import com.myxiaowang.logistics.service.LbtService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月18日 14:40:00
 */
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private LbtService lbtService;

    @GetMapping("/lbt")
    public ResponseResult<List<Lbt>> getLbt(){
        return ResponseResult.success(lbtService.list(new QueryWrapper<Lbt>().eq("isShow", 1)),"请求成功");
    }

}
