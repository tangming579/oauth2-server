package com.tm.resource.server.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.tm.resource.server.service.CustomResourceServerTokenServices;
import com.tm.resource.server.utlis.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tangming
 * @date 2022/8/10
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    /**
     * 资源服务器 ID
     */
    private static final String RESOURCE_ID = "resource-server";

    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(req -> req.antMatchers("/auth/**").permitAll())
                //oauth2使用jwt模式，会走接口拿取公钥解密验签
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        super.configure(http);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resources.tokenServices(tokenServices());

        resources.resourceId(RESOURCE_ID).stateless(true);
        resources.tokenServices(tokenServices2());
        //resources.authenticationEntryPoint(authenticationEntryPoint);
    }

    public CustomResourceServerTokenServices tokenServices2(){
        CustomResourceServerTokenServices services = new CustomResourceServerTokenServices();
        return services;
    }
}