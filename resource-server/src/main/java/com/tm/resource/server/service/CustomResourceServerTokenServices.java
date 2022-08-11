package com.tm.resource.server.service;

import com.tm.resource.server.utlis.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;


/**
 * @author: tangming
 * @date: 2022-08-11
 */
@Slf4j
public class CustomResourceServerTokenServices implements ResourceServerTokenServices {

    private final TokenStore tokenStore;
    private static final String RESOURCE_ID = "resource-server";
    private static final String AUTHORIZATION_SERVER_TOKEN_KEY_ENDPOINT_URL = "http://localhost:8080/authorization-server/oauth/token_key";

    public CustomResourceServerTokenServices() {
        this.tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * Description: 用于从 accessToken 中加载凭证信息, 并构建出 {@link OAuth2Authentication} 的方法<br>
     *     Details:
     *
     * @see ResourceServerTokenServices#loadAuthentication(String)
     */
    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        log.debug("CustomResourceServerTokenServices :: loadAuthentication called ...");
        log.trace("CustomResourceServerTokenServices :: loadAuthentication :: accessToken: {}", accessToken);

        return tokenStore.readAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        log.debug("CustomResourceServerTokenServices :: readAccessToken called ...");
        throw new UnsupportedOperationException("暂不支持 readAccessToken!");
    }
    @SuppressWarnings("deprecation")
    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifier(new org.springframework.security.jwt.crypto.sign.RsaVerifier(retrievePublicKey()));
        return jwtAccessTokenConverter;
    }

    private String retrievePublicKey() {
        final RestTemplate restTemplate = new RestTemplate();
        final String responseValue = restTemplate.getForObject(AUTHORIZATION_SERVER_TOKEN_KEY_ENDPOINT_URL, String.class);

        log.debug("{} :: 授权服务器返回原始公钥信息: {}", RESOURCE_ID, responseValue);
        String publicKey = JsonUtil.toJsonNode(responseValue).get("value").asText();
        return publicKey;
    }
}
