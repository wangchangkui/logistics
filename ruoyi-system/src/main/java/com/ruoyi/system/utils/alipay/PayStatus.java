package com.ruoyi.system.utils.alipay;

import java.util.Optional;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月24日 09:27:00
 */
public enum PayStatus {
    /**
     * 支付状态
     */

    WAIT_BUYER_PAY("WAIT_BUYER_PAY","等待卖家付款"),
    TRADE_CLOSED("TRADE_CLOSED","订单已经关闭"),
    TRADE_SUCCESS("TRADE_SUCCESS","交易成功"),
    TRADE_FINISHED("TRADE_FINISHED","交易已经完成")
    ;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    PayStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String key;
    private String value;

    public static String payInfo(String key){
        return Optional.of(PayStatus.valueOf(key).getValue()).orElse("");
    }
}
