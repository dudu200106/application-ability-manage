package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 共性应用支撑应用场景
 * @TableName GXYYZC_YYCJ
 */
@TableName(value ="GXYYZC_YYCJ")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContextEntity implements Serializable {
    /**
     * 应用场景序号
     */
    @TableId(value = "YYCJ_XH",type = IdType.AUTO)
    @Schema(description = "应用场景序号")
    private Integer id;

    /**
     * 应用场景标题
     */
    @TableField(value = "YYCJ_BT")
    @Schema(description = "应用场景标题")
    private String title;

    /**
     * 图片路径
     */
    @TableField(value = "YYCJ_TPLJ")
    @Schema(description = "图片路径")
    private String url;

    /**
     * 应用场景图片描述
     */
    @TableField(value = "YYCJ_MS")
    @Schema(description = "应用场景图片描述")
    private String describe;

    /**
     * 是否启动（0启动  1禁用）
     */
    @TableField(value = "YYCJ_QD")
    @Schema(description = "是否启动（0启动  1禁用）")
    private Integer isUsable;

    /**
     * 创建时间
     */
    @TableField(value = "YYCJ_CJSJ")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "YYCJ_GXSJ")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 逻辑删除（0可用  1已删除）
     */
    @TableField(value = "YYCJ_SC")
    @Schema(description = "逻辑删除（0可用  1已删除）")
    private Integer isDelete;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}