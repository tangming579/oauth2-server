package com.tm.auth.common.gm;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

/**
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

    @Override
    public void verify(byte[] bytes, byte[] bytes1) {

    }

    @Override
    public String algorithm() {
        return algorithm;
    }
}
