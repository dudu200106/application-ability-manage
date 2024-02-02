package com.dsj.csp.manage.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用接口绑定表
 *
 * @TableName GXYYZC_YYJKBD
 */
@Data
@EqualsAndHashCode
@TableName("GXYYZC_YYJKBD")
public class AppApiBand implements Serializable {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @TableField("YY_ID")
    private Long appId;
    /**
     * 接口ID
     */
    @Schema(description = "接口ID")
    @TableField("NLJK_ID")
    private Long apiId;
    /**
     * 能力ID
     */
    @Schema(description = "能力ID")
    @TableField("NL_ID")
    private Long abilityId;
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableField("YH_ID")
    private String userId;


}
