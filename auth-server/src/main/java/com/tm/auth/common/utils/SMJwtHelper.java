package com.tm.auth.common.utils;

import com.tm.auth.common.gmJwt.SM2JwtImpl;
import com.tm.auth.mbg.model.OauthAuthority;
import com.tm.auth.pojo.Authority;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.jwt.*;
import org.springframework.security.jwt.codec.Codecs;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

import java.nio.CharBuffer;
import java.util.*;

/**
 * @author tangming
 * @date 2022/8/22
 */
public class SMJwtHelper {
    private static final byte JWT_PART_SEPARATOR = (byte) 46;

    /**
     * 解码token并签名校验
     *
     * @param token
     * @param verifier
     * @return
     */
    public static Jwt decodeAndVerify(String token, SignatureVerifier verifier) {
        Jwt jwt = decode(token);
        jwt.verifySignature(verifier);
        return jwt;
    }

    public static Jwt decode(String token) {
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

    /**
     * 将编码为jwt
     *
     * @param content
     * @param signer
     * @return
     */
    public static String encode(CharSequence content, Signer signer) {
        byte[] claims = Codecs.utf8Encode(content);
        byte[] headerBytes = createHeader().getBytes();

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
    private static byte[] combineSignByte(byte[] headerBytes, byte[] payloadBytes) {
        // header + payload
        byte[] hash = new byte[headerBytes.length + payloadBytes.length + 1];
        System.arraycopy(headerBytes, 0, hash, 0, headerBytes.length);
        hash[headerBytes.length] = JWT_PART_SEPARATOR;
        System.arraycopy(payloadBytes, 0, hash, headerBytes.length + 1, payloadBytes.length);
        return hash;
    }

    private static String createHeader() {
        Map<String, String> map = new LinkedHashMap();
        map.put("alg", "SM3withSM2");
        map.put("typ", "JWT_PAAS");
        return JsonUtil.toJsonString(map);
    }

    private static byte[] convertAuthStrToJson(byte[] claims) {
        Map<String, Object> node = JsonUtil.parseMap(new String(claims));
        List<String> au = (List<String>) node.get("authorities");
        if (Objects.isNull(au)) return claims;
        List<Authority> authorities = new ArrayList<>();
        for (String auth : au) {
            Authority authority = JsonUtil.parseObject(auth, Authority.class);
            authorities.add(authority);
        }
        node.put("authorities", authorities);
        claims = JsonUtil.toJsonString(node).getBytes();
        return claims;
    }
}
