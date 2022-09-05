package com.tm.auth.common.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.*;

/**
 * @author tangming
 * @date 2022/8/29
 */
public class SM2AccessTokenConverter extends DefaultAccessTokenConverter {
    private String clientIdAttribute = "client_id";
    private String scopeAttribute = "scope";

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, String> parameters = new HashMap();
        String clientId = (String) map.get(this.clientIdAttribute);
        parameters.put(this.clientIdAttribute, clientId);
        Collection<? extends GrantedAuthority> authorities = null;
        if (map.containsKey("allocAuthorities")) {
            //String[] roles = (String[])((Collection)map.get("allocAuthorities")).toArray(new String[0]);
            //allocAuthorities = AuthorityUtils.createAuthorityList(roles);
        }

        OAuth2Request request = new OAuth2Request(parameters, clientId, authorities, true, null, null, (String) null, null, null);
        return new OAuth2Authentication(request, null);
    }

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, ?> accessToken = super.convertAccessToken(token, authentication);
        accessToken.remove(scopeAttribute);
        return accessToken;
    }
}
