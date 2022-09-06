package com.tm.auth;

import com.google.common.collect.Lists;
import com.tm.auth.common.gmUtils.BCECUtil;
import com.tm.auth.common.gmUtils.SM2Util;
import com.tm.auth.common.gmUtils.SM4Util;
import lombok.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@SpringBootTest
class ApplicationTests {
    @Getter
    @Setter
    @ToString
    public class ExportTemperatureDto {
        private String name;
        private Double morningTemperature;
        private Double afternoonTemperature;
        private String classId;
        private String gradeId;
        private Integer personId;
    }

    @Test
    void contextLoads() {
        List<ExportTemperatureDto> temperatureList = Lists.newArrayList();
        temperatureList.add(new ExportTemperatureDto());
        temperatureList.add(new ExportTemperatureDto());
        temperatureList.add(new ExportTemperatureDto());
        temperatureList.add(new ExportTemperatureDto());

        temperatureList.add(new ExportTemperatureDto());
        temperatureList.add(new ExportTemperatureDto());

        List<ExportTemperatureDto> result = temperatureList.stream()
                .collect(
                        collectingAndThen(
                                toCollection(
                                        () -> new TreeSet<>(comparing(ExportTemperatureDto::getPersonId))
                                ),
                                ArrayList::new
                        )
                );

        result.forEach(System.out::println);

        /*
            输出结果为：
                personId为1的，其名称为haha
                personId为2的，其名称为haha
            因为TreeSet底层是使用TreeMap进行实现的，传入了根据getPersonId进行比较的比较器
            在判断personId相同时，其比较结果为0，然后就会替换其value值，而key值是不会变化的，
            又因为TreeSet是将传入的元素作为key的，所以使用TreeSet时，当比较器比较的结果相同时，以不会将原来的值替换成比较后的值
         */

    }

    private static final String EPIDEMIC_KEY = "8641F1CF0F312C7DE0BA269C1C47394C";

    @Test
    void jwtTest2() {
        try {
            //授权服务-生成公私钥
            AsymmetricCipherKeyPair keyPair = SM2Util.generateKeyPairParameter();
            ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

            //将私钥转成字符串
            String privateKeyHex = ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase();
            byte[] key = ByteUtils.fromHexString(EPIDEMIC_KEY);
            byte[] cipherText = SM4Util.encrypt_ECB_Padding(key, privateKeyHex.getBytes());
            String cipherTextStr = ByteUtils.toHexString(cipherText);
            byte[] decryptedData = SM4Util.decrypt_ECB_Padding(key, cipherText);
            String privateKeyHexR = new String(decryptedData);
            ECPrivateKeyParameters priKeyRecovery = new ECPrivateKeyParameters(new BigInteger(ByteUtils.fromHexString(privateKeyHexR)), SM2Util.DOMAIN_PARAMS);

            //授权服务-暴露公钥
            byte[] pubKeyX509Der = BCECUtil.convertECPublicKeyToX509(pubKey);
            String publicKeyHex = ByteUtils.toHexString(pubKeyX509Der).toUpperCase();
            String pubKeyX509Pem = BCECUtil.convertECPublicKeyX509ToPEM(pubKeyX509Der);

            //授权服务-用私钥加密
            byte[] SRC_DATA = "Hello tangming".getBytes();
            byte[] sign = SM2Util.sign(priKey, SRC_DATA);
            System.out.println("SM2 sign without withId result:\n" + ByteUtils.toHexString(sign));

            //资源服务-将公钥字符串转为对象
            byte[] rs_pubKeyX509Der = BCECUtil.convertECPublicKeyPEMToX509(pubKeyX509Pem);
            ECPublicKeyParameters rs_pubKey = BCECUtil.convertPublicKeyToParameters(BCECUtil.convertX509ToECPublicKey(rs_pubKeyX509Der));

            //资源服务-用公钥验证
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
