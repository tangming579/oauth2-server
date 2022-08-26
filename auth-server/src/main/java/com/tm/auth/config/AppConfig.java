package com.tm.auth.config;

import com.google.common.collect.ImmutableMap;
import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author tangming
 * @date 2022/8/24
 */
@Configuration
@EnableSwagger2WebMvc
public class AppConfig {
    @Bean
    public Docket createRestApi() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("RESTful APIs").description("auth-server")
                .version("1.0").build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                //为当前包下controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.tm.auth.api"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public FilterRegistrationBean corsFilterRegistration() {
        CORSFilter filter = new CORSFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        ImmutableMap<String, String> initParameters = ImmutableMap.<String, String>builder()
                .put("cors.allowOrigin", "*")
                .put("cors.supportedMethods", "GET,POST,PUT,DELETE,PATCH")
                .put("cors.supportedHeaders", "Content-Type,username,authorization,teamspace,project,onbehalfuser,tenant")
                .build();
        registration.setInitParameters(initParameters);
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE));
        registration.setMatchAfter(false);
        registration.setUrlPatterns(Arrays.asList("/*"));
        return registration;
    }

    @Bean
    public FilterRegistrationBean characterEncodingFilterRegistration() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setDispatcherTypes(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE));
        registration.setMatchAfter(false);
        registration.setUrlPatterns(Arrays.asList("/*"));
        return registration;
    }
}
