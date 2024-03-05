package com.dsj.csp.common.consts;

/**
 * 用于与网关进行加解密通讯的密钥
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
public interface GatewayCryptKeyConst {
    /**
     * 客户端私钥 用于对网关的响应JSON数据进行解密
     */
    String CLIENT_PRIVATE = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgY76nZ6wJHbHfea5gof/+HYTfA5wJY1KXlhz2dxDN1O6gCgYIKoEcz1UBgi2hRANCAATUv0wbU4ml/mXD1QBeHELdMPNtD0xr397OPEWmlV7aGhhMBeEKo7agZXxhCx/K9lzUCBdDmETs2mNuGEEVRTbB";

    /**
     * 客户端公钥
     */
    String CLIENT_PUBLIC = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE1L9MG1OJpf5lw9UAXhxC3TDzbQ9Ma9/ezjxFppVe2hoYTAXhCqO2oGV8YQsfyvZc1AgXQ5hE7NpjbhhBFUU2wQ==";

    /**
     * 服务端私钥
     */
    String SERVER_PRIVATE = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgaEZNpYBlDfcQdRN/MjWn3Y105awE4KqgViV7Cim2HNqgCgYIKoEcz1UBgi2hRANCAARhrwmCZS5JG6HHCNJ32Nv+gsN1hzov3vmTzvGR5Vmdn28MQIGDdkHUhgRS02LqU48qjD68uj260s0n+f/fO2Sr";

    /**
     * 服务端公钥 用于对网关的请求数据进行加密
     */
    String SERVER_PUBLIC = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEYa8JgmUuSRuhxwjSd9jb/oLDdYc6L975k87xkeVZnZ9vDECBg3ZB1IYEUtNi6lOPKow+vLo9utLNJ/n/3ztkqw==";

}
