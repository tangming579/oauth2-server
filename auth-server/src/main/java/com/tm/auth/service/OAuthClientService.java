package com.tm.auth.service;

import com.tm.auth.dao.OAuthClientDao;
import com.tm.auth.po.OAuthClient;
import lombok.extern.slf4j.Slf4j;
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
public class OAuthClientService implements ClientDetailsService {

    @Resource
    OAuthClientDao oAuthClientDao;

    @Resource
    PasswordEncoder passwordEncoder;

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
            log.info("查找auth对象 ---> clientId={}", clientId);
            Optional<OAuthClient> oAuthClientOptional = oAuthClientDao.findByClientId(clientId);
            ClientDetails clientDetails = oAuthClientOptional.get();
            log.info("查找auth对象 ---> 完成, clientDetails={}", clientDetails);
            return clientDetails;
        } catch (Exception e) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
        }
    }
}