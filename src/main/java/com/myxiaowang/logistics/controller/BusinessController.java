package com.myxiaowang.logistics.controller;

import com.myxiaowang.logistics.pojo.Business;
import com.myxiaowang.logistics.service.BusinessService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月19日 17:57:00
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/getById/{businessId}")
    public ResponseResult<Business> getBusinessById(@PathVariable("businessId") String businessId){
        return businessService.getBusinessById(businessId);
    }

    @GetMapping("/businessList")
    public ResponseResult<List<Business>> getBusinessList(){
        return businessService.getBusinessList();
    }

}
