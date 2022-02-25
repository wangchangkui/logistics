package com.myxiaowang.logistics.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.pojo.Order;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

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
    private static final ArrayList<Map<String, QueryWrapper<Order>>> SELECT=new ArrayList<>(8);

    public static void init(){
        Map<String, QueryWrapper<Order>> st = new HashMap<>(8);
        st = getStringQueryWrapperMap(st);
        st = getStringQueryWrapperMap(st);
        getMapper(st);
    }

    private static void getMapper(Map<String, QueryWrapper<Order>> stringQueryWrapperHashMap) {
        stringQueryWrapperHashMap.put("a",new QueryWrapper<Order>());
        stringQueryWrapperHashMap.put("b",new QueryWrapper<Order>().orderByDesc("create_time"));
        stringQueryWrapperHashMap.put("c",new QueryWrapper<Order>().orderByAsc("money"));
        stringQueryWrapperHashMap.put("d",new QueryWrapper<Order>().orderByAsc("create_time"));
        stringQueryWrapperHashMap.put("e",new QueryWrapper<Order>().orderByDesc("money"));
        SELECT.add(stringQueryWrapperHashMap);
    }

    @NotNull
    private static Map<String, QueryWrapper<Order>> getStringQueryWrapperMap(Map<String, QueryWrapper<Order>> st) {
        getMapper(st);
        st=new HashMap<>(8);
        return st;
    }


    public static QueryWrapper<Order> getSelect(int v1, String con,String userId){
        if(SELECT.size()==0){
            init();
        }
        if(v1>SELECT.size()-1){
            throw new RuntimeException("没有改模块的值");
        }
        QueryWrapper<Order> orderQueryWrapper = SELECT.get(v1).get(con);
        if(v1 == 1){
            orderQueryWrapper.eq("status",v1);
        }
        if(v1 == 2){
            orderQueryWrapper.eq("status",5);
        }
        if (Strings.isBlank(userId)) {
            return orderQueryWrapper;
        }
        return orderQueryWrapper.eq("user_id",userId);

    }
}
