package com.tm.auth.api;

import com.tm.auth.common.api.ApiResult;
import com.tm.auth.dto.AuthTokenReq;
import com.tm.auth.service.OAuthAuthorityService;
import com.tm.auth.service.OAuthJwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * @author tangming
 * @date 2022/8/23
 */
@RestController
@RequestMapping("/api/oauth")
@Api("授权接口")
@Slf4j
public class OAuthController {
    //令牌请求的端点
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;
    @Autowired
    private OAuthJwtService oAuthJwtService;
    @Autowired
    private OAuthAuthorityService authorityService;
    @Value("${server.port}")
    private Integer serverPort;

    /**
     * 自定义获取令牌接口
     * <p>
     * 优点：1.可以自定义请求参数和返回值；2.更好的支持feign调用 3.可以更方便的捕获所有的异常
     * 缺点：1.密码验证失效了，http请求调用自己；
     *
     * @param request
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @ApiOperation("获取令牌")
    @PostMapping(value = "/token")
    public ApiResult getToken(@Valid @RequestBody AuthTokenReq request) throws HttpRequestMethodNotSupportedException, IOException {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.set("grant_type", "client_credentials");
        paramsMap.set("client_id", request.getClientId());
        paramsMap.set("client_secret", request.getClientSecret());
        paramsMap.set("target_id", request.getTargetId());

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false); // 解决401报错时，报java.net.HttpRetryException: cannot retry due to server authentication, in streaming mode
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = String.format("http://127.0.0.1:%s/oauth/token", serverPort);
        OAuth2AccessToken response = restTemplate.postForObject(url, paramsMap, OAuth2AccessToken.class);
        OAuth2AccessToken oAuth2AccessToken = response;
        return ApiResult.success(oAuth2AccessToken);
    }

    @ApiOperation("校验令牌")
    @PostMapping(value = "/check_token")
    public ApiResult checkToken(@RequestParam("token") String value) {
        Map<String, ?> map = checkTokenEndpoint.checkToken(value);
        return ApiResult.success(map);
    }

    @ApiOperation("获取某个资源公钥")
    @GetMapping("/token_key")
    public ApiResult getKey(String clientId) {
        return ApiResult.success(oAuthJwtService.getJwtPublicKey(Collections.singletonList(clientId)));
    }

    @ApiOperation("获取所有资源公钥")
    @PostMapping("/token_key_all")
    public ApiResult getKeyAll() {
        return ApiResult.success(oAuthJwtService.getJwtPublicKey(Collections.emptyList()));
    }

    @ApiOperation("获取应用权限列表")
    @PostMapping("/getAuthorities")
    @ResponseBody
    public ApiResult getAuthorities(String clientId, String targetId) {
        return ApiResult.success(authorityService.getClientAuthorities(clientId, targetId));
    }
}
