package com.tm.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author tangming
 * @date 2022/9/7
 */
@Data
@Component
@ConfigurationProperties(OAuthProperties.PREFIX)
public class OAuthProperties {
    public static final String PREFIX = "paas.auth";

    //token默认有效时间（秒）
    public Integer tokenValiditySecondsDefault;
    //token中权限部分最大长度，如果应用权限信息超过此长度，需要通过接口获取权限
    public Integer tokenAuthoritiesMaxLength;
}
