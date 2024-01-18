package com.dsj.csp.manage.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/17 17:09
 * @Todo:
 */
public class Sm2 {

    public static Map<String, String> sm2Test() {
        Map<String,String>map=new HashMap<>();
        //使用自定义密钥对加密或解密
        System.out.println("使用自定义密钥对加密或解密====开始");
        KeyPair pair = SecureUtil.generateKeyPair("SM2");

        String text = "gxyyzcgeneratekey";


        byte[] privateKey = pair.getPrivate().getEncoded();
        byte[] publicKey = pair.getPublic().getEncoded();
        String privateEncode = Base64.encode(privateKey);
        String publicEncode = Base64.encode(publicKey);
        System.out.println("私钥：" + privateEncode);
        System.out.println("公钥：" + publicEncode);
        map.put("publicEncode",publicEncode);
        map.put("privateEncode", privateEncode);

        SM2 sm22 = SmUtil.sm2(privateKey, publicKey);
        // 公钥加密
        String encryptStr2 = sm22.encryptBcd(text, KeyType.PublicKey);
        System.out.println("公钥加密：" + encryptStr2);
        //私钥解密
        String decryptStr2 = StrUtil.utf8Str(sm22.decryptFromBcd(encryptStr2, KeyType.PrivateKey));
        System.out.println("私钥解密：" + decryptStr2);
        System.out.println("使用自定义密钥对加密或解密====结束");
        return map;
    }

    public static void main(String[] args) {
        System.out.println(sm2Test());
    }

}
