package com.tm.auth.dao;

import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.mbg.model.OauthClientAuthorityRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tangming
 * @date 2022/8/31
 */
public interface OauthClientAuthorityRelDao {
    /**
     * 获取应用所有权限
     */
    List<OauthAuthority> getAuthorities(@Param("clientId") String clientId, @Param("targetId") String targetId);

    int addAuthorities(List<OauthAuthority> authorityList);

    int addClientAuthorityRel(List<OauthClientAuthorityRel> clientAuthorityRels);
}
