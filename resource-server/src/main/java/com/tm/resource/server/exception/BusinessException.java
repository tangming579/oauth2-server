package com.tm.resource.server.exception;

/**
 * @author tangming
 * @date 2022/8/11
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }
}
