package com.tm.auth.service;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.dao.OauthClientAuthorityRelDao;
import com.tm.auth.dto.AuthoritiesRequest;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.mbg.model.OauthAuthorityExample;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    public List<OauthAuthority> getOAuthAuthoritiesByClientId(String clientId, String targetId) {
        return oauthClientAuthorityRelDao.getPermissionList(clientId, targetId);
    }

    @Transactional
    public int authorities(AuthoritiesRequest authoritiesRequest) {
        OauthClientDetails clientDetails = oauthClientDetailsMapper.selectByPrimaryKey(authoritiesRequest.getClientId());
        if (clientDetails == null)
            throw new OAuthExecption("不存在的应用" + authoritiesRequest.getClientId());
        return 1;
    }

    /**
     * 获取权限信息
     *
     * @param clientId 权限所属应用id
     * @param targetId 权限目标应用id
     * @return
     */
    public Authority getAuthoritiesByClientId(String clientId, String targetId) {
        Optional<List<OauthAuthority>> authorities = Optional.ofNullable(getOAuthAuthoritiesByClientId(clientId, targetId));
        Authority authority = new Authority();
        authority.setTargetId(targetId);
        authority.setTargetRules(authorities.get());
        return authority;
    }
}
