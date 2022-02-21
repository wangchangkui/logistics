package com.myxiaowang.logistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myxiaowang.logistics.pojo.Business;
import com.myxiaowang.logistics.util.Reslut.ResponseResult;
import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月21日 16:35:00
 */
public interface BusinessService extends IService<Business> {

    /**
     * 根据Id获取对应的商户信息
     * @param businessId 商户id
     * @return 商户
     */
    ResponseResult<Business> getBusinessById(String businessId);

    /**
     * 获取所有的商业用户
     * @return 返回集合
     */
    ResponseResult<List<Business>> getBusinessList();
}
