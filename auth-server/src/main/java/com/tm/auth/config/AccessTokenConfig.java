package com.tm.auth.config;

import com.tm.auth.common.converter.SM2JwtAccessTokenConverter;
import com.tm.auth.common.gmJwt.SM2JwtTokenStore;
import com.tm.auth.common.gmUtils.SM2Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;
import java.security.KeyPair;

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
        return new SM2JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 配置令牌的创建及验签方式
     * 基于此对象创建的令牌信息会封装到OAuth2AccessToken类型的对象中
     * 然后再存储到TokenStore对象，外界需要时，会从tokenStore进行获取。
     */
    @Bean
    public SM2JwtAccessTokenConverter jwtAccessTokenConverter() {
        SM2JwtAccessTokenConverter jwtAccessTokenConverter = new SM2JwtAccessTokenConverter();
        //非对称加密签名
        jwtAccessTokenConverter.setKeyPair(this.keyPair);
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        try {
            KeyPair KeyPair = SM2Util.generateKeyPair();
            return KeyPair;
        } catch (Exception e) {
            return null;
        }
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        //默认编码算法的Id,新的密码编码都会使用这个id对应的编码器
//        String idForEncode = "bcrypt";
//        Map encoders = new HashMap();
//        encoders.put(idForEncode,new BCryptPasswordEncoder());
//        //（默认编码器id，编码器map）
//        return new DelegatingPasswordEncoder(idForEncode,encoders);
//    }
}
