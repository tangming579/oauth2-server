package com.tm.auth.dto;

import lombok.Data;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Data
public class AuthTokenRequest {
    private String clientId;
    private String clientSecret;
    private String targetId;
}
