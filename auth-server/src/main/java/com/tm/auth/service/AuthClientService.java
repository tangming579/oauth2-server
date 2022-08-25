package com.tm.auth.service;

import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.mbg.model.OauthClientDetailsExample;
import com.tm.auth.pojo.AuthClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Slf4j
@Service
public class AuthClientService {
    @Autowired
    OauthClientDetailsMapper oauthClientDetailsMapper;

    public int createClient(OauthClientDetails oauthClientDetails) {
        return oauthClientDetailsMapper.insert(oauthClientDetails);
    }

    public OauthClientDetails findClientById(String clientId) {
        return oauthClientDetailsMapper.selectByPrimaryKey(clientId);
    }

    public int deleteClient(String clientId) {
        return oauthClientDetailsMapper.deleteByPrimaryKey(clientId);
    }

    public ClientDetails getClientDetails(String clientId) {
        OauthClientDetails oauthClientDetails = findClientById(clientId);
        ClientDetails clientDetails = new AuthClientDetails();
        BeanUtils.copyProperties(oauthClientDetails, clientDetails);
        return clientDetails;
    }

    public List<OauthClientDetails> getList() {
        return oauthClientDetailsMapper.selectByExample(new OauthClientDetailsExample());
    }
}
