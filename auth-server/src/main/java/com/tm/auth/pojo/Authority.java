package com.tm.auth.pojo;

import com.tm.auth.common.utils.JsonUtil;
import com.tm.auth.mbg.model.OauthAuthority;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author: tangming
 * @date: 2022-08-27
 */
public class Authority extends OauthAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        String jsonStr = "path: " + this.getPaths() + ",methods:" + this.getMethods();
        return jsonStr;
    }
}
