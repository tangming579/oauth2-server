package com.tm.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/9
 */
@Configuration
public class WebConfig {
    /**
     * 编码器创建
     * @return PasswordEncoder
     */
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
    public KeyPair keyPair() {
        //获取资源文件中的密钥
        ClassPathResource ksFile = new ClassPathResource("jwt.jks");
        //输入密码创建KeyStoreKeyFactory
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, "password".toCharArray());
        //通过别名获取KeyPair
        return ksFactory.getKeyPair("oauth-jwt");
    }
}
