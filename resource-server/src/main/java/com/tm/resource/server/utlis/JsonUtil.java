package com.tm.resource.server.utlis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.tm.resource.server.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.setDefaultPrettyPrinter(new MinimalPrettyPrinter());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T parseObject(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new BusinessException("parseObject from string failed：" + e.getMessage());
        }
    }

    public static <T> T parseObject(InputStream inputStream, Class<T> tClass) throws IOException {
        try {
            Reader reader = new InputStreamReader(inputStream);
            return mapper.readValue(reader, tClass);
        } catch (Exception e) {
            throw new BusinessException("parseObject from stream failed：" + e.getMessage());
        }
    }

    public static Map<String, Object> parseMap(String json) {
        try {
            return mapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new BusinessException("parseMap failed：" + e.getMessage());
        }
    }

    public static <T> List<T> parseList(String json, Class<T> tClass) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, tClass);
            List<T> list = mapper.readValue(json, javaType);
            return list;
        } catch (Exception e) {
            throw new BusinessException("parseList failed：" + e.getMessage());
        }
    }

    public static String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new BusinessException("toJsonString failed：" + e.getMessage());
        }
    }

    public static JsonNode toJsonNode(String json) {
        try {
            JsonNode jsonNode = mapper.readTree(json);
            return jsonNode;
        } catch (Exception e) {
            throw new BusinessException("toJsonNode failed：" + e.getMessage());
        }
    }

    public static JsonNode toJsonNode(Object object) {
        try {
            String jsonStr = mapper.writeValueAsString(object);
            JsonNode jsonNode = mapper.readTree(jsonStr);
            return jsonNode;
        } catch (Exception e) {
            throw new BusinessException("toJsonNode failed：" + e.getMessage());
        }
    }
}