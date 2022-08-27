package com.tm.auth.api;

import com.tm.auth.common.api.CommonResult;
import com.tm.auth.common.gmUtils.BCECUtil;
import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.dto.AuthTokenRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/23
 */
@RestController
@RequestMapping("/oauth")
@Api("授权接口")
@Slf4j
public class OAuthController {
    //令牌请求的端点
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private CheckTokenEndpoint checkTokenEndpoint;

//    @ApiOperation("应用获取令牌")
//    @PostMapping(value = "/token")
//    public CommonResult getToken(@Valid @RequestBody AuthTokenRequest request) throws HttpRequestMethodNotSupportedException {
//        Map<String, String> params = new HashMap<>();
//        params.put("grant_type", "client_credentials");
//        params.put("client_id", request.getClientId());
//        params.put("client_secret", request.getClientSecret());
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(request.getClientId(),
//                        request.getClientSecret(), new ArrayList<>());
//        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(authenticationToken, params).getBody();
//        return CommonResult.success(oAuth2AccessToken);
//    }

    @ApiOperation("校验令牌")
    @PostMapping(value = "/check_token")
    public CommonResult checkToken(@RequestParam("token") String value) {
        Map<String, ?> map = checkTokenEndpoint.checkToken(value);
        return CommonResult.success(map);
    }

    @ApiOperation("获取某个资源公钥")
    @GetMapping("/token_key")
    public CommonResult getKey() throws IOException {
        //第1部：授权服务-生成公私钥
        AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
        ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
        //第2部：授权服务-暴露公钥
        byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
        String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);
        return CommonResult.success(pubKeyX509Pem);
    }

    @ApiOperation("获取所有资源公钥")
    @PostMapping("/token_key_all")
    public CommonResult getKeyAll() throws IOException {
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
