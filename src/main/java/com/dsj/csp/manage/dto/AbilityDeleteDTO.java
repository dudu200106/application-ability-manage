package com.dsj.csp.manage.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AbilityDeleteDTO implements Serializable {
    private String abilityIds;

    private String abilityApplyIds;

    private String apiApplyIds;

    private String apiIds;

}
