package com.tm.auth.service;

import com.nimbusds.jwt.util.DateUtils;
import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.common.api.ResultCode;
import com.tm.auth.common.gmJwt.SM2JwtImpl;
import com.tm.auth.common.gmJwt.SM2Signer;
import com.tm.auth.common.gmJwt.SM2Verifier;
import com.tm.auth.common.utils.JsonUtil;
import com.tm.auth.common.utils.SMUtils;
import com.tm.auth.config.OAuthProperties;
import com.tm.auth.dto.PublicKeyDto;
import com.tm.auth.mbg.mapper.OauthClientKeypairMapper;
import com.tm.auth.mbg.model.OauthClientKeypair;
import com.tm.auth.mbg.model.OauthClientKeypairExample;
import com.tm.auth.pojo.Authority;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.codec.Codecs;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.CharBuffer;
import java.security.Security;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tangming
 * @date 2022/8/25
 */
@Service
@Slf4j
public class OAuthJwtService {
    private static final byte JWT_PART_SEPARATOR = (byte) '.';
    private static final String TOKEN_EXP = "exp";
    //权限验证方式，0 解析token中authorities验证； 1调用授权服务接口在线验证
    private static final String TOKEN_AUT = "autype";
    private static final String TOKEN_TYP = "JWT_PAAS";
    private static final String TOKEN_ALG = "SM3withSM2";
    private static final String TOKEN_CLIENT_ID = "client_id";
    private static final String TOKEN_AUTH = "authorities";
    private static final byte[] TOKEN_HEADER;
    private static final int TOKEN_MAX_CLOCK_SKEW = 60;
    @Autowired
    private OauthClientKeypairMapper oauthClientKeypairMapper;
    @Autowired
    private OAuthProperties oAuthProperties;

