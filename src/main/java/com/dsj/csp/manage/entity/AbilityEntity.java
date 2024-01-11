package com.dsj.csp.manage.entity;
import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 能力表
 * @TableName MANAGE_ABILITY
 */
@TableName(value ="MANAGE_ABILITY")
@Data
public class AbilityEntity implements Serializable {
    /**
     * 能力id
     */
    @TableId(value = "ABILITY_ID", type = IdType.AUTO)
    private Long abilityId;

    /**
     * 能力类型
     */
    @TableField(value = "ABILITY_TYPE")
    private Integer abilityType;

    /**
     * 能力名称
     */
    @TableField(value = "ABILITY_NAME")
    private String abilityName;

    /**
     * 能力描述
     */
    @TableField(value = "ABILITY_DESC")
    private String abilityDesc;

    /**
     * 状态(0可用  1不可用)
     */
    @TableField(value = "STATUS")
    private Integer status;

    /**
     * 逻辑删除(0未删除  1已删除)
     */
    @TableField(value = "IS_DELETE")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,value = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "UPDATE_TIME")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}