package com.dsj.csp.manage.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:07
 */
@Data
public class SupportQueryRequest implements Serializable {
    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private String abilityName;

    private String apiName;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeBegin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTimeBegin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTimeEnd;
}
