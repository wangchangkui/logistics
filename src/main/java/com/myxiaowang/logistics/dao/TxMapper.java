package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myxiaowang.logistics.pojo.Tx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月23日 11:21:00
 */
@Mapper
public interface TxMapper extends BaseMapper<Tx> {
    Page<Tx> getUserTx(@Param("page") Page<Tx> txPage,@Param("userId") String userId);
}
