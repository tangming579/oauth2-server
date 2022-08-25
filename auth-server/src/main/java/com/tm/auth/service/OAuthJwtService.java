package com.tm.auth.service;

import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

/**
 * @author tangming
 * @date 2022/8/25
 */
@Service
public class OAuthJwtService {
    @Autowired
    OauthClientDetailsMapper oauthClientDetailsMapper;

    /**
     * 生成SM2非对称加密秘钥对
     *
     * @return
     */
    public KeyPair generateKeyPair() {
        try {
            KeyPair KeyPair = SM2Util.generateKeyPair();
            return KeyPair;
        } catch (Exception e) {
            return null;
        }
    }

    public String getJwtPrivateKey(String clientId) {
        String key = oauthClientDetailsMapper.selectByPrimaryKey(clientId).getJwtPrivateKey();
        return key;
    }

    public String getJwtPublicKey(String clientId) {
        String key = oauthClientDetailsMapper.selectByPrimaryKey(clientId).getJwtPublicKey();
        return key;
    }
}