    static {
        TOKEN_HEADER = createJwtHeader().getBytes();
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成SM2非对称加密秘钥对并保存
     *
     * @return
     */
    public Integer generateKeyPair(String clientId) {
        //为服务生成公私钥
        Map.Entry<String, String> keyPair = SMUtils.generateSM2Key();
        OauthClientKeypair keypair = new OauthClientKeypair();
        keypair.setClientId(clientId);
        keypair.setPrivateKey(keyPair.getKey());
        keypair.setPublicKey(keyPair.getValue());
        return oauthClientKeypairMapper.insert(keypair);
    }

    public int deleteJwtKeypair(String clientId) {
        return oauthClientKeypairMapper.deleteByPrimaryKey(clientId);
    }

    public OauthClientKeypair getJwtKeypair(String clientId) {
        OauthClientKeypair keypair = oauthClientKeypairMapper.selectByPrimaryKey(clientId);
        if (keypair == null) {
            String msg = String.format("未获取到 clientId: %s 的token秘钥信息", clientId);
            log.error(msg);
            throw new OAuthExecption(msg);
        }
        return keypair;
    }

    public String getJwtPrivateKey(String clientId) {
        return getJwtKeypair(clientId).getPrivateKey();
    }

    public String getJwtPublicKey(String clientId) {
        return getJwtKeypair(clientId).getPublicKey();
    }

    public List<PublicKeyDto> getJwtPublicKey(List<String> clientIds) {
        OauthClientKeypairExample example = new OauthClientKeypairExample();
        if (!CollectionUtils.isEmpty(clientIds)) {
            example.createCriteria().andClientIdIn(clientIds);
        }
        return oauthClientKeypairMapper.selectByExample(example).stream()
                .map(x -> {
                    PublicKeyDto info = new PublicKeyDto();
                    info.setPublicKey(x.getPublicKey());
                    info.setClientId(x.getClientId());
                    return info;
                }).collect(Collectors.toList());
    }

    /**
     * 解析token并签名校验
     *
     * @param token
     * @return
     */
    public Map<String, Object> decodeJwtStrAndVerify(String token) {
        Jwt jwt = decodeJwtStr(token);
        String claimsStr = jwt.getClaims();
        Map<String, Object> claims = JsonUtil.parseMap(claimsStr);
        String clientId = claims.get(TOKEN_CLIENT_ID).toString();
        String publicKey = getJwtPublicKey(clientId);
        SM2Verifier verifier = new SM2Verifier(publicKey);
        jwt.verifySignature(verifier);
        if (claims.containsKey(TOKEN_EXP) && claims.get(TOKEN_EXP) instanceof Integer) {
            Integer intValue = (Integer) claims.get(TOKEN_EXP);
            claims.put(TOKEN_EXP, (long) intValue);
            Date now = new Date();
            Date exp = DateUtils.fromSecondsSinceEpoch(((Number) intValue).longValue());
            if (!DateUtils.isAfter(exp, now, TOKEN_MAX_CLOCK_SKEW)) {
                throw new OAuthExecption(ResultCode.TOKEN_EXPIRED.getMessage(), (int) ResultCode.TOKEN_EXPIRED.getCode());
            }
        }
        return claims;
    }

    public Jwt decodeJwtStr(String token) {
        int firstPeriod = token.indexOf(JWT_PART_SEPARATOR);
        int lastPeriod = token.lastIndexOf(JWT_PART_SEPARATOR);
        if (firstPeriod > 0 && lastPeriod > firstPeriod) {
            CharBuffer buffer = CharBuffer.wrap(token, 0, firstPeriod);
            byte[] header = Codecs.b64UrlDecode(buffer);
            buffer.limit(lastPeriod).position(firstPeriod + 1);
            byte[] payload = Codecs.b64UrlDecode(buffer);
            buffer.limit(token.length()).position(lastPeriod + 1);
            byte[] signature = Codecs.b64UrlDecode(buffer);
            return new SM2JwtImpl(header, payload, signature);
        } else {
            throw new IllegalArgumentException("JWT must have 3 tokens");
        }
    }

    public String encodePayloadToJwtStr(String content) {
        String claimsStr = new String(Codecs.utf8Encode(content));
        Map<String, Object> claims = JsonUtil.parseMap(claimsStr);
        String clientId = claims.get(TOKEN_CLIENT_ID).toString();
        String privateKey = getJwtPrivateKey(clientId);
        SM2Signer signer = new SM2Signer(privateKey);
        return encodePayloadToJwtStr(content, signer);
    }

    /**
     * 将payload字符串增加header和sign后转为jwt字符串
     *
     * @param content payload字符串
     * @param signer
     * @return
     */
    public String encodePayloadToJwtStr(String content, Signer signer) {
        byte[] claims = Codecs.utf8Encode(content);
        byte[] headerBytes = TOKEN_HEADER;

        claims = convertAuthStrToJson(claims);

        String headerBase64 = Base64.encodeBase64URLSafeString(headerBytes);
        String payloadBase64 = Base64.encodeBase64URLSafeString(claims);

        byte[] combineSignByte = combineSignByte(headerBase64.getBytes(), payloadBase64.getBytes());
        byte[] signatureBytes = signer.sign(combineSignByte);

        String signatureBase64 = Base64.encodeBase64URLSafeString(signatureBytes);
        return String.format("%s.%s.%s", headerBase64, payloadBase64, signatureBase64);
    }

    /**
     * 拼接签名部分 header + . + payload
     *
     * @param headerBytes  header
     * @param payloadBytes payload
     * @return bytes
     */
    private byte[] combineSignByte(byte[] headerBytes, byte[] payloadBytes) {
        // header + payload
        byte[] hash = new byte[headerBytes.length + payloadBytes.length + 1];
        System.arraycopy(headerBytes, 0, hash, 0, headerBytes.length);
        hash[headerBytes.length] = JWT_PART_SEPARATOR;
        System.arraycopy(payloadBytes, 0, hash, headerBytes.length + 1, payloadBytes.length);
        return hash;
    }

    private static String createJwtHeader() {
        Map<String, String> map = new LinkedHashMap();
        map.put("alg", TOKEN_ALG);
        map.put("typ", TOKEN_TYP);
        return JsonUtil.toJsonString(map);
    }

    /**
     * 框架默认 authorities 的内容是字符串数组，需要改成项目中的json数组
     *
     * @param claims
     * @return
     */
    private byte[] convertAuthStrToJson(byte[] claims) {
        Map<String, Object> node = JsonUtil.parseMap(new String(claims));
        node.put(TOKEN_AUT, 0);
        Object objAuth = node.get(TOKEN_AUTH);
        if (Objects.isNull(objAuth)) return claims;
        List<String> au = (List<String>) objAuth;
        if (CollectionUtils.isEmpty(au)) return claims;
        List<Authority> authorities = new ArrayList<>();
        for (String auth : au) {
            Authority authority = JsonUtil.parseObject(auth, Authority.class);
            authorities.add(authority);
        }
        int authoritiesLength = JsonUtil.toJsonString(authorities).length();
        if (authoritiesLength <= oAuthProperties.tokenAuthoritiesMaxLength) {
            node.put(TOKEN_AUTH, authorities);
            node.put(TOKEN_AUT, 0);
        } else {
            node.put(TOKEN_AUTH, null);
            node.put(TOKEN_AUT, 1);
        }
        claims = JsonUtil.toJsonString(node).getBytes();
        return claims;
    }
}
