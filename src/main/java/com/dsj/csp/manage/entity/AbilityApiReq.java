package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("GXYYZC_NLJK_QQCS")
public class AbilityApiReq implements Serializable {

    @TableId(value = "QQCS_ID", type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long reqId;

    @TableField("NLJK_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("QQCS_MC")
    private String reqName;

    @TableField("QQCS_LX")
    private String reqType;

    @TableField("QQCS_MS")
    private String description;

    @TableField("QQCS_SFBY")
    private Integer isNeed;

}
