package com.dsj.csp.manage.dto.gateway;

import lombok.Data;

/**
 * 功能说明：api处理VO
 *
 * @author 蔡云
 * 2024/2/25
 */
@Data
public class ApiHandleVO {

    private String apiId;
    private String apiExtId;
    private String apiName;
    private String apiStatus;
    private String apiStatusDesc;

    private String handleOperate;
    private String handleResult;
    private String handleMessage;
}
