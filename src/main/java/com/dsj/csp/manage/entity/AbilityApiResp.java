package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@TableName("GXYYZC_NLJK_XYCS")
public class AbilityApiResp implements Serializable {

    @TableId(value = "XYCS_ID", type = IdType.NONE)
    @Schema(description="响应参数ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long respId;

    @TableField("NLJK_ID")
    @Schema(description="接口ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("XYCS_MC")
    @Schema(description="响应参数名称")
    private String respName;

    @TableField("XYCS_LX")
    @Schema(description="响应参数类型")
    private String respType;

    @TableField("XYCS_MS")
    @Schema(description="响应参数描述")
    private String description;

    @TableField("XYCS_SFBY")
    @Schema(description="是否必需")
    private Integer isNeed;

}
