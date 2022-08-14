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
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter =
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
        KeyPair keyPair1 = ksFactory.getKeyPair("oauth-jwt");
        String publicKey = ((RSAPublicKey) keyPair1.getPublic()).getEncoded().toString();
        String privateKey = keyPair1.getPrivate().getEncoded().toString();
        return ksFactory.getKeyPair("oauth-jwt");
    }

    public static PrivateKey getPrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
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
