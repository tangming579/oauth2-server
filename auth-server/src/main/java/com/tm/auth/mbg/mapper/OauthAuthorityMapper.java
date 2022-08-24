package com.tm.auth.mbg.mapper;

import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.mbg.model.OauthAuthorityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthAuthorityMapper {
    long countByExample(OauthAuthorityExample example);

    int deleteByExample(OauthAuthorityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OauthAuthority record);

    int insertSelective(OauthAuthority record);

    List<OauthAuthority> selectByExample(OauthAuthorityExample example);

    OauthAuthority selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OauthAuthority record, @Param("example") OauthAuthorityExample example);

    int updateByExample(@Param("record") OauthAuthority record, @Param("example") OauthAuthorityExample example);

    int updateByPrimaryKeySelective(OauthAuthority record);

    int updateByPrimaryKey(OauthAuthority record);
}