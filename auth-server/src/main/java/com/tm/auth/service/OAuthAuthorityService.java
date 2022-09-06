package com.tm.auth.service;

import com.github.pagehelper.util.StringUtil;
import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.dao.OauthClientAuthorityRelDao;
import com.tm.auth.dto.AuthoritiesReq;
import com.tm.auth.dto.AuthorityDto;
import com.tm.auth.dto.OauthAuthorityDto;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.mapper.OauthClientAuthorityRelMapper;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.*;
import com.tm.auth.pojo.Authority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    public int allocClientAuthorities(AuthoritiesReq authoritiesReq) {
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
        if (CollectionUtils.isEmpty(authoritiesReq.getAuthorities())) {
            return 1;
        }
        //插入新关系
        List<OauthClientAuthorityRel> clientAuthorityRelList = new ArrayList<>();
        for (Authority authority : authoritiesReq.getAuthorities()) {
            for (OauthAuthorityDto oauthAuthorityDto : authority.getTargetRules()) {
                OauthAuthority oauthAuthority = new OauthAuthority();
                BeanUtils.copyProperties(oauthAuthorityDto, oauthAuthority);
                oauthAuthority.setTargetId(authority.getTargetId());
                authorityMapper.insertSelective(oauthAuthority);

                OauthClientAuthorityRel rel = new OauthClientAuthorityRel();
                rel.setClientId(authoritiesReq.getClientId());
                rel.setAuthorityId(oauthAuthority.getId());
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
    public Authority getClientAuthorities(String clientId, String targetId) {
        List<OauthAuthority> authorities = authorityRelDao.getAuthorities(clientId, targetId);
        if (CollectionUtils.isEmpty(authorities)) {
            return null;
        }
        Authority authority=new Authority();
        authority.setTargetId(targetId);
        authority.setTargetRules(authorities.stream().map(x->{
            OauthAuthorityDto authorityDto = new OauthAuthorityDto();
            BeanUtils.copyProperties(x, authorityDto);
            return authorityDto;
        }).collect(Collectors.toList()));
        return authority;
    }

    public List<Authority> getClientAuthorities(String clientId) {
        List<OauthAuthority> authorities = authorityRelDao.getAuthoritiesAll(clientId);
        if (CollectionUtils.isEmpty(authorities)) {
            return null;
        }
        List<Authority> authorityList = new ArrayList<>();
        authorities.stream().collect(
                        Collectors.groupingBy(OauthAuthority::getTargetId, TreeMap::new, Collectors.toList()))
                .forEach((key, list) -> {
                    Authority authority = new Authority();
                    authority.setTargetId(key);
                    authority.setTargetRules(list.stream().map(x -> {
                        OauthAuthorityDto authorityDto = new OauthAuthorityDto();
                        BeanUtils.copyProperties(x, authorityDto);
                        return authorityDto;
                    }).collect(Collectors.toList()));
                    authorityList.add(authority);
                });

        return authorityList;
    }
}
