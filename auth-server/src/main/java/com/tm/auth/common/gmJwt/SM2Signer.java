package com.tm.auth.common.gmJwt;

import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.common.utils.SMUtils;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
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
    private final ECPrivateKeyParameters privateKey;
    private final String algorithm;

    public SM2Signer(String privateKeyEncry) {
        this.algorithm = DEFAULT_ALGORITHM;
        privateKey = SMUtils.convertToBCECPrivateKey(privateKeyEncry);
    }

    public SM2Signer(ECPrivateKeyParameters privateKey) {
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
