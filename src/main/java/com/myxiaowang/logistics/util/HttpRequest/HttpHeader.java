package com.myxiaowang.logistics.util.HttpRequest;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 20:40:00
 */
public enum HttpHeader {
    /*
    * 请求头枚举
    * */
    CONTENT_TYPE("Content-Type","application/json; charset=UTF-8")
    ;

    private String header;
    private String value;

    HttpHeader(String header, String value) {
        this.header = header;
        this.value = value;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
