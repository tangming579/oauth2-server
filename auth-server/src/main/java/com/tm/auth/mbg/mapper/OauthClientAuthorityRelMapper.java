package com.tm.auth.mbg.mapper;

import com.tm.auth.mbg.model.OauthClientAuthorityRel;
import com.tm.auth.mbg.model.OauthClientAuthorityRelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthClientAuthorityRelMapper {
    long countByExample(OauthClientAuthorityRelExample example);

    int deleteByExample(OauthClientAuthorityRelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OauthClientAuthorityRel record);

    int insertSelective(OauthClientAuthorityRel record);

    List<OauthClientAuthorityRel> selectByExample(OauthClientAuthorityRelExample example);

    OauthClientAuthorityRel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OauthClientAuthorityRel record, @Param("example") OauthClientAuthorityRelExample example);

    int updateByExample(@Param("record") OauthClientAuthorityRel record, @Param("example") OauthClientAuthorityRelExample example);

    int updateByPrimaryKeySelective(OauthClientAuthorityRel record);

    int updateByPrimaryKey(OauthClientAuthorityRel record);
}