package com.dsj.csp.manage.dto.gateway;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@Data
public class CryptJsonBody {

    private String contentEncrypt;

    /**
     * 将 contentCrypt 解密，还原出原本的请求响应体内容
     */
    public static String decryptToStr(CryptJsonBody cryptJsonBody, String privateKey) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        return sm2.decryptStr(cryptJsonBody.getContentEncrypt(), KeyType.PrivateKey);
    }

    /**
     * 将 contentCrypt 解密，还原出原本的请求响应体内容，返回JSONObject对象
     */
    public static JSONObject decryptToJSON(CryptJsonBody cryptJsonBody, String privateKey) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String decryptStr = sm2.decryptStr(cryptJsonBody.getContentEncrypt(), KeyType.PrivateKey);
        return new JSONObject(decryptStr);
    }

    /**
     * 将 contentCrypt 解密，还原出原本的请求响应体内容，返回 T 对象
     */
    public static <T> T decryptToObj(CryptJsonBody cryptJsonBody, String privateKey, Class<T> clazz) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String decryptStr = sm2.decryptStr(cryptJsonBody.getContentEncrypt(), KeyType.PrivateKey);
        return new JSONObject(decryptStr).toBean(clazz);
    }

    /**
     * 将 contentCrypt 解密，返回List
     */
    public static <T> List<T> decryptToList(CryptJsonBody cryptJsonBody, String lable, String privateKey, Class<T> clazz) {
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        String decryptStr = sm2.decryptStr(cryptJsonBody.getContentEncrypt(), KeyType.PrivateKey);
        return JSONUtil.toList(new JSONObject(decryptStr).getJSONArray(lable), clazz);
    }


    /**
     * 将对象加密为 contentCrypt
     */
    public static CryptJsonBody encryptObj(Object object, String publicKey) {
        SM2 sm2 = SmUtil.sm2(null, publicKey);
        String jsonStr = JSONUtil.toJsonStr(object);
        String encryptStr = sm2.encryptBase64(jsonStr, KeyType.PublicKey);
        CryptJsonBody cryptJsonBody = new CryptJsonBody();
        cryptJsonBody.setContentEncrypt(encryptStr);
        return cryptJsonBody;
    }
}
