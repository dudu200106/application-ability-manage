package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.AbilityApiEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 能力注册新增VO
 *
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/11
 */
@Data
@Schema(description = "能力注册VO")
public class AbilityLoginVO implements Serializable {

    @Schema(description = "能力类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String abilityType;

    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String abilityName;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "能力提供者")
    private String abilityProvider;

    @Schema(description = "能力描述")
    private String abilityDesc;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.AUTO)
    private Integer status;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "是否删除", requiredMode = Schema.RequiredMode.AUTO)
    private Integer isDelete;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.AUTO)
    private Date createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.AUTO)
    private Date updateTime;

    @Schema(description = "调用数量限制")
    private Integer recallLimit;

    @Schema(description = "并发请求量限制")
    private Integer qps;

    @Schema(description = "API列表")
    private List<AbilityApiEntity> apiList;
}
