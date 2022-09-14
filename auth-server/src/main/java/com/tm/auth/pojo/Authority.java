package com.tm.auth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.tm.auth.common.utils.JsonUtil;
import com.tm.auth.dto.OauthAuthorityDto;
import com.tm.auth.mbg.model.OauthAuthority;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: tangming
 * @date: 2022-08-27
 */
@Data
public class Authority implements GrantedAuthority {
    private String targetId;
    private List<OauthAuthorityDto> targetRules;

    public Authority() {
        targetRules = new ArrayList<>();
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        if (StringUtils.isEmpty(targetId) && CollectionUtils.isEmpty(targetRules))
            return null;
        Map<String, Object> authInfo = Maps.newHashMap();
        authInfo.put("targetId", targetId);
        if (!CollectionUtils.isEmpty(targetRules)) {
            List list = targetRules.stream().map(x -> {
                Map<String, Object> authDetails = Maps.newHashMap();
                authDetails.put("methods", x.getMethods());
                authDetails.put("paths", x.getPaths());
                return authDetails;
            }).collect(Collectors.toList());
            authInfo.put("targetRules", list);
        }
        String jsonStr = JsonUtil.toJsonString(authInfo);
        return jsonStr;
    }
}
