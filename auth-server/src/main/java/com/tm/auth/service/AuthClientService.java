package com.tm.auth.service;

import com.github.pagehelper.PageHelper;
import com.tm.auth.common.api.CommonPage;
import com.tm.auth.mbg.mapper.OauthClientDetailsMapper;
import com.tm.auth.mbg.model.OauthClientDetails;
import com.tm.auth.mbg.model.OauthClientDetailsExample;
import com.tm.auth.pojo.AuthClientDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public int updateClient(OauthClientDetails oauthClientDetails) {
        return oauthClientDetailsMapper.updateByPrimaryKey(oauthClientDetails);
    }

    public int deleteClient(String clientId) {
        return oauthClientDetailsMapper.deleteByPrimaryKey(clientId);
    }

    public OauthClientDetails getClient(String clientId) {
        return oauthClientDetailsMapper.selectByPrimaryKey(clientId);
    }

    public ClientDetails getClientDetails(String clientId) {
        OauthClientDetails oauthClientDetails = getClient(clientId);
        AuthClientDetails clientDetails = new AuthClientDetails();
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
