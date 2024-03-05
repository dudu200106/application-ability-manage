package com.dsj.csp.manage.dto.gateway;

import lombok.Data;

/**
 * 功能说明：app处理VO
 *
 * @author 蔡云
 * 2024/2/25
 */
@Data
public class ApplyHandleVO {

    AppHandleVO appHandleVO;
    ApiHandleVO apiHandleVO;


    private String applyId;
    private String applyExtId;
    private String applyStatus;
    private String applyStatusDesc;

    private String handleOperate;
    private String handleResult;
    private String handleMessage;

}
