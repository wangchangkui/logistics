package com.myxiaowang.logistics.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myxiaowang.logistics.dao.BusinessMapper;
import com.myxiaowang.logistics.pojo.Business;
import com.myxiaowang.logistics.pojo.QueryDto.QueryDto;
import com.myxiaowang.logistics.service.BusinessService;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月21日 16:36:00
 */
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business> implements BusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public ResponseResult<Business> getBusinessById(String businessId) {
        return ResponseResult.success(getOne(new QueryWrapper<Business>().eq("business_id",businessId)));
    }

    @Override
    public ResponseResult<Page<Business>> getBusinessList(QueryDto<Business,Business> queryDto) {
        Page<Business> businessPage = businessMapper.selectBusinessList(queryDto.getPage(), queryDto.getEntity());
        return ResponseResult.success(businessPage);
    }
}
