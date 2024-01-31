package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.entity.AbilityApiResp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AbilityApiVO implements Serializable {
    @Schema(description = "接口ID", requiredMode = Schema.RequiredMode.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @Schema(description="能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "能力名称")
    @Length(max= 30,message="编码长度不能超过30")
    private String abilityName;

    @Schema(description="接口名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Length(max= 30,message="编码长度不能超过30")
    private String apiName;

    @Schema(description="接口描述")
    @Length(max= 300,message="编码长度不能超过300")
    private String apiDesc;

    @Schema(description="调用量限制")
    private Integer recallLimit;

    @Schema(description="并发数限制")
    private Integer qps;

    @Schema(description="响应格式")
    private String respFormat;

    @Schema(description="协议")
    private String protocol;

    @Schema(description="主机地址")
    private String apiHost;

    @Schema(description="接口URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apiUrl;

    @Schema(description="接口版本")
    private String apiVersion;

    @Schema(description="创建时间" )
    private Timestamp createTime;

    @Schema(description = "更新时间")
    private Timestamp updateTime;

//    @Schema(description = "网关公钥")
//    private String publicKey;
//
//    @Schema(description = "网关私钥")
//    private String secretKey;

    @Schema(description = "请求方法", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reqMethod;

    @Schema(description = "请求示例")
    private String reqDemo;

    @Schema(description = "响应示例")
    private String respDemo;

    @Schema(description = "响应状态码")
    private String respStatusCode;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "接口提供者ID")
    private Long userId;

    @Schema(description = "接口提供公司名称")
    private String companyName;

    /**
     * 请求参数列表
     */
    @Schema(description = "接口的请求参数")
    private List<AbilityApiReq> reqList;

    /**
     * 相应参数列表
     */
    @Schema(description = "接口的响应参数")
    private List<AbilityApiResp> respList;

}
