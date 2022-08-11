package com.tm.auth.config;

import com.tm.auth.service.OAuthClientService;
import com.tm.auth.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Collections;

/**
 * @author tangming
 * @date 2022/8/9
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 密钥对
     */
    @Resource
    private KeyPair keyPair;

    @Resource
    TokenStore tokenStore;

    @Resource
    JwtAccessTokenConverter accessTokenConverter;
    /**
     * 密码编码器
     */
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
                // 设置可以请求接入点的安全表达式为`permitAll()`
                .tokenKeyAccess("permitAll()")
                // 设置检查token的安全表达式为`isAuthenticated()`，已认证
                .checkTokenAccess("permitAll()")
                // 允许进行表单认证
                .allowFormAuthenticationForClients()
                // 设置oauth_client_details中的密码编码器
                .passwordEncoder(passwordEncoder);
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
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenServices(tokenServices()); // 设置检验token 服务配置
    }

    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端服务
        services.setClientDetailsService(clientService);
        // 存储令牌方式
        services.setTokenStore(tokenStore);
        //3.设置令牌增强(改变默认令牌创建方式，没有这句话默认是UUID)
        services.setTokenEnhancer(accessTokenConverter);
        // 令牌有效期
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        // 支持令牌刷新
        services.setSupportRefreshToken(true);
        // 刷新令牌有效期
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }
}
