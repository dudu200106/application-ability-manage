package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Data
@TableName("MANAGE_ABILITY")
public class AbilityEntity extends BaseEntity implements Serializable {
    @TableId(value = "NL_XH", type = IdType.AUTO)
    private Long abilityId;

    @TableField("NL_LX")
    private String abilityType;

    @TableField("NL_MC")
    private String abilityName;

    @TableField("YH_ID")
    private Long userId;

    @TableField("NL_TGZ")
    private String abilityProvider;

    @TableField("NL_MS")
    private String abilityDesc;

    @TableField("ZT")
    private Integer status;

    @TableField("BZ")
    private String note;

//    @TableField("SFSC")
//    private Integer isDelete;

    @TableField(value = "CJSJ")
    private Date createTime;

    @TableField(value = "GXSJ")
    private Date updateTime;

    @TableField("DYLXZ")
    private Integer recallLimit;

    @TableField("BFSXZ")
    private Integer qps;

}
