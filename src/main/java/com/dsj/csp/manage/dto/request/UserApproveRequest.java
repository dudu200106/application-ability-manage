package com.dsj.csp.manage.dto.request;

import lombok.Data;

@Data
public class UserApproveRequest {
    private String userId;
    private String userName;
    private String phone;
    private String email;
    private Integer status;
    private String note;
}
