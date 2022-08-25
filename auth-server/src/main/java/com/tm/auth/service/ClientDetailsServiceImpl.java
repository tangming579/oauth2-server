package com.tm.auth.service;

import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.dto.AuthClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

/**
 * @author: tangming
 * @date: 2022-08-10
 */
@Service
@Slf4j
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private AuthClientService clientService;

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
            ClientDetails clientDetails = clientService.getClientDetails(clientId);
            return clientDetails;
        } catch (Exception e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }

    public void create(AuthClientRequest client) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
    }
}