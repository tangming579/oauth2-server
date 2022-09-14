package com.tm.auth.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Data
@ToString
public class ClientCreateReq {

    /**
     * 客户端ID
     */
    @NotBlank(message = "不能为空")
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "不能为空")
    private String clientSecret;

    /**
     * 客户端名称
     */
    @NotBlank(message = "不能为空")
    private String clientName;

    /**
     * 客户端描述
     */
    private String clientDesc;

    /**
     * accessToken 有效时间
     * 单位:秒
     */
    @Min(value = 60, message = "最小为60")
    private Integer accessTokenValiditySeconds;
}
