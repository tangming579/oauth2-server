package com.tm.auth.common.converter;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.service.OAuthJwtService;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

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
    private OAuthJwtService oAuthJwtService;

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

    public void setOAuthJwtService(OAuthJwtService oAuthJwtService) {
        this.oAuthJwtService = oAuthJwtService;
    }

    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        try {
            String content = this.objectMapper.formatMap(this.tokenConverter.convertAccessToken(accessToken, authentication));
            return oAuthJwtService.encodePayloadToJwtStr(content);
        } catch (Exception e) {
            throw new OAuthExecption("encode access token error" + e.getMessage());
        }
    }

    public Map<String, Object> decodeTokenToMap(String token) {
        return oAuthJwtService.decodeJwtStrAndVerify(token);
    }
}
