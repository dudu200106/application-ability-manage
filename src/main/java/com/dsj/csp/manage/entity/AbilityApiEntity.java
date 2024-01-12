package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("MANAGE_ABILITY_API")
public class AbilityApiEntity extends BaseEntity implements Serializable {
    @TableId
    private Long apiId;

    @TableField("ABILITY_ID")
    private Long abilityId;

    @TableField("API_NAME")
    private String apiName;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("RECALL_LIMIT")
    private Integer recallLimit;

    @TableField("QPS")
    private Integer qps;

    @TableField("REQUEST_PARAM")
    private String requestParam;

    @TableField("RESPONSE_PARAM")
    private String responseParam;

    @TableField("PROTOCOL")
    private String protocol;

    @TableField("API_HOST")
    private String apiHost;

    @TableField("BASE_PATH")
    private String basePath;

    @TableField("API_URL")
    private String apiUrl;

    @TableField("API_VERSION")
    private String apiVersion;

    // mybatis plus 好像会与POJO里面的isDelete冲突
//    @TableField("IS_DELETE")
//    private Integer isDelete;

    @TableField(value = "CREATE_TIME" )
    private Timestamp createTime;

    @TableField(value = "UPDATE_TIME")
    private Timestamp updateTime;
}
