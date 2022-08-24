package com.tm.auth.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.tm.auth.common.api.CommonResult;
import com.tm.auth.common.gmUtils.BCECUtil;
import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.dto.AuthTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/23
 */
@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {
    //令牌请求的端点
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;
    
    @PostMapping(value = "/token")
    public CommonResult getToken(@Valid @RequestBody AuthTokenRequest request) throws HttpRequestMethodNotSupportedException {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", request.getClientId());
        params.put("client_secret", request.getClientSecret());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getClientId(),
                        request.getClientSecret(), new ArrayList<>());
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(authenticationToken, params).getBody();
        return CommonResult.success(oAuth2AccessToken);
    }

    @PostMapping(value = "/check_token")
    public CommonResult<Map<String, ?>> checkToken(@RequestParam("token") String value) {
        Map<String, ?> map = checkTokenEndpoint.checkToken(value);
        return CommonResult.success(map);
    }

    @GetMapping("/token_key")
    public CommonResult getKey(Principal principal) throws IOException {
        //第1部：授权服务-生成公私钥
        AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
        ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
        //第2部：授权服务-暴露公钥
        byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
        String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);
        return CommonResult.success(pubKeyX509Pem);
    }

//    @GetMapping("/.well-known/jwks.json")
//    @ResponseBody
//    public Map<String, Object> getKey() {
//        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
//        RSAKey key = new RSAKey.Builder(publicKey).build();
//        return new JWKSet(key).toJSONObject();
//    }
}
