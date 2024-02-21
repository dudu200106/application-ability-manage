package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */

@TableName(value ="GXYYZC_WDML")
@EqualsAndHashCode
@Data
public class DocCatalogEntity implements Serializable {
    /**
     * 目录Id
     */
    @TableId(value = "WDML_ID", type = IdType.AUTO)
    @Schema(description = "目录ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long catalogId;

    /**
     * 目录名称
     */
    @TableField(value = "WDML_MC")
    @Schema(description = "目录名称")
    private String catalogName;

    /**
     * 目录描述
     */
    @TableField(value = "WDML_MS")
    @Schema(description = "目录描述")
    private String catalogDesc;

    /**
     * 备注
     */
    @TableField(value = "BZ")
    @Schema(description = "备注")
    private String note;

    /**
     * 是否删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "IS_DELETE")
    @Schema(description = "是否删除")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "WDML_CJSJ", fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "WDML_GXSJ", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 图标名称
     */
    @TableField(value = "TB")
    @Schema(description = "图标名称")
    private String iconName;

}