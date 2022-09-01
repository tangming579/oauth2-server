package com.tm.auth.config;

import com.tm.auth.common.converter.SM2JwtAccessTokenConverter;
import com.tm.auth.common.gmJwt.SM3PasswordEncoder;
import com.tm.auth.service.OAuthClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * @author tangming
 * @date 2022/8/9
 */
@EnableAuthorizationServer
@Configuration
@Order(2)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    TokenStore tokenStore;
    @Resource
    SM2JwtAccessTokenConverter accessTokenConverter;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    OAuthClientService clientService;


    /**
     * 配置授权服务器的 token 接入点
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //获取令牌的服务端点是公开的
                .tokenKeyAccess("permitAll()")
                //校验令牌的服务端点是公开的
                .checkTokenAccess("permitAll()")
                // 允许进行表单方式获取令牌
                .allowFormAuthenticationForClients();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SM3PasswordEncoder();
    }

    /**
     * 配置 Jdbc 版本的 JdbcClientDetailsService
     * 也就是读取oauth_client_details表的信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 自定义
        clients.withClientDetails(clientService);
    }

    /**
     * 配置授权访问的接入点
     *
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.authenticationManager(authenticationManager)
                // 设置检验token 服务配置
                .tokenServices(tokenServices());
                // 自定义异常翻译器
                //.exceptionTranslator(providerExceptionHandler);
    }

    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端服务
        services.setClientDetailsService(clientService);
        // 存储令牌方式
        services.setTokenStore(tokenStore);
        // 设置令牌增强(改变默认令牌创建方式，没有这句话默认是UUID)
        services.setTokenEnhancer(accessTokenConverter);
        // 令牌有效期
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        // 不支持令牌刷新
        services.setSupportRefreshToken(false);
        return services;
    }
}
