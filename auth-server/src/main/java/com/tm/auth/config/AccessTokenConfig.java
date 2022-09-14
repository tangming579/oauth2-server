package com.tm.auth.config;

import com.tm.auth.common.converter.SM2JwtAccessTokenConverter;
import com.tm.auth.common.gmJwt.SM2JwtTokenStore;
import com.tm.auth.service.OAuthJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Configuration
public class AccessTokenConfig {
    @Autowired
    private OAuthJwtService oAuthJwtService;

    /**
     * 基于国密SM2的token存储bean
     *
     * @return
     */
    @Bean
    TokenStore tokenStore() {
        return new SM2JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 配置令牌的创建及验签方式
     * 基于此对象创建的令牌信息会封装到OAuth2AccessToken类型的对象中
     * 然后再存储到TokenStore对象，外界需要时，会从tokenStore进行获取。
     */
    @Bean
    public SM2JwtAccessTokenConverter jwtAccessTokenConverter() {
        SM2JwtAccessTokenConverter converter = new SM2JwtAccessTokenConverter();
        converter.setOAuthJwtService(oAuthJwtService);
        return converter;
    }
}
