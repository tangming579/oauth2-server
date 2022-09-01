package com.tm.auth.common.converter;

import com.tm.auth.common.gmJwt.SM2Signer;
import com.tm.auth.common.gmJwt.SM2Verifier;
import com.tm.auth.common.utils.SMJwtHelper;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Assert;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SM2算法的 JWT 令牌转换器
 *
 * @author: tangming
 * @date: 2022-08-14
 */

public class SM2JwtAccessTokenConverter implements TokenEnhancer, AccessTokenConverter {
    public static final String TOKEN_ID = "jti";
    public static final String TOKEN_EXP = "exp";
    private AccessTokenConverter tokenConverter = new SM2AccessTokenConverter();
    private JsonParser objectMapper = JsonParserFactory.create();
    private Signer signer;
    private SignatureVerifier verifier;

    /**
     * 生成token（调用“/oauth/token”时触发）
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> info = new LinkedHashMap(accessToken.getAdditionalInformation());
        String tokenId = result.getValue();
        if (!info.containsKey(TOKEN_ID)) {
            info.put(TOKEN_ID, tokenId);
        }
        result.setAdditionalInformation(info);
        result.setValue(this.encode(result, authentication));
        return result;
    }

    /**
     *
     * @param oAuth2AccessToken
     * @param oAuth2Authentication
     * @return
     */
    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        return this.tokenConverter.convertAccessToken(oAuth2AccessToken, oAuth2Authentication);
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
        return this.tokenConverter.extractAccessToken(value, map);
    }

    /**
     * 认证信息
     *
     * @param map
     * @return
     */
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        return this.tokenConverter.extractAuthentication(map);
    }

    public void setKeyPair(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.state(privateKey instanceof BCECPrivateKey, "KeyPair must be an SM2");
        this.signer = new SM2Signer((BCECPrivateKey) privateKey);
        PublicKey publicKey = keyPair.getPublic();
        this.verifier = new SM2Verifier((BCECPublicKey) publicKey);
    }

    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        try {
            String content = this.objectMapper.formatMap(this.tokenConverter.convertAccessToken(accessToken, authentication));
            return SMJwtHelper.encode(content, this.signer);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert access token to JSON", e);
        }
    }

    public Map<String, Object> decode(String token) {
        try {
            Jwt jwt = SMJwtHelper.decodeAndVerify(token, this.verifier);
            String claimsStr = jwt.getClaims();
            Map<String, Object> claims = this.objectMapper.parseMap(claimsStr);
            if (claims.containsKey(TOKEN_EXP) && claims.get(TOKEN_EXP) instanceof Integer) {
                Integer intValue = (Integer) claims.get(TOKEN_EXP);
                claims.put(TOKEN_EXP, new Long((long) intValue));
            }
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert access token to JSON", e);
        }
    }
}
