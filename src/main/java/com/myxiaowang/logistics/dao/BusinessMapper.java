package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myxiaowang.logistics.pojo.Business;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月21日 16:22:00
 */
@Mapper
public interface BusinessMapper extends BaseMapper<Business> {

    /**
     * 查询商户集合
     * @param businessPage 商户分页
     * @param query 请求参数
     * @return 返回结果
     */
    Page<Business>  selectBusinessList(@Param("businessPage") Page<Business> businessPage, @Param("query") Business query);
}
