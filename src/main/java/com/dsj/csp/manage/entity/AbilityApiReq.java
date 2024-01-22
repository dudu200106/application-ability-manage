package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("GXYYZC_NLJK_QQCS")
public class AbilityApiReq implements Serializable {

    @TableId(value = "QQCS_XH_ID", type = IdType.NONE)
    private Long reqId;

    @TableField("NLJK_XH_ID")
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
