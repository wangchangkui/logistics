package com.myxiaowang.logistics.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myxiaowang.logistics.pojo.Order;

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

    /**
     * 写死的值 不建议修改
     */
    public static void init(){
        Map<String, QueryWrapper<Order>> stringQueryWrapperHashMap = new HashMap<>(8);
        stringQueryWrapperHashMap.put("a",new QueryWrapper<Order>().eq("status",1));
        stringQueryWrapperHashMap.put("b",new QueryWrapper<Order>().eq("status",1).orderByDesc("create_time"));
        stringQueryWrapperHashMap.put("c",new QueryWrapper<Order>().eq("status",1).orderByAsc("money"));
        stringQueryWrapperHashMap.put("d",new QueryWrapper<Order>().eq("status",1).orderByAsc("create_time"));
        stringQueryWrapperHashMap.put("e",new QueryWrapper<Order>().eq("status",1).orderByDesc("money"));
        SELECT.add(stringQueryWrapperHashMap);
        stringQueryWrapperHashMap=new HashMap<>(8);
        stringQueryWrapperHashMap.put("a",new QueryWrapper<Order>().eq("status",1).eq("goods_name","取快递"));
        stringQueryWrapperHashMap.put("b",new QueryWrapper<Order>().eq("status",1).eq("goods_name","取快递").orderByDesc("create_time"));
        stringQueryWrapperHashMap.put("c",new QueryWrapper<Order>().eq("status",1).eq("goods_name","取快递").orderByAsc("money"));
        stringQueryWrapperHashMap.put("d",new QueryWrapper<Order>().eq("status",1).eq("goods_name","取快递").orderByAsc("create_time"));
        stringQueryWrapperHashMap.put("e",new QueryWrapper<Order>().eq("status",1).eq("goods_name","取快递").orderByDesc("money"));
        SELECT.add(stringQueryWrapperHashMap);
        stringQueryWrapperHashMap=new HashMap<>(8);
        stringQueryWrapperHashMap.put("a",new QueryWrapper<Order>().eq("status",1).eq("goods_name","打印"));
        stringQueryWrapperHashMap.put("b",new QueryWrapper<Order>().eq("status",1).eq("goods_name","打印").orderByDesc("create_time"));
        stringQueryWrapperHashMap.put("c",new QueryWrapper<Order>().eq("status",1).eq("goods_name","打印").orderByAsc("money"));
        stringQueryWrapperHashMap.put("d",new QueryWrapper<Order>().eq("status",1).eq("goods_name","打印").orderByAsc("create_time"));
        stringQueryWrapperHashMap.put("e",new QueryWrapper<Order>().eq("status",1).eq("goods_name","打印").orderByDesc("money"));
        SELECT.add(stringQueryWrapperHashMap);
    }


    public static QueryWrapper<Order> getSelect(int v1, String con){
        if(SELECT.size()==0){
            init();
        }
        if(v1>SELECT.size()-1){
            throw new RuntimeException("没有改模块的值");
        }
        return SELECT.get(v1).get(con);
    }
}
