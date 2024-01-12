package com.dsj.csp.manage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Data
public class ApiListDTO implements Serializable {
    private Long apiId;

    private String apiName;
}
