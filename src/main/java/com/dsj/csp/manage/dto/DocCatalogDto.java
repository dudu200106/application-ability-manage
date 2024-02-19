package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.DocEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-19
 */
@Data
public class DocCatalogDto implements Serializable {
    /**
     * 目录Id
     */
    @Schema(description = "目录ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long catalogId;

    /**
     * 目录名称
     */
    @Schema(description = "目录名称")
    private String catalogName;

    /**
     * 目录描述
     */
    @Schema(description = "目录描述")
    private String catalogDesc;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String note;

    /**
     * 文档列表
     */
    @Schema(description = "文档列表")
    private List<DocEntity> docList;
}
