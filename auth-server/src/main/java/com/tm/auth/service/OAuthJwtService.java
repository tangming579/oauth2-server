package com.tm.auth.service;

import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.common.utils.SMUtils;
import com.tm.auth.dto.PublicKeyInfo;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.mapper.OauthClientKeypairMapper;
import com.tm.auth.mbg.model.OauthClientKeypair;
import com.tm.auth.mbg.model.OauthClientKeypairExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.KeyPair;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tangming
 * @date 2022/8/25
 */
@Service
public class OAuthJwtService {
    @Autowired
    OauthClientKeypairMapper oauthClientKeypairMapper;

    /**
     * 生成SM2非对称加密秘钥对
     *
     * @return
     */
    public Integer generateKeyPair(String clientId) {
        //为服务生成公私钥
        Map.Entry<String, String> keyPair = SMUtils.generateSM2Key();
        OauthClientKeypair keypair = new OauthClientKeypair();
        keypair.setClientId(clientId);
        keypair.setPrivateKey(keyPair.getKey());
        keypair.setPublicKey(keyPair.getValue());
        return oauthClientKeypairMapper.insert(keypair);
    }

    public OauthClientKeypair getJwtKeypair(String clientId) {
        return oauthClientKeypairMapper.selectByPrimaryKey(clientId);
    }

    public String getJwtPrivateKey(String clientId) {
        return oauthClientKeypairMapper.selectByPrimaryKey(clientId).getPrivateKey();
    }

    public String getJwtPublicKey(String clientId) {
        return oauthClientKeypairMapper.selectByPrimaryKey(clientId).getPublicKey();
    }

    public List<PublicKeyInfo> getJwtPublicKey(List<String> clientIds) {
        OauthClientKeypairExample example = new OauthClientKeypairExample();
        if (!CollectionUtils.isEmpty(clientIds)) {
            example.createCriteria().andClientIdIn(clientIds);
        }
        return oauthClientKeypairMapper.selectByExample(example).stream()
                .map(x -> {
                    PublicKeyInfo info = new PublicKeyInfo();
                    info.setPublicKey(x.getPublicKey());
                    info.setClientId(x.getClientId());
                    return info;
                }).collect(Collectors.toList());
    }
}
