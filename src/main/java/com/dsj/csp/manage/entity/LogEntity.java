package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "GXYYZC_RZB")
public class LogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "RZ_ID",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 日志内容
     */
    @TableField(value = "RZ_NR")
    @Schema(description = "日志内容")
    private String logContent;

    /**
     * 创建人
     */
    @TableField(value = "RZ_CJR")
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "RZ_CJSJ")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "RZ_GXR")
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "RZ_GXSJ")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 耗时
     */
    @TableField(value = "RZ_HS")
    @Schema(description = "耗时")
    private Long costTime;

    /**
     * IP
     */
    @TableField(value = "IP")
    @Schema(description = "IP")
    private String ip;

    /**
     * 请求参数
     */
    @TableField(value = "RZ_QQCS")
    @Schema(description = "请求参数")
    private String requestParam;

    /**
     * 请求类型
     */
    @TableField(value = "RZ_QQLX")
    @Schema(description = "请求类型")
    private String requestType;

    /**
     * 请求路径
     */
    @TableField(value = "RZ_QQLJ")
    @Schema(description = "请求路径")
    private String requestUrl;
    /**
     * 请求方法
     */
    @TableField(value = "RZ_QQFF")
    @Schema(description = "请求方法")
    private String method;

    /**
     * 用户名称
     */
    @TableField(value = "YH_MC")
    @Schema(description = "用户名称")
    private String username;
    /**
     * 用户账户
     */
    @TableField(value = "YH_ID")
    @Schema(description = "用户账户")
    private String userid;

    /**
     * 日志类型（1登录日志，2操作日志）
     */
    @TableField(value = "RZ_LX")
    @Schema(description = "日志类型（1登录日志，2操作日志）")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Integer logType;

    /**
     * 操作类型（1查询，2添加，3修改，4删除,5导入，6导出）
     */
    @TableField(value = "RZ_CZLX")
    @Schema(description = "操作类型（1查询，2添加，3修改，4删除,5导入，6导出）")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Integer operateType;

}
