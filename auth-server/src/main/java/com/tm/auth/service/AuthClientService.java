package com.tm.auth.service;

import com.github.pagehelper.PageHelper;
import com.tm.auth.common.api.CommonPage;
import com.tm.auth.common.utils.SMUtils;
import com.tm.auth.dto.AuthClientRequest;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.mbg.model.OauthClientDetailsExample;
import com.tm.auth.pojo.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Slf4j
@Service
public class AuthClientService {
    @Value("${paas.auth.access-token-validity-seconds}")
    private Integer access_token_validity_seconds;
    @Autowired
    private OauthClientDetailsMapper oauthClientDetailsMapper;

    public int createClient(AuthClientRequest authClientRequest) {
        OauthClientDetails oauthClientDetails = new OauthClientDetails();
        BeanUtils.copyProperties(authClientRequest, oauthClientDetails);
        //为服务生成公私钥
        Map.Entry<String, String> keyPair = SMUtils.generateSM2Key();
        oauthClientDetails.setJwtPrivateKey(keyPair.getKey());
        oauthClientDetails.setJwtPublicKey(keyPair.getValue());

        if (authClientRequest.getAccessTokenValidity() == null)
            authClientRequest.setAccessTokenValidity(access_token_validity_seconds);
        String secret = authClientRequest.getClientSecret();
        String pw = SMUtils.sm3HashHex(secret.getBytes(StandardCharsets.UTF_8));
        oauthClientDetails.setClientSecret(pw);
        oauthClientDetails.setCreateTime(new Date());
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

    public OAuthClient getClientDetails(String clientId) {
        OauthClientDetails oauthClientDetails = getClient(clientId);
        if (oauthClientDetails == null) return null;
        OAuthClient clientDetails = new OAuthClient();
        BeanUtils.copyProperties(oauthClientDetails, clientDetails);
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
