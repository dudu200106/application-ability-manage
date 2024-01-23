package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.entity.AbilityApiResp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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

    @Schema(description="接口名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apiName;

    @Schema(description="接口描述")
    private String description;

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

    @Schema(description = "网关公钥")
    private String publicKey;

    @Schema(description = "网关私钥")
    private String secretKey;

    @Schema(description = "请求方法", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reqMethod;

    @Schema(description = "请求示例")
    private String reqDemo;

    @Schema(description = "响应示例")
    private String respDemo;

    @Schema(description = "响应状态码")
    private String respStatusCode;

    /**
     * 请求参数列表
     */
    private List<AbilityApiReq> reqList;

    /**
     * 相应参数列表
     */
    private List<AbilityApiResp> respList;

}
