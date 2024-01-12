package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Data
@Schema(description = "能力列表DTO")
public class AbilityListDTO implements Serializable {
    @Schema(description = "能力ID")
    private Long abilityId;

    @Schema(description = "能力类型")
    private String abilityType;

    @Schema(description = "能力名称")
    private String abilityName;

    @Schema(description = "能力描述")
    private String abilityDesc;

    @Schema(description = "能力提供者")
    private String abilityProvider;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "召回限制")
    private Integer recallLimit;

    @Schema(description = "每秒请求数")
    private Integer qps;

    @Schema(description = "API列表")
    private List<ApiListDTO> apiListDTOS;
}
