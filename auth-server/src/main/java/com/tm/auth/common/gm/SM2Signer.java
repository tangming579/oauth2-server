package com.tm.auth.common.gm;

import com.tm.auth.common.gmUtils.SM2Util;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.springframework.security.jwt.crypto.sign.Signer;

/**
 * SM2算法的JWT签名逻辑
 *
 * @author tangming
 * @date 2022/8/22
 */
public class SM2Signer implements Signer {
    static final String DEFAULT_ALGORITHM = "SM3withSM2";
    private final BCECPrivateKey privateKey;
    private final String algorithm;

    public SM2Signer(BCECPrivateKey privateKey) {
        this.privateKey = privateKey;
        this.algorithm = DEFAULT_ALGORITHM;
        if (privateKey == null) {
            throw new IllegalArgumentException("The Key Provider cannot be null.");
        }
    }

    @Override
    public byte[] sign(byte[] bytes) {
        byte[] signatureByte;
        try {
            signatureByte = SM2Util.sign(privateKey, bytes);
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
        return signatureByte;
    }

    @Override
    public String algorithm() {
        return this.algorithm;
    }
}
