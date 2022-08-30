package com.tm.auth.service;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.pojo.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * @author: tangming
 * @date: 2022-08-10
 */
@Service
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private OAuthClientService clientService;

    /**
     * 获取授权id
     *
     * @param clientId
     * @return
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            OAuthClient clientDetails = clientService.getClientDetails(clientId);
            if (clientDetails == null)
                throw new OAuthExecption("No client with requested id: " + clientId);
            return clientDetails;
        } catch (Exception e) {
            throw new OAuthExecption("loadClientByClientId error: " + e.getMessage());
        }
    }
}