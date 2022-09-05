package com.tm.auth.service;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.dao.OauthClientAuthorityRelDao;
import com.tm.auth.dto.AuthoritiesReq;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.mapper.OauthClientAuthorityRelMapper;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.*;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private OauthClientAuthorityRelDao authorityRelDao;
    @Autowired
    private OauthClientDetailsMapper clientDetailsMapper;
    @Autowired
    private OauthAuthorityMapper authorityMapper;
    @Autowired
    private OauthClientAuthorityRelMapper clientAuthorityRelMapper;

    @Transactional
    public int allocAuthorities(AuthoritiesReq authoritiesReq) {
        OauthClientDetails clientDetails = clientDetailsMapper.selectByPrimaryKey(authoritiesReq.getClientId());
        if (clientDetails == null)
            throw new OAuthExecption("不存在的应用" + authoritiesReq.getClientId());
        //先删除原有关系
        OauthClientAuthorityRelExample clientAuthorityRelExample = new OauthClientAuthorityRelExample();
        clientAuthorityRelExample.createCriteria().andClientIdEqualTo(authoritiesReq.getClientId());
        List<OauthClientAuthorityRel> clientAuthorityRels = clientAuthorityRelMapper.selectByExample(clientAuthorityRelExample);
        if (!CollectionUtils.isEmpty(clientAuthorityRels)) {
            clientAuthorityRelMapper.deleteByExample(clientAuthorityRelExample);
            OauthAuthorityExample authorityExample = new OauthAuthorityExample();
            authorityExample.createCriteria().andIdIn(clientAuthorityRels.stream().map(OauthClientAuthorityRel::getAuthorityId).collect(Collectors.toList()));
            authorityMapper.deleteByExample(authorityExample);
        }
        if (authoritiesReq.getAuthorities() == null) {
            return 1;
        }
        //插入新关系
        List<OauthClientAuthorityRel> clientAuthorityRelList = new ArrayList<>();
        for (Authority authority : authoritiesReq.getAuthorities()) {
            for (OauthAuthority oauthAuthority : authority.getTargetRules()) {
                oauthAuthority.setTargetId(authority.getTargetId());
                Integer id = authorityMapper.insertSelective(oauthAuthority);

                OauthClientAuthorityRel rel = new OauthClientAuthorityRel();
                rel.setClientId(authoritiesReq.getClientId());
                rel.setAuthorityId((long) id);
                clientAuthorityRelList.add(rel);
            }
        }
        authorityRelDao.addClientAuthorityRel(clientAuthorityRelList);
        return 1;
    }

    @Transactional
    public int deleteAuthorities(String clientId) {
        OauthClientAuthorityRelExample clientAuthorityRelExample = new OauthClientAuthorityRelExample();
        clientAuthorityRelExample.createCriteria().andClientIdEqualTo(clientId);
        List<OauthClientAuthorityRel> clientAuthorityRels = clientAuthorityRelMapper.selectByExample(clientAuthorityRelExample);
        if (!CollectionUtils.isEmpty(clientAuthorityRels)) {
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
