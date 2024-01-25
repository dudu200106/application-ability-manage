package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Date;
import java.util.List;

/**
 * 能力申请DTO
 *
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/11
 */
@Data
@Schema(description = "能力申请VO")
public class AbilityApplyDTO {

    @Schema(description = "能力申请ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityApplyId;

    @Schema(description = "能力ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "应用ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    @Schema(description = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @Schema(description = "能力名称")
    private String abilityName;

    @Schema(description = "能力类型")
    private String abilityType;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "申请使用的接口列表id")
    private String apiIds;

    @Schema(description = "申请使用的接口列表")
    private List<AbilityApiEntity> apiList;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "政府名称")
    private String govName;

    @Schema(description = "申请说明")
    private String illustrate;

    @Schema(description="状态")
    private Integer status;

    @Schema(description="备注")
    private String note;

    @Schema(description="审批时间")
    private Date approveTime;

    @Schema(description="更新时间")
    private Date updateTime;

    @Schema(description="创建时间")
    private Date createTime;


}
