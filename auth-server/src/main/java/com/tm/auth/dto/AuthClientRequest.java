package com.tm.auth.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Data
@ToString
public class AuthClientRequest {

    /**
     * 客户端ID
     */
    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "客户端密码不能为空")
    private String clientSecret;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端描述
     */
    private String clientDesc;

    /**
     * accessToken 有效时间
     * 单位:秒
     */
    @NotNull(message = "accessToken有效时间不能为空")
    @Min(value = 60, message = "排序最小为60")
    private Integer accessTokenValidity;
}
