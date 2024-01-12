package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Data
public class AbilityListDTO implements Serializable {
    private Long abilityId;

    private String abilityType;

    private String abilityName;

    private String abilityDesc;

    private String abilityProvider;

    private Timestamp createTime;

    private Integer recallLimit;

    private Integer qps;

    private List<ApiListDTO> apiListDTOS;
}
