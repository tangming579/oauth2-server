package com.tm.auth.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangming
 * @date 2022/9/5
 */
@Data
public class ClientUpdateReq {
    /**
     * 客户端ID
     */
    @NotBlank(message = "不能为空")
    private String clientId;

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
    @NotNull(message = "不能为空")
    @Min(value = 60, message = "最小为60")
    private Integer accessTokenValiditySeconds;
}
