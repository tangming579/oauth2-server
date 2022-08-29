package com.tm.auth.common.gmJwt;

import com.tm.auth.common.gmUtils.SM2Util;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

/**
 * SM2算法的JWT验签逻辑
 *
 * @author tangming
 * @date 2022/8/22
 */
public class SM2Verifier implements SignatureVerifier {
    private final BCECPublicKey key;
    private final String algorithm;

    public SM2Verifier(BCECPublicKey key) {
        this.key = key;
        this.algorithm = "SM3withSM2";
    }

    public SM2Verifier(BCECPublicKey key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    /**
     * 校验签名
     *
     * @param content 内容 base64(header)+base64(payload)
     * @param sig     签名
     */
    @Override
    public void verify(byte[] content, byte[] sig) {
        try {
            boolean verifyResult = SM2Util.verify(key, content, sig);
            if (!verifyResult) {
                throw new InvalidSignatureException("SM2 Signature did not match content");
            }
        } catch (Exception e) {
            throw new InvalidSignatureException("SM2 Signature did not match content");
        }
    }

    @Override
    public String algorithm() {
        return algorithm;
    }
}
