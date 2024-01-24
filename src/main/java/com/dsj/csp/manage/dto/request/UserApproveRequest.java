package com.dsj.csp.manage.dto.request;

import lombok.Data;

@Data
public class UserApproveRequest {
    private String userId;
    private Integer status;
    private String userName;
    private String note;
}
