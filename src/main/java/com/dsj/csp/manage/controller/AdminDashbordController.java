
package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "首页统计")
@RequestMapping("/dashboard")
@Validated
public class AdminDashbordController {
    private final Log logger = LogFactory.getLog(AdminDashbordController.class);

    @Autowired
    private ManageApplicationService manageApplicationService;
    @Autowired
    private UserApproveService userApproveServicel;
    @Autowired
    private AbilityService abilityService;


    @Operation(summary = "后端首页统计应用")
    @GetMapping("/getTotal")
    public Object info() {
        int appTotal = (int) manageApplicationService.count();
        int userTotal= (int) userApproveServicel.count();
        int abilityTotal= (int) abilityService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("appTotal", appTotal);
        data.put("userTotal",userTotal);
        data.put("abilityTotal",abilityTotal);
        return Result.success(data);
    }

}

