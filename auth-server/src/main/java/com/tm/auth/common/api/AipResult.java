package com.tm.auth.common.api;

/**
 * @author tangming
 * @date 2022/8/23
 */
public class AipResult<T> {
    private long status;
    private String message;
    private T data;

    protected AipResult() {
    }

    protected AipResult(long status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> AipResult<T> success(T data) {
        return new AipResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> AipResult<T> success(T data, String message) {
        return new AipResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> AipResult<T> failed(String message) {
        return new AipResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> AipResult<T> failed(long status, String message) {
        return new AipResult<T>(status, message, null);
    }

    public static <T> AipResult<T> failed(ResultCode resultCode) {
        return new AipResult<T>(resultCode.getCode(), resultCode.getMessage(), null);
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
