package com.tm.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Configuration
@MapperScan({"com.tm.auth.mbg.mapper"})
public class MyBatisConfig {
}
