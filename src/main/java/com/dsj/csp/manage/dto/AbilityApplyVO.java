package com.dsj.csp.manage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 能力申请试用VO
 *
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/11
 */
@Data
public class AbilityApplyVO implements Serializable {

    private Long abilityId;

    private Long appId;

    private Long userId;

    private String abilityName;

    private String abilityType;

    private String appName;

    // 申请使用的接口列表, 逗号隔开
    private String apiIds;

    private String companyName;

    private String govName;

    // 申请说明
    private String illustrate;

}
