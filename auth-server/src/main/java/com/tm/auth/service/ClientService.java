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
@Service
@Slf4j
public class ClientService {
    @Autowired
    OauthClientDetailsMapper oauthClientDetailsMapper;

    public void create(OauthClientDetails oauthClientDetails) {
        oauthClientDetailsMapper.insert(oauthClientDetails);
    }
}
