package com.tm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Configuration
public class AccessTokenConfig {
    /**
     * 密钥对
     */
    @Resource
    private KeyPair keyPair;

    /**
     * 基于内存的token存储bean
     *
     * @return
     */
    @Bean
    TokenStore tokenStore() {
        //return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
        //return new JwkTokenStore("http://localhost:8080/authorization-server/.well-known/jwks.json");
    }


    /**
     * 配置令牌的创建及验签方式
     * 基于此对象创建的令牌信息会封装到OAuth2AccessToken类型的对象中
     * 然后再存储到TokenStore对象，外界需要时，会从tokenStore进行获取。
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter=
                new JwtAccessTokenConverter();
        //jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);//设置密钥
        //非对称加密签名
        jwtAccessTokenConverter.setKeyPair(this.keyPair);
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //获取资源文件中的密钥
        ClassPathResource ksFile = new ClassPathResource("jwt.jks");
        //输入密码创建KeyStoreKeyFactory
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, "password".toCharArray());
        //通过别名获取KeyPair
        return ksFactory.getKeyPair("oauth-jwt");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        //默认编码算法的Id,新的密码编码都会使用这个id对应的编码器
        String idForEncode = "bcrypt";
        //要支持的多种编码器
        //举例：历史原因，之前用的SHA-1编码，现在我们希望新的密码使用bcrypt编码
        //老用户使用SHA-1这种老的编码格式，新用户使用bcrypt这种编码格式，登录过程无缝切换
        Map encoders = new HashMap();
        encoders.put(idForEncode,new BCryptPasswordEncoder());
        //encoders.put("SHA-1",new MessageDigestPasswordEncoder("SHA-1"));

        //（默认编码器id，编码器map）
        return new DelegatingPasswordEncoder(idForEncode,encoders);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //非对称加密签名
        converter.setKeyPair(keyPair);
        //对称加密签名
        //converter.setSigningKey("xxx");
        return converter;
    }
}
