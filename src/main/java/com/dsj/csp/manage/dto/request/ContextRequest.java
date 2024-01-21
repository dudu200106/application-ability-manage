package com.dsj.csp.manage.dto.request;

import lombok.Data;

import java.util.Date;
@Data
public class ContextRequest {
    private Integer id;
    private Integer isUsable;
    private String title;
    private String describe;
    private Date updateTime;
    private String url;
    private Integer isDelete;
}
