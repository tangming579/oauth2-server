package com.tm.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

/**
 * Authorization Server 的安全配置
 *
 * @author tangming
 * @date 2022/8/9
 */
@Order(1)
@Configuration
class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/.well-known/jwks.json"))
                .authorizeRequests(req -> req.mvcMatchers("/.well-known/jwks.json").permitAll());
    }
}
