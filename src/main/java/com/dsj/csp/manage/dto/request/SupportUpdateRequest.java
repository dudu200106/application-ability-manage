package com.dsj.csp.manage.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:07
 */
@Data
public class SupportUpdateRequest implements Serializable {
    private String title;

    private Integer status;
}
