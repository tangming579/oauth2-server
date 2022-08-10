package com.tm.resource.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tangming
 * @date 2022/8/10
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/auth/**"))
                .authorizeRequests(req -> req
                        .antMatchers("/auth/**").permitAll()
                )
                //oauth2使用jwt模式，会走接口拿取公钥解密验签
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt()
                );
        super.configure(http);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }

    /**
     *  配置资源服务器如何验证token有效性
     *  1. DefaultTokenServices
     *     如果认证服务器和资源服务器同一服务时,则直接采用此默认服务验证即可
     *  2. RemoteTokenServices (当前采用这个)
     *     当认证服务器和资源服务器不是同一服务时, 要使用此服务去远程认证服务器验证
     * */
    public ResourceServerTokenServices tokenServices(){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/authorization-server/oauth/check_token");
        remoteTokenServices.setClientId("resource-server");
        remoteTokenServices.setClientSecret("12345678");
        return remoteTokenServices;
    }
}
