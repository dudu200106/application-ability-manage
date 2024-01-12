package com.dsj.csp.manage.dto;

import com.dsj.csp.manage.entity.AbilityApiEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * 能力注册新增VO
 *
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/11
 */
@Data
public class AbilityLoginVO implements Serializable {

    private String abilityType;

    private String abilityName;

    private Long userId;

    private String abilityProvider;

    private String abilityDesc;

    private Integer status;

    private String note;

    private Integer isDelete;

    private Date createTime;

    private Date updateTime;

    private Integer recallLimit;

    private Integer qps;

    private List<AbilityApiEntity> apiList;

}
