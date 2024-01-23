package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("GXYYZC_NLJK_XYCS")
public class AbilityApiResp implements Serializable {

    @TableId(value = "XYCS_XH_ID", type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long respId;

    @TableField("NLJK_XH_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("XYCS_MC")
    private String respName;

    @TableField("XYCS_LX")
    private String respType;

    @TableField("XYCS_MS")
    private String description;

    @TableField("XYCS_SFBY")
    private Integer isNeed;

}
