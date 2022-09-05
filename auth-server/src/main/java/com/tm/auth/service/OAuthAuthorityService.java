package com.tm.auth.service;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.dao.OauthClientAuthorityRelDao;
import com.tm.auth.dto.AuthoritiesRequest;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.mapper.OauthClientAuthorityRelMapper;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.*;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private OauthClientAuthorityRelDao authorityRelDao;
    @Autowired
    private OauthClientDetailsMapper clientDetailsMapper;
    @Autowired
    private OauthAuthorityMapper authorityMapper;
    @Autowired
    private OauthClientAuthorityRelMapper clientAuthorityRelMapper;

    @Transactional
    public int allocAuthorities(AuthoritiesRequest authoritiesRequest) {
        OauthClientDetails clientDetails = clientDetailsMapper.selectByPrimaryKey(authoritiesRequest.getClientId());
        if (clientDetails == null)
            throw new OAuthExecption("不存在的应用" + authoritiesRequest.getClientId());
        //先删除原有关系
        OauthClientAuthorityRelExample clientAuthorityRelExample = new OauthClientAuthorityRelExample();
        clientAuthorityRelExample.createCriteria().andClientIdEqualTo(authoritiesRequest.getClientId());
        List<OauthClientAuthorityRel> clientAuthorityRels = clientAuthorityRelMapper.selectByExample(clientAuthorityRelExample);
        if (clientAuthorityRels != null) {
            clientAuthorityRelMapper.deleteByExample(clientAuthorityRelExample);
            OauthAuthorityExample authorityExample = new OauthAuthorityExample();
            authorityExample.createCriteria().andIdIn(clientAuthorityRels.stream().map(OauthClientAuthorityRel::getAuthorityId).collect(Collectors.toList()));
            authorityMapper.deleteByExample(authorityExample);
        }
        if (authoritiesRequest.getAuthorities() == null) {
            return 1;
        }
        //插入新关系
        List<OauthClientAuthorityRel> clientAuthorityRelList = new ArrayList<>();
        for (Authority authority : authoritiesRequest.getAuthorities()) {
            OauthClientAuthorityRel rel = new OauthClientAuthorityRel();
            rel.setClientId(authoritiesRequest.getClientId());
            for (OauthAuthority oauthAuthority : authority.getTargetRules()) {
                Integer id = authorityMapper.insert(oauthAuthority);
                rel.setAuthorityId((long) id);
            }
        }
        authorityRelDao.addClientAuthorityRel(clientAuthorityRelList);
        return 1;
    }

    @Transactional
    public int deleteAuthorities(String clientId) {
        OauthClientDetails clientDetails = clientDetailsMapper.selectByPrimaryKey(clientId);
        if (clientDetails == null)
            throw new OAuthExecption("不存在的应用" + clientId);
        OauthClientAuthorityRelExample clientAuthorityRelExample = new OauthClientAuthorityRelExample();
        clientAuthorityRelExample.createCriteria().andClientIdEqualTo(clientId);
        List<OauthClientAuthorityRel> clientAuthorityRels = clientAuthorityRelMapper.selectByExample(clientAuthorityRelExample);
        if (clientAuthorityRels != null) {
            clientAuthorityRelMapper.deleteByExample(clientAuthorityRelExample);
            OauthAuthorityExample authorityExample = new OauthAuthorityExample();
            authorityExample.createCriteria().andIdIn(clientAuthorityRels.stream().map(OauthClientAuthorityRel::getAuthorityId).collect(Collectors.toList()));
            authorityMapper.deleteByExample(authorityExample);
        }
        return 1;
    }

    /**
     * 获取权限信息
     *
     * @param clientId 权限所属应用id
     * @param targetId 权限目标应用id
     * @return
     */
    public Authority getAuthorities(String clientId, String targetId) {
        Optional<List<OauthAuthority>> authorities = Optional.ofNullable(authorityRelDao.getAuthorities(clientId, targetId));
        Authority authority = new Authority();
        authority.setTargetId(targetId);
        authority.setTargetRules(authorities.get());
        return authority;
    }
}
