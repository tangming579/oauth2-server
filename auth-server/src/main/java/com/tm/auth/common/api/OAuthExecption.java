package com.tm.auth.common.api;

import lombok.Data;

/**
 * @author tangming
 * @date 2022/8/26
 */
@Data
public class OAuthExecption extends RuntimeException {
    private int status;

    public OAuthExecption(String msg) {
        this(msg, -1);
    }

    public OAuthExecption(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public OAuthExecption(String msg, int status, Throwable e) {
        super(msg, e);
        this.status = status;
    }
}
