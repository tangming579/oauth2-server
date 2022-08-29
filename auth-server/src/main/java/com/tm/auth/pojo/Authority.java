package com.tm.auth.pojo;

import com.google.common.collect.Maps;
import com.tm.auth.common.utils.JsonUtil;
import com.tm.auth.mbg.model.OauthAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: tangming
 * @date: 2022-08-27
 */
public class Authority extends OauthAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        Map<String, Object> authInfo = Maps.newHashMap();
        authInfo.put("targetId", this.getTargetId());
        authInfo.put("methods", this.getMethods());
        authInfo.put("paths", this.getPaths());
        String jsonStr = JsonUtil.toJsonString(authInfo);
        return jsonStr;
    }
}
