package com.dsj.csp.manage.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/9 0009 15:45
 * @Todo: 加密SM4算法
 */
public class Sm4 {
private  static String sm(){
    SymmetricCrypto sm4 = SmUtil.sm4();
    String substring = SnowflakeIdGenerator.createId().substring(0,7);
    String acesKey = sm4.encryptHex(substring);
    String setKey = sm4.encryptHex(SnowflakeIdGenerator.createId().substring(7, 18));
    System.out.println(setKey);
    System.out.println(acesKey);
    return acesKey;
}

}
