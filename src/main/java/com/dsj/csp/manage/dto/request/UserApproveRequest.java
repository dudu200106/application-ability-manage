package com.dsj.csp.manage.dto.request;

import lombok.Data;

@Data
public class UserApproveRequest {
    private String userId;
    private String status;
    private String note;
}
