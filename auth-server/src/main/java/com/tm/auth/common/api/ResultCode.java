package com.tm.auth.common.api;

/**
 * @author tangming
 * @date 2022/8/23
 */
public enum ResultCode {
    SUCCESS(0, "成功"),
    FAILED(-1, "失败"),
    VALIDATE_FAILED(100000, "参数检验失败"),
    AUTHORIZATION_LOST(100001, "http head中缺少Authorization"),
    TOKEN_LOST(100002, "缺少token"),
    UNAUTHORIZED(100003, "未经许可的客户端，请检查id和secret"),
    OFFLINE(100004, "应用不处于上线状态"),
    TOKEN_ILLEGAL(100005, "token非法"),
    TOKEN_EXPIRED(100006, "token已过期"),
    PERMISSION_FAILED(100007, "获取应用具有的权限列表失败"),
    FORBIDDEN(100008, "权限不足，拒绝访问");
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
