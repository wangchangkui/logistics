package com.ruoyi.common.core.domain.resutl;

import java.util.Objects;

public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;



    private static volatile ResponseResult responseResult;

    private ResponseResult(){
    }

    private static<T> ResponseResult<T> getInstance() {
        if (Objects.isNull(responseResult)) {
            synchronized (ResponseResult.class) {
                if (Objects.isNull(responseResult)) {
                    responseResult = new ResponseResult<>();
                }

            }
        }
        return responseResult;
    }

    /**
     * 正常数据返回
     * @param data 数据对象
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = getInstance();
        result.setData(data);
        result.setCode(ResultInfo.SUCCESS);
        return result;
    }


    /**
     * 正常数据返回
     * @param data    数据对象
     * @param message 自定义成功信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(T data, String message) {
        ResponseResult<T> result = getInstance();
        result.setData(data);
        result.setCode(ResultInfo.SUCCESS);
        result.setMessage(message);
        return result;
    }

    /**
     * 返回错误类型
     * @param message 消息
     * @param <T> <></>
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(String message){
        ResponseResult<T> responseResult=getInstance();
        responseResult.setMessage(message);
        responseResult.setCode(ResultInfo.NO_RESULT.getCode());
        responseResult.setData(null);
        return responseResult;
    }


    /**
     * 错误数据返回
     * @param responseType 错误数据
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(ResultInfo responseType) {
        ResponseResult<T> result = getInstance();
        result.setData(null);
        result.setCode(responseType.getCode());
        result.setMessage(ResultInfo.convertTo(responseType.getCode()).getMessage());
        return result;
    }

    public static <T> ResponseResult<T> error(Integer code, String message) {
        ResponseResult<T> result = getInstance();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    /**
     * 错误数据返回
     *
     * @param responseType 错误数据
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(ResultInfo responseType, T data) {
        ResponseResult<T> result = getInstance();
        result.setData(data);
        result.setCode(responseType.getCode());
        result.setMessage(responseType.getMessage());
        return result;
    }

    public static <T> ResponseResult<T> error(ResultInfo responseType, String message, T data) {
        ResponseResult<T> result = getInstance();
        result.setData(data);
        result.setCode(responseType.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 错误数据返回
     *
     * @param responseType 错误数据
     * @param message      自定义错误信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> error(ResultInfo responseType, String message) {
        ResponseResult<T> result = getInstance();
        result.setData(null);
        result.setCode(responseType.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回成功的信息
     *
     * @param code 状态码
     * @param message  成功信息
     * @return ResponseResult
     */
    public static <T> ResponseResult<T> success(int code, String message) {
        ResponseResult<T> result = getInstance();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 判断是否是正确返回
     *
     * @return 正确返回则返回true, 否则返回false
     */
    public boolean success() {
        return ResultInfo.SUCCESS.getCode().equals(this.code);
    }

    public void setCode(ResultInfo responseType) {
        this.code = responseType.getCode();
        this.message = responseType.getMessage();
    }

    public void setData(ResultInfo responseType, T data) {
        this.code = responseType.getCode();
        this.message = responseType.getMessage();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
