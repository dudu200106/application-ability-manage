package com.dsj.csp.manage.service;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@FeignClient(name = "AppFeign", url = "http://106.227.94.62:8001/smart-gateway-admin/allow/app")
public interface AppFeignService {
    /**
     * 新增app
     */
    @PostMapping("/add")
    Result<CryptJsonBody> addApp(@RequestBody CryptJsonBody cryptJsonBody);

    /**
     * 禁用app
     */
    @PostMapping("/cancel")
    Result<CryptJsonBody> cancelApp(@RequestBody CryptJsonBody cryptJsonBody);
}
