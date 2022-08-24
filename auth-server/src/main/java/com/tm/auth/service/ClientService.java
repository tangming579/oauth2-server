package com.tm.auth.service;

import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Slf4j
public class ClientService {
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
}
