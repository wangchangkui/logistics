package com.myxiaowang.logistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myxiaowang.logistics.pojo.Arrears;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月21日 18:11:00
 */
@Mapper
public interface ArrearsMapper extends BaseMapper<Arrears> {
    Arrears getUserArre(Integer id);
    @MapKey("id")
    List<Map<String,Object>> getArrearsMap(String userId);
}
