package com.tm.auth.common.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> stringSet) {
        return String.join(SPLIT_CHAR, stringSet);
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {
        return new HashSet<>(Arrays.asList(string.split(",")));
    }
}

