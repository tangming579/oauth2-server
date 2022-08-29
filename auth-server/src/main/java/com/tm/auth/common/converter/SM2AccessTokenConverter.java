package com.tm.auth.common.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.*;

/**
 * @author tangming
 * @date 2022/8/29
 */
public class SM2AccessTokenConverter extends DefaultAccessTokenConverter {
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, String> parameters = new HashMap();
        Collection<? extends GrantedAuthority> authorities = null;
        if (map.containsKey("authorities")) {
            //String[] roles = (String[])((Collection)map.get("authorities")).toArray(new String[0]);
            //authorities = AuthorityUtils.createAuthorityList(roles);
        }
        String clientId = "paas-server-a";

        OAuth2Request request = new OAuth2Request(parameters, clientId, authorities, true, null, null, (String) null, (Set) null, (Map) null);
        return new OAuth2Authentication(request, null);
    }
}
