package com.tm.auth.service;

import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.po.OAuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author: tangming
 * @date: 2022-08-10
 */
@Service
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {
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
            ClientService clientService = new ClientService();
            OauthClientDetails clientDetails = clientService.findClientById(clientId);
            OAuthClient oAuthClient = new OAuthClient();
            oAuthClient.setClientId(clientDetails.getClientId());
            oAuthClient.setClientSecret(oAuthClient.getClientSecret());
            oAuthClient.setAccessTokenValiditySeconds(oAuthClient.getAccessTokenValiditySeconds());
            oAuthClient.setAuthorizedGrantTypes("client_credentials");
            return oAuthClient;
        } catch (Exception e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    public void create(OAuthClient client) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
    }
}