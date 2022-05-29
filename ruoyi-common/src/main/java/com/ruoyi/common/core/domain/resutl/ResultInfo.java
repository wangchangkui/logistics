package com.ruoyi.common.core.domain.resutl;


/**
 * 接口返回参数
 */
public enum ResultInfo{

    /** 根据项目修改选用合适的错误码**/

    /**
     * 公用模块
     **/
    SUCCESS(200000, "请求成功"),
    EXCEPTION(400000, "出现异常"),
    REQUEST_FAIL(400001, "请求失败"),
    PARAM_ERROR(400002, "参数错误"),
    REQUEST_TIMEOUT(400003, "请求超时"),
    NO_RESULT(400004, "无查询结果"),
    NO_SYSTEM(400005, "请选择操作系统"),
    /**
     * 用户登录模块
     **/
    USER_INVALID(100001, "用户名或密码错误"),
    USER_WRONG_OLD_PASSWORD(100002, "旧密码错误"),
    USER_WRONG_TIWICE_PASSWORD(100003, "两次密码不匹配"),
    USER_FORBIDED(100004, "用户已禁用"),
    USER_NOT_LOGGED_IN(100005, "用户未登录"),
    USER_NOT_ADD_MESSAGE(100006, "用户已存在"),
    USER_WRONG_VERIFICATION_CODE(100007, "验证码错误"),
    USER_RE_LOGIN(100008, "请重新登录"),


    /**
     * 权限模块
     */
    NO_ACCESS_FUNC_PERMISSION(400011, "无此功能访问权限"),
    NO_ACCESS_DATA_PERMISSION(400012, "无此数据访问权限"),

    /**
     * 操作类型
     */
    INSERT_FAIL(400021, "插入失败"),
    UPDATE_FAIL(400022, "更新失败"),
    DELETE_FAIL(400023, "删除失败"),
    INSERT_REAPT(400024, "重复插入相同数据"),
    UPDATE_PASSWORD(400025, "密码未修改"),
    UNABLE_DELETE_CASECADE(400026, "存在关联数据,暂时无法删除");


    private Integer code;
    private String message;

    ResultInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultInfo convertTo(Integer systemId) {
        for (int i = 0; i < ResultInfo.values().length; i++) {
            if (ResultInfo.values()[i].code.equals(systemId)) {
                return ResultInfo.values()[i];
            }
        }
        return EXCEPTION;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
