package com.tm.auth.common.gm;

import org.springframework.security.jwt.crypto.sign.Signer;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;

/**
 * @author tangming
 * @date 2022/8/19
 */
public class Sm2Signer implements Signer {
    static final String DEFAULT_ALGORITHM = "SM2";
    private final RSAPrivateKey key;
    private final String algorithm;

    public Sm2Signer(BigInteger n, BigInteger d) {
        this(createPrivateKey(n, d));
    }

    public Sm2Signer(RSAPrivateKey key) {
        this(key, "SHA256withRSA");
    }

    public Sm2Signer(RSAPrivateKey key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    public Sm2Signer(String sshKey) {
        this(loadPrivateKey(sshKey));
    }

    public byte[] sign(byte[] bytes) {
        try {
            Signature signature = Signature.getInstance(this.algorithm);
            signature.initSign(this.key);
            signature.update(bytes);
            return signature.sign();
        } catch (GeneralSecurityException var3) {
            throw new RuntimeException(var3);
        }
    }

    public String algorithm() {
        return this.algorithm;
    }

    private static RSAPrivateKey createPrivateKey(BigInteger n, BigInteger d) {
        try {
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(n, d));
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    private static RSAPrivateKey loadPrivateKey(String key) {
        KeyPair kp = SM2Helper.parseKeyPair(key);
        if (kp.getPrivate() == null) {
            throw new IllegalArgumentException("Not a private key");
        } else {
            return (RSAPrivateKey)kp.getPrivate();
        }
    }
}