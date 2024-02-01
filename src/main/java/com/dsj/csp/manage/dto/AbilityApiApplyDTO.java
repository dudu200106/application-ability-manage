package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.entity.AbilityApiResp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
public class AbilityApiApplyDTO {

    @Schema(description = "接口申请ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiApplyId;

    @Schema(description="接口ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @Schema(description = "应用ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    @Schema(description = "能力ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @Schema(description="接口名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Length(max= 30,message="编码长度不能超过30")
    private String apiName;

    @Schema(description="接口描述")
    @Length(max= 300,message="编码长度不能超过300")
    private String apiDesc;

    @Schema(description = "能力名称")
    @Length(max= 30,message="编码长度不能超过30")
    private String abilityName;

    @Schema(description = "应用名称")
    @Length(max= 30,message="编码长度不能超过30")
    private String appName;

    @Schema(description = "申请说明")
    @Length(max= 300,message="编码长度不能超过300")
    private String illustrate;

    @Schema(description="状态")
    private Integer status;

    @Schema(description="调用量限制")
    private Integer recallLimit;

    @Schema(description="并发数限制")
    private Integer qps;

    @Schema(description="备注")
    @Length(max= 300,message="编码长度不能超过300")
    private String note;

    @Schema(description="审批时间")
    private Date approveTime;

    @Schema(description="更新时间")
    private Date updateTime;

    @Schema(description="创建时间")
    private Date createTime;


    @Schema(description = "公司名称")
    @Length(max= 30,message="编码长度不能超过30")
    private String companyName;

    @Schema(description = "政府名称")
    @Length(max= 30,message="编码长度不能超过30")
    private String govName;

    @Schema(description = "申请使用的接口")
    private AbilityApiEntity api;

    /**
     * 请求参数列表
     */
    @Schema(description = "接口的请求参数")
    private List<AbilityApiReq> reqParams;

    /**
     * 相应参数列表
     */
    @Schema(description = "接口的响应参数")
    private List<AbilityApiResp> respParams;

}
