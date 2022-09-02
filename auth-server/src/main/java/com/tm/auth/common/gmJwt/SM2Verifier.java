package com.tm.auth.common.gmJwt;

import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.common.utils.JsonUtil;
import com.tm.auth.common.utils.SMUtils;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * SM2算法的JWT验签逻辑
 *
 * @author tangming
 * @date 2022/8/22
 */
public class SM2Verifier implements SignatureVerifier {
    private final ECPublicKeyParameters key;
    private final String algorithm;
    public static final String TOKEN_EXP = "exp";

    public SM2Verifier(String publicKeyPem) {
        this.key = SMUtils.convertToBCECPublicKey(publicKeyPem);
        this.algorithm = "SM3withSM2";
    }

    public SM2Verifier(ECPublicKeyParameters key) {
        this.key = key;
        this.algorithm = "SM3withSM2";
    }

    public SM2Verifier(ECPublicKeyParameters key, String algorithm) {
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
