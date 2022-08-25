package com.tm.auth.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

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
     * 权限范围
     * read，write等可自定义
     */
    @NotNull(message = "授权范围不能为空")
    private Set<String> scope = new LinkedHashSet<>(0);

    /**
     * 资源id集合
     */
    @NotNull(message = "授权范围不能为空")
    private Set<String> resourceIds = new LinkedHashSet<>(0);

    /**
     * accessToken 有效时间
     * 单位:秒
     */
    @NotNull(message = "accessToken有效时间不能为空")
    private Integer accessTokenValiditySeconds;

    /**
     * accessToken 刷新时间
     * 单位:秒
     */
    private Integer refreshTokenValiditySeconds;

    /**
     * 指定客户端所拥有的Spring Security的权限值
     */
    private List<GrantedAuthority> authorities;
}
