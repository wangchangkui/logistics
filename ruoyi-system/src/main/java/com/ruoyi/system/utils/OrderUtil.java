package com.ruoyi.system.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.Orders;

import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月17日 10:00:00
 */


public class OrderUtil {

    private static final ArrayList<Map<String, QueryWrapper<Orders>>> SELECT = new ArrayList<>(8);

    public static void init() {
        Map<String, QueryWrapper<Orders>> st = new HashMap<>(8);
        try {
            st = getStringQueryWrapperMap(st);
            st = getStringQueryWrapperMap(st);
            st = getStringQueryWrapperMap(st);
            st = getStringQueryWrapperMap(st);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getMapper(st);
    }

    private static void getMapper(Map<String, QueryWrapper<Orders>> stringQueryWrapperHashMap) {
        // 查询全部
        stringQueryWrapperHashMap.put("a", new QueryWrapper<Orders>());
        // 床架时间 降序
        stringQueryWrapperHashMap.put("b", new QueryWrapper<Orders>().orderByDesc("create_time"));
        // 金钱 升序
        stringQueryWrapperHashMap.put("c", new QueryWrapper<Orders>().orderByAsc("money"));
        // 创建时间 升序
        stringQueryWrapperHashMap.put("d", new QueryWrapper<Orders>().orderByAsc("create_time"));
        // 金钱 降序
        stringQueryWrapperHashMap.put("e", new QueryWrapper<Orders>().orderByDesc("money"));
        SELECT.add(stringQueryWrapperHashMap);
    }


    private static Map<String, QueryWrapper<Orders>> getStringQueryWrapperMap(Map<String, QueryWrapper<Orders>> st) throws Exception {

        if (null == st) {
            throw new Exception("控制正异常");
        }
        getMapper(st);
        st = new HashMap<>(8);
        return st;
    }


    public static Wrapper<Orders> getSelect(int v1, String con, String userId) {

        LambdaQueryWrapper<Orders> orderQueryWrapper;
        if (v1 == 0){
            orderQueryWrapper = new LambdaQueryWrapper<Orders>();
        } else {
            orderQueryWrapper = new LambdaQueryWrapper<Orders>().eq(Orders::getStatus, v1);
        }
        if (Strings.isNotBlank(userId)) {
            orderQueryWrapper.eq(Orders::getUserId, userId);
        }

        switch (con){
            case "a":
            case "b": orderQueryWrapper.orderByAsc(Orders::getCreateTime);break;
            case "c": orderQueryWrapper.orderByAsc(Orders::getMoney);break;
            case "d": orderQueryWrapper.orderByDesc(Orders::getCreateTime);break;
            case "e": orderQueryWrapper.orderByDesc(Orders::getMoney);break;

            default: break;
        }
        return orderQueryWrapper;
    }
}
