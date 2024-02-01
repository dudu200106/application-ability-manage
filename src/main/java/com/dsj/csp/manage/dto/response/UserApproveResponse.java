package com.dsj.csp.manage.dto.response;

import lombok.Data;

@Data
public class UserApproveResponse {
    private String password;
    private String newPassword;
    private String newPassword2;
}
