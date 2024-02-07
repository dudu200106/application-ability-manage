package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */

/**
 * 能力实体类
 */
@Data
@EqualsAndHashCode
@TableName("GXYYZC_NL")
public class AbilityEntity implements Serializable {

    /**
     * 能力ID
     */
    @TableId(value = "NL_ID", type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "能力ID")
    private Long abilityId;

    /**
     * 能力类型
     */
    @TableField("NL_LX")
    @Schema(description = "能力类型")
    private String abilityType;

    /**
     * 名称
     */
    @TableField("NL_MC")
    @Schema(description = "名称")
    @NotBlank
    private String abilityName;

    /**
     * 用户ID
     */
    @TableField("YH_ID")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 能力提供者
     */
    @TableField("NL_TGZ")
    @Schema(description = "能力提供者")
    private String abilityProvider;

    /**
     * 能力描述
     */
    @TableField("NL_MS")
    @Schema(description = "能力描述")
    private String abilityDesc;

    /**
     * 能力状态
     */
    @TableField("ZT")
    @Schema(description = "能力状态")
    private Integer status;

    /**
     * 备注
     */
    @TableField("BZ")
    @Schema(description = "备注")
    private String note;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "IS_DELETE")
    @Schema(description = "逻辑删除")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "CJSJ")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "GXSJ")
    @Schema(description = "修改时间")
    private Date updateTime;
}
