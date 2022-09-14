package com.tm.auth.pojo;

import com.tm.auth.mbg.model.OauthAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * @author: tangming
 * @date: 2022-08-27
 */
public class JwtAuthority extends OauthAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        if (Objects.isNull(this.getClientId())) return null;
        String jsonStr = "path: " + this.getPaths() + ",methods:" + this.getMethods();
        return jsonStr;
    }
}
