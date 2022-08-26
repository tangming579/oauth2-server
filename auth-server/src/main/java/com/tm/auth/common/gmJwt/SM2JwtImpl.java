package com.tm.auth.common.gmJwt;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.codec.Codecs;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

/**
 * @author tangming
 * @date 2022/8/23
 */
public class SM2JwtImpl implements Jwt {
    static byte[] PERIOD = Codecs.utf8Encode(".");

    private final byte[] header;
    private final byte[] payload;
    private final byte[] signature;
    private final String claims;

    public SM2JwtImpl(byte[] header, byte[] payload, byte[] signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
        this.claims = Codecs.utf8Decode(payload);
    }

    @Override
    public String getClaims() {
        return claims;
    }

    @Override
    public String getEncoded() {
        return Codecs.utf8Decode(this.bytes());
    }

    @Override
    public void verifySignature(SignatureVerifier signatureVerifier) {
        signatureVerifier.verify(content(), signature);
    }

    @Override
    public byte[] bytes() {
        return Codecs.concat(content(), PERIOD, Codecs.b64UrlEncode(this.signature));
    }

    public byte[] content() {
        return Codecs.concat(new byte[][]{Codecs.b64UrlEncode(this.header), PERIOD, Codecs.b64UrlEncode(this.payload)});
    }
}
