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
@TableName(value ="GXYYZC_WD")
@EqualsAndHashCode
@Data
public class DocEntity implements Serializable {
    /**
     * 文档ID
     */
    @TableId(value = "WD_ID", type = IdType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "文档ID")
    private Long docId;
    /**
     * 文档目录ID
     */
    @TableField(value = "WDML_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "文档目录ID")
    private Long catalogId;
    /**
     * 接口ID
     */
    @TableField(value = "NLJK_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "接口ID")
    private Long apiId;
    /**
     * 文档名称
     */
    @TableField(value = "WD_MC")
    @Schema(description = "文档名称")
    private String docName;

    /**
     * 文档描述
     */
    @TableField(value = "WD_MS")
    @Schema(description = "文档描述")
    private String docDesc;

    /**
     * 创建人
     */
    @TableField(value = "WD_CZR")
    @Schema(description = "创建人")
    private String creator;

    /**
     * 状态(默认:0未提交 1待审核 2审核通过 3审核不通过 4已发布 5已下线)
     */
    @TableField(value = "ZT")
    @Schema(description = "状态(默认:0未提交 1待审核 2审核通过 3审核不通过 4已发布 5已下线)")
    private Integer status;

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
    @TableField(value = "WD_CJSJ")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "WD_GXSJ")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 审批时间
     */
    @TableField(value = "WD_SPSJ")
    @Schema(description = "审批时间")
    private Date approveTime;

    /**
     * 审批时间
     */
    @TableField(value = "WD_FBSJ")
    @Schema(description = "发布时间")
    private Date submitTime;

    /**
     * 文档内容
     */
    @TableField(value = "WD_NR")
    @Schema(description = "文档内容")
    private String content;



}