package com.tm.auth.common.converter;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 自定义访问令牌转换器
 *
 * @author: tangming
 * @date: 2022-08-14
 */

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    /**
     * 生成token（调用“/oauth/token”时触发）
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

        // 将用户信息添加到token额外信息中
        defaultOAuth2AccessToken.getAdditionalInformation().put("test", "自定义信息");
        return super.enhance(defaultOAuth2AccessToken, authentication);
    }

    /**
     * 解析token（调用“/oauth/check_token”时触发）
     *
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
        Map<String, ?> additionalInfo = oauth2AccessToken.getAdditionalInformation();
        return oauth2AccessToken;
    }

    //@Override
    //public void setKeyPair(KeyPair keyPair) {
        //PrivateKey privateKey = keyPair.getPrivate();
        //Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA ");
        //this.signer = new RsaSigner((RSAPrivateKey)privateKey);
        //RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        //this.verifier = new RsaVerifier(publicKey);
        //this.verifierKey = "-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(publicKey.getEncoded())) + "\n-----END PUBLIC KEY-----";
    //}
}
