package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-18
 */

@Data
public class DocDto  implements Serializable {
    /**
     * 文档ID
     */
    @Schema(description = "文档ID")
    private Long docId;

    /**
     * 文档目录ID
     */
    @Schema(description = "文档目录ID")
    private Long catalogId;

    /**
     * 文档目录名称
     */
    @Schema(description = "文档目录名称")
    private String catalogName;

    /**
     * 接口ID
     */
    @Schema(description = "接口ID")
    private Long apiId;

    /**
     * 文档名称
     */
    @Schema(description = "文档名称")
    private String docName;

    /**
     * 文档描述
     */
    @Schema(description = "文档描述")
    private String docDesc;

    /**
     * 操作人
     */
    @Schema(description = "操作人")
    private String operator;

    /**
     * 状态(默认:0未提交 1待审核 2审核通过 3审核不通过 4已发布 5已下线)
     */
    @Schema(description = "状态(默认:0未提交 1待审核 2审核通过 3审核不通过 4已发布 5已下线)")
    private Integer status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String note;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;
    /**
     * 审批时间
     */
    @Schema(description = "审批时间")
    private Date approveTime;
    /**
     * 文档内容
     */
    @Schema(description = "文档内容")
    private String content;

}
