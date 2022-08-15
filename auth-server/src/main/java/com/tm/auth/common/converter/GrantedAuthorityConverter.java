package com.tm.auth.common.converter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;

/**
 * @author: tangming
 * @date: 2022-08-14
 */
@Converter
public class GrantedAuthorityConverter implements AttributeConverter<List<GrantedAuthority>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<GrantedAuthority> grantedAuthorities) {
        if (grantedAuthorities == null)
            return null;
        return String.join(SPLIT_CHAR, AuthorityUtils.authorityListToSet(grantedAuthorities));
    }

    @Override
    public List<GrantedAuthority> convertToEntityAttribute(String s) {
        if (!StringUtils.hasText(s))
            return Collections.emptyList();
        return AuthorityUtils.commaSeparatedStringToAuthorityList(s);
    }
}