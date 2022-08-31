package com.tm.auth.service;

import com.tm.auth.dao.OauthClientAuthorityRelDao;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.mbg.model.OauthAuthorityExample;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.BeanUtils;
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
    private OauthClientAuthorityRelDao oauthClientAuthorityRelDao;

    public List<OauthAuthority> getOAuthAuthoritiesByClientId(String targetId) {
        return oauthClientAuthorityRelDao.getPermissionList(targetId);
    }

    public List<Authority> getAuthoritiesByClientId(String targetId) {
        Optional<List<OauthAuthority>> authorities = Optional.ofNullable(getOAuthAuthoritiesByClientId(targetId));
        return authorities.map(oauthAuthorities -> oauthAuthorities.stream().map(x -> {
            Authority authority = new Authority();
            BeanUtils.copyProperties(x, authority);
            return authority;
        }).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
