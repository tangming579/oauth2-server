package com.tm.auth.mbg.mapper;

import com.tm.auth.mbg.model.OauthClientKeypair;
import com.tm.auth.mbg.model.OauthClientKeypairExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthClientKeypairMapper {
    long countByExample(OauthClientKeypairExample example);

    int deleteByExample(OauthClientKeypairExample example);

    int deleteByPrimaryKey(String clientId);

    int insert(OauthClientKeypair record);

    int insertSelective(OauthClientKeypair record);

    List<OauthClientKeypair> selectByExample(OauthClientKeypairExample example);

    OauthClientKeypair selectByPrimaryKey(String clientId);

    int updateByExampleSelective(@Param("record") OauthClientKeypair record, @Param("example") OauthClientKeypairExample example);

    int updateByExample(@Param("record") OauthClientKeypair record, @Param("example") OauthClientKeypairExample example);

    int updateByPrimaryKeySelective(OauthClientKeypair record);

    int updateByPrimaryKey(OauthClientKeypair record);
}