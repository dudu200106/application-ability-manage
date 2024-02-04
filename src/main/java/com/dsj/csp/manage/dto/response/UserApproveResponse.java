package com.dsj.csp.manage.dto.response;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserApproveResponse {
    private String password;
    @Size(min = 8,max = 20,message = "密码长度8~20位。包含字母、数字和符号")
    private String newPassword;
    @Size(min = 8,max = 20,message = "密码长度8~20位。包含字母、数字和符号")
    private String newPassword2;
}
