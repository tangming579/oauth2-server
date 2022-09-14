package com.tm.auth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Data
public class AuthTokenReq {
    @NotBlank(message = "不能为空")
    private String clientId;
    @NotBlank(message = "不能为空")
    private String clientSecret;
    @NotBlank(message = "不能为空")
    private String targetId;
}
