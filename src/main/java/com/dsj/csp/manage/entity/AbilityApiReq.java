package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("GXYYZC_NLJK_QQCS")
public class AbilityApiReq implements Serializable {

    @TableId(value = "QQCS_ID", type = IdType.NONE)
    @Schema(description="请求参数ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long reqId;

    @TableField("NLJK_ID")
    @Schema(description="接口ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("QQCS_MC")
    @Schema(description="请求参数名称")
    private String reqName;

    @TableField("QQCS_LX")
    @Schema(description="请求参数类型")
    private String reqType;

    @TableField("QQCS_MS")
    @Schema(description="请求参数描述")
    private String description;

    @TableField("QQCS_SFBY")
    @Schema(description="是否必需")
    private Integer isNeed;

}
