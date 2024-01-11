package com.dsj.csp.manage.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/10 0010 13:52
 * @Todo:
 */
public class SM4EncryptionExample {


    public static String generateApiKey(int keyLength) {
        // 定义可能包含在API密钥中的字符集
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // 使用安全的伪随机数生成器生成随机字节数组
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);

        // 将字节数组转换为字符串
        StringBuilder apiKeyBuilder = new StringBuilder(keyLength);
        for (byte b : bytes) {
            apiKeyBuilder.append(characters.charAt(Math.abs(b) % characters.length()));
        }

        // Base64编码以确保生成的密钥是可用的ASCII字符
//        return Base64.getEncoder().encodeToString(apiKeyBuilder.toString().getBytes());
        String key= String.valueOf(apiKeyBuilder.toString().getBytes());
        return key;
    }

    public static void main(String[] args) {
        // 指定API密钥的长度
        SymmetricCrypto sm4 = SmUtil.sm4();
        String setKey = sm4.encryptHex(SnowflakeIdGenerator.createId().substring(7, 18));
        int apiKeyLength = 24;
        // 生成API密钥
        String apiKey  = sm4.encryptHex(generateApiKey(apiKeyLength));

        System.out.println(setKey);
        System.out.println("Generated API Key: " + apiKey);
    }
}
