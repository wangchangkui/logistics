package com.myxiaowang.logistics.util.Enum;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年01月28日 09:26:00
 */
public enum OrderByEnum {
    A("a"),B("b"),C("c"),D("d"),E("e");
    private String code;

    OrderByEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
