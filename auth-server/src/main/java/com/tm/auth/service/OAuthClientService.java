package com.tm.auth.service;

import com.github.pagehelper.PageHelper;
import com.tm.auth.common.api.CommonPage;
import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.common.utils.SMUtils;
import com.tm.auth.config.OAuthProperties;
import com.tm.auth.dto.AuthorityDto;
import com.tm.auth.dto.ClientCreateReq;
import com.tm.auth.dto.ClientDetailsDto;
import com.tm.auth.dto.ClientUpdateReq;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Slf4j
@Service
public class OAuthClientService implements ClientDetailsService {
    @Autowired
    private OAuthProperties oAuthProperties;
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    @Autowired
    private OAuthJwtService oAuthJwtService;
    @Autowired
    private OAuthAuthorityService oAuthAuthorityService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            //??????????????????targetId???????????????????????????
            //???????????????????????????????????????????????????id?????????token??????????????????????????????
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String targetId = request.getParameter("target_id");
            return getClientDetailsWithAuthorities(clientId, targetId);
        } catch (Exception e) {
            log.error("loadClientByClientId error " + e.getMessage(), e);
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    @Transactional
    public int createClient(ClientCreateReq clientCreateReq) {
        OauthClientDetails clientDetails = oauthClientDetailsMapper.selectByPrimaryKey(clientCreateReq.getClientId());
        if (Objects.nonNull(clientDetails)) {
            throw new OAuthExecption(String.format("??????%s?????????", clientDetails.getClientId()));
        }
        //??????token?????????????????????????????????
        if (clientCreateReq.getAccessTokenValiditySeconds() == null)
            clientCreateReq.setAccessTokenValiditySeconds(oAuthProperties.tokenValiditySecondsDefault);
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        BeanUtils.copyProperties(clientCreateReq, oauthClientDetails);

        //????????????sm3 hash??????
        String secret = clientCreateReq.getClientSecret();
        String pwHash = SMUtils.sm3HashHex(secret.getBytes(StandardCharsets.UTF_8));
        oauthClientDetails.setClientSecret(pwHash);
        oauthClientDetails.setCreateTime(new Date());
        //?????????????????????jwt????????????
        oAuthJwtService.generateKeyPair(clientCreateReq.getClientId());
        return oauthClientDetailsMapper.insert(oauthClientDetails);
    }

    public int updateClient(ClientUpdateReq clientUpdateReq) {
        OauthClientDetails clientDetails = oauthClientDetailsMapper.selectByPrimaryKey(clientUpdateReq.getClientId());
        if (Objects.isNull(clientDetails)) {
            throw new OAuthExecption(String.format("??????%s?????????", clientDetails.getClientId()));
        }
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        BeanUtils.copyProperties(clientUpdateReq, oauthClientDetails);
        oauthClientDetails.setClientSecret(clientDetails.getClientSecret());
        return oauthClientDetailsMapper.updateByPrimaryKeySelective(oauthClientDetails);
    }

    @Transactional
    public int deleteClient(String clientId) {
        oAuthJwtService.deleteJwtKeypair(clientId);
        oAuthAuthorityService.deleteAuthorities(clientId);
        return oauthClientDetailsMapper.deleteByPrimaryKey(clientId);
    }

    public OauthClientDetails getClient(String clientId) {
        return oauthClientDetailsMapper.selectByPrimaryKey(clientId);
    }

    /**
     * ???????????????????????????????????????????????? jwt ??????
     *
     * @param clientId
     * @param targetId
     * @return
     */
    public OAuthClient getClientDetailsWithAuthorities(String clientId, String targetId) {
        OauthClientDetails oauthClientDetails = getClient(clientId);
        if (oauthClientDetails == null)
            throw new NoSuchClientException("No client with requested id: " + clientId);
        OAuthClient clientDetails = new OAuthClient();
        BeanUtils.copyProperties(oauthClientDetails, clientDetails);
        List<Authority> authorities = Collections.singletonList(oAuthAuthorityService.getClientAuthorities(clientId, targetId));
        clientDetails.setAuthorities(authorities);
        return clientDetails;
    }

    public List<OauthClientDetails> listAllClient() {
        return oauthClientDetailsMapper.selectByExample(new OauthClientDetailsExample());
    }

    public CommonPage listPageClient(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OauthClientDetails> oauthClientDetailsList = oauthClientDetailsMapper.selectByExample(new OauthClientDetailsExample());
        CommonPage<OauthClientDetails> pageInfo = CommonPage.restPage(oauthClientDetailsList);
        List<ClientDetailsDto> clientDetailsDtos = oauthClientDetailsList
                .stream().map(x -> {
                    ClientDetailsDto dto = new ClientDetailsDto();
                    BeanUtils.copyProperties(x, dto);
                    return dto;
                }).collect(Collectors.toList());
        CommonPage<ClientDetailsDto> pageInfoDto = CommonPage.restPage(clientDetailsDtos);
        pageInfoDto.setPageNum(pageInfo.getPageNum());
        pageInfoDto.setPageSize(pageInfo.getPageSize());
        pageInfoDto.setTotalPage(pageInfo.getTotalPage());
        pageInfoDto.setTotal(pageInfo.getTotal());
        return pageInfoDto;
    }
}
