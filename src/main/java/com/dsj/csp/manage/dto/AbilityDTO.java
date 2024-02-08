package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Data
public class AbilityDTO {
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
     * 创建时间
     */
    @TableField(value = "CJSJ")
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd", fallbackPatterns = {"yyyy/MM/dd 00:00:00", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "GXSJ")
    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd", fallbackPatterns = {"yyyy/MM/dd 00:00:00", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"})
    private Date updateTime;

    @Schema(description = "能力接口列表")
    private List<AbilityApiEntity> apiList;
}
