package com.tm.auth.po;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Data
@ToString
public class OAuthClient implements ClientDetails {
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
     * 授权类型
     * client_credentials 客户端模式
     */
    @NotBlank(message = "授权类型不能为空")
    private String authorizedGrantTypes;

    /**
     * 指定客户端所拥有的Spring Security的权限值
     */
    private List<GrantedAuthority> authorities;


    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override

    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return false;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return new HashSet<>(Arrays.asList(this.authorizedGrantTypes.split(",")));
    }
}
