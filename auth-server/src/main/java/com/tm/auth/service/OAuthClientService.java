package com.tm.auth.service;

import com.github.pagehelper.PageHelper;
import com.tm.auth.common.api.CommonPage;
import com.tm.auth.common.utils.SMUtils;
import com.tm.auth.dto.AuthClientRequest;
import com.tm.auth.mbg.mapper.OauthAuthorityMapper;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.mbg.model.OauthClientDetailsExample;
import com.tm.auth.pojo.Authority;
import com.tm.auth.pojo.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Slf4j
@Service
public class OAuthClientService implements ClientDetailsService {
    @Value("${paas.auth.access-token-validity-seconds}")
    private Integer access_token_validity_seconds;
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    @Autowired
    private OauthAuthorityMapper oauthAuthorityMapper;
    @Autowired
    private OAuthJwtService oAuthJwtService;
    @Autowired
    private OAuthAuthorityService oAuthAuthorityService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            return getClientDetailsWithAuthorities(clientId, "paas-server-b");
        } catch (Exception e) {
            log.error("loadClientByClientId error " + e.getMessage(), e);
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    @Transactional
    public int createClient(AuthClientRequest authClientRequest) {
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        BeanUtils.copyProperties(authClientRequest, oauthClientDetails);
        if (authClientRequest.getAccessTokenValidity() == null)
            authClientRequest.setAccessTokenValidity(access_token_validity_seconds);
        String secret = authClientRequest.getClientSecret();
        String pw = SMUtils.sm3HashHex(secret.getBytes(StandardCharsets.UTF_8));
        oauthClientDetails.setClientSecret(pw);
        oauthClientDetails.setCreateTime(new Date());
        oAuthJwtService.generateKeyPair(authClientRequest.getClientId());
        return oauthClientDetailsMapper.insert(oauthClientDetails);
    }

    public int updateClient(OauthClientDetails oauthClientDetails) {
        return oauthClientDetailsMapper.updateByPrimaryKey(oauthClientDetails);
    }

    public int deleteClient(String clientId) {
        return oauthClientDetailsMapper.deleteByPrimaryKey(clientId);
    }

    public OauthClientDetails getClient(String clientId) {
        return oauthClientDetailsMapper.selectByPrimaryKey(clientId);
    }

    public OAuthClient getClientDetailsWithAuthorities(String clientId, String targetId) {
        OauthClientDetails oauthClientDetails = getClient(clientId);
        if (oauthClientDetails == null)
            throw new NoSuchClientException("No client with requested id: " + clientId);
        OAuthClient clientDetails = new OAuthClient();
        BeanUtils.copyProperties(oauthClientDetails, clientDetails);
        List<Authority> authorities = Collections.singletonList(oAuthAuthorityService.getAuthoritiesByClientId(clientId, targetId));
        clientDetails.setAuthorities(authorities);
        return clientDetails;
    }

    public List<OauthClientDetails> listAllClient() {
        return oauthClientDetailsMapper.selectByExample(new OauthClientDetailsExample());
    }

    public CommonPage listPageClient(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OauthClientDetails> list = oauthClientDetailsMapper.selectByExample(new OauthClientDetailsExample());
        return CommonPage.restPage(list);
    }
}
