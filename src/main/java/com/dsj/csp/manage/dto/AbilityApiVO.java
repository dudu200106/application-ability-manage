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
    @Schema(description="NLJK_XH")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @Schema(description="NL_XH")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description="NLJK_MC")
    private String apiName;

    @Schema(description="NLJK_MS")
    private String description;

    @Schema(description="DYLXZ")
    private Integer recallLimit;

    @Schema(description="BFSXZ")
    private Integer qps;

    @Schema(description="NLJK_XYGS")
    private String respFormat;

    @Schema(description="NLJK_XY")
    private String protocol;

    @Schema(description="NLJK_ZJDZ")
    private String apiHost;

    @Schema(description="NLJK_URL")
    private String apiUrl;

    @Schema(description="NLJK_BBH")
    private String apiVersion;

    @Schema(description="NLJK_CJSJ" )
    private Timestamp createTime;

    @Schema(description = "NLJK_GXSJ")
    private Timestamp updateTime;

    @Schema(description = "WG_GY")
    private String publicKey;

    @Schema(description = "WG_SY")
    private String secretKey;

    @Schema(description = "NLJK_QQFF")
    private String reqMethod;

    @Schema(description = "NLJK_QQSL")
    private String reqDemo;

    @Schema(description = "NLJK_XYSL")
    private String respDemo;

    @Schema(description = "NLJK_XYZTM")
    private String respStatusCode;

    private List<AbilityApiReq> reqList;

    private List<AbilityApiResp> respList;

}
