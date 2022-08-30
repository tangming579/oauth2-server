package com.tm.auth.service;

import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.mbg.model.OauthAuthorityExample;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author tangming
 * @date 2022/8/30
 */
@Service
public class OAuthAuthorityService {
    @Autowired
    private OauthAuthorityMapper oauthAuthorityMapper;

    public List<OauthAuthority> getOAuthAuthoritiesByClientId(String targetId) {
        OauthAuthorityExample example = new OauthAuthorityExample();
        example.createCriteria().andTargetIdEqualTo(targetId);
        return oauthAuthorityMapper.selectByExample(example);
    }

    public List<Authority> getAuthoritiesByClientId(String targetId) {
        Optional<List<OauthAuthority>> authorities = Optional.ofNullable(getOAuthAuthoritiesByClientId(targetId));
        if (!authorities.isPresent()) return Collections.emptyList();
        return authorities.get().stream().map(x->(Authority)x).collect(Collectors.toList());
    }
}
