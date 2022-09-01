package com.tm.auth.common.utils;

import com.tm.auth.common.api.OAuthExecption;
import com.tm.auth.common.gmUtils.BCECUtil;
import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.common.gmUtils.SM3Util;
import com.tm.auth.common.gmUtils.SM4Util;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author tangming
 * @date 2022/8/26
 */
public class SMUtils {
    private static final String EPIDEMIC_KEY = "8641F1CF0F312C7DE0BA269C1C47394C";
    private static final byte[] KEY;
    private static final String ENCODING = "UTF-8";

    static {
        KEY = ByteUtils.fromHexString(EPIDEMIC_KEY);
    }

    public static String sm4Encrypt(String paramStr) {
        try {
            if (StringUtils.hasText(paramStr)) {
                byte[] srcData = paramStr.getBytes(ENCODING);
                byte[] cipherText = SM4Util.encrypt_ECB_Padding(KEY, srcData);
                String cipherTextStr = ByteUtils.toHexString(cipherText);
                return cipherTextStr;
            } else {
                return paramStr;
            }
        } catch (Exception e) {
            throw new RuntimeException("sm4Encrypt error " + e.getMessage());
        }
    }

    public static String sm4Decrypt(String cipherText) {
        try {
            if (StringUtils.hasText(cipherText)) {
                byte[] cipherData = ByteUtils.fromHexString(cipherText);
                byte[] decryptedData = SM4Util.decrypt_ECB_Padding(KEY, cipherData);
                String decryptStr = new String(decryptedData);
                return decryptStr;
            } else {
                return cipherText;
            }
        } catch (Exception e) {
            throw new RuntimeException("sm4Decrypt error", e);
        }
    }

    /**
     * 生成国密秘钥对
     * 私钥为私钥对象转16进制字符串后再SM4加密
     * 公钥为Pem明文
     *
     * @return
     */
    public static Map.Entry<String, String> generateSM2Key() {
        try {
            //生成公私钥
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
            //对私钥使用SM4加密处理
            String privateKeyHex = ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase();
            String privateEncry = sm4Encrypt(privateKeyHex);
            //公钥转为pem格式
            byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
            String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);

            return new AbstractMap.SimpleEntry<>(privateEncry, pubKeyX509Pem);
        } catch (Exception e) {
            throw new RuntimeException("generateSM2KeyPair error", e);
        }
    }

    public static ECPrivateKeyParameters convertToBCECPrivateKey(String privateKeyEncry) {
        String privateKeyHexR = sm4Decrypt(privateKeyEncry);
        ECPrivateKeyParameters privateKey = new ECPrivateKeyParameters(
                new BigInteger(ByteUtils.fromHexString(privateKeyHexR)), SM2Util.DOMAIN_PARAMS);
        return privateKey;
    }

    public static ECPublicKeyParameters convertToBCECPublicKey(String pubKeyX509Pem) {
        try {
            byte[] rs_pubKeyX509Der = BCECUtil.convertECPublicKeyPEMToX509(pubKeyX509Pem);
            ECPublicKeyParameters publicKey = BCECUtil.convertPublicKeyToParameters(
                    BCECUtil.convertX509ToECPublicKey(rs_pubKeyX509Der));
            return publicKey;
        } catch (Exception e) {
            throw new OAuthExecption("convertToBCECPublicKey error " + pubKeyX509Pem);
        }
    }

    public static byte[] sm3Hash(byte[] srcData) {
        return SM3Util.hash(srcData);
    }

    public static String sm3HashHex(byte[] srcData) {
        byte[] hash = SM3Util.hash(srcData);
        return ByteUtils.toHexString(hash);
    }

    public static boolean sm3Verify(byte[] srcData, byte[] sm3Hash) {
        return SM3Util.verify(srcData, sm3Hash);
    }

    public static boolean sm3Verify(String srcData, String sm3Hash) {
        byte[] srcDataHash = ByteUtils.fromHexString(srcData);
        byte[] sm3HashByte = ByteUtils.fromHexString(sm3Hash);
        return SM3Util.verify(srcDataHash, sm3HashByte);
    }
}
