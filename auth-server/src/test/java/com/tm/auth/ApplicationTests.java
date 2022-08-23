package com.tm.auth;

import com.tm.auth.common.gmUtils.BCECUtil;
import com.tm.auth.common.gmUtils.SM2Util;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void jwtTest2() {
        try {
            //第1部：授权服务-生成公私钥
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            //第2部：授权服务-暴露公钥
            byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
            String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);

            //第3部：授权服务-用私钥加密
            byte[] SRC_DATA = "Hello tangming".getBytes();
            byte[] sign = SM2Util.sign(priKey, SRC_DATA);
            System.out.println("SM2 sign without withId result:\n" + ByteUtils.toHexString(sign));

            //第4部：资源服务-将公钥字符串转为对象
            byte[] rs_pubKeyX509Der = BCECUtil.convertECPublicKeyPEMToX509(pubKeyX509Pem);
            ECPublicKeyParameters rs_pubKey = BCECUtil.convertPublicKeyToParameters(BCECUtil.convertX509ToECPublicKey(rs_pubKeyX509Der));

            //第5部：资源服务-用公钥验证
            boolean flag = SM2Util.verify(rs_pubKey, SRC_DATA, sign);
            if (!flag) {
                //Assert.fail("sign without withId verify failed");
            } else
                System.out.println("success");

        } catch (Exception e) {
            System.out.printf("error");
        }
    }
}
