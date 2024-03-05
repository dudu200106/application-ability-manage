package com.dsj.csp.manage.dto.gateway;

import lombok.Data;

/**
 * 功能说明：app处理VO
 *
 * @author 蔡云
 * 2024/2/25
 */
@Data
public class AppHandleVO {

    private String appId;
    private String appExtId;
    private String appName;
    private String appStatus;
    private String appStatusDesc;

    private String appPrivateKey;
    private String appPublicKey;
    private String serverPrivateKey;
    private String serverPublicKey;

    private String handleOperate;
    private String handleResult;
    private String handleMessage;

}
