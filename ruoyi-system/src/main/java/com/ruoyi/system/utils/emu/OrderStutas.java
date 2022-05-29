package com.ruoyi.system.utils.emu;

import java.util.Arrays;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月21日 14:25:00
 */
public enum OrderStutas {
    /**
     *  订单状态类
     */
    WAIT(1,"等待"),
    CANCEL(3,"已取消"),
    RETURN(4,"已退回"),
    ORDER(2,"已抢单"),
    PAY(6,"待支付"),
    OVER(5,"已结单");

    private Integer id;
    private String stutas;

    OrderStutas(Integer id, String stutas) {
        this.id = id;
        this.stutas = stutas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStutas() {
        return stutas;
    }

    public void setStutas(String stutas) {
        this.stutas = stutas;
    }

    /**
     * 获取订单是否是等待状态
     * @param id 订单id
     * @return 返回值
     */
    public static boolean hasStutasWithWait(Integer id){
      return  Arrays.stream(values()).filter(z-> z.id.equals(id)).findFirst().orElse(CANCEL).id.equals(1);
    }

    public static String getStatus(Integer id){
        return Arrays.stream(values()).filter(z-> z.getId().equals(id)).map(OrderStutas::getStutas).findFirst().orElse("未知");
    }


}
