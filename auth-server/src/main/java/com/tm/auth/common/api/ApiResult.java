package com.tm.auth.common.api;

/**
 * @author tangming
 * @date 2022/8/23
 */
public class ApiResult<T> {
    private long status;
    private String message;
    private T data;

    protected ApiResult() {
    }

    protected ApiResult(long status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> ApiResult<T> failed(long status, String message) {
        return new ApiResult<T>(status, message, null);
    }

    public static <T> ApiResult<T> failed(ResultCode resultCode) {
        return new ApiResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
