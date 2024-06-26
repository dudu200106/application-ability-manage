package com.dsj.csp.manage.dto.request;

import com.dsj.csp.common.constant.AccountLoginWay;
import com.dsj.csp.common.dto.UserAccountDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserApproveRequest {
    private String userId;
    private String password;
    private @NotNull @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间") String newPassword;
    private String userName;
    private String phone;
    private String email;
    private Integer status;
    private String note;
    private AccountLoginWay loginWay;
    private String loginName;
}
