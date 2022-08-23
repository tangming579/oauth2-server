package com.tm.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangming
 * @date 2022/8/23
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {

    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;
    @Autowired
    private TokenKeyEndpoint tokenKeyEndpoint;
}
