package com.dsj.csp.manage.service;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用于代理智能网关管理端的远程服务调用
 *
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@FeignClient(name = "feignTest", url = "http://106.227.94.62:8001/smart-gateway-admin/allow/api")
public interface ApiFeignService {
    /**
     * 新增api
     */
    @PostMapping("/add")
    Result<CryptJsonBody> addApi(@RequestBody CryptJsonBody cryptJsonBody);

    /**
     * 禁用api
     */
    @PostMapping("/cancel")
    Result<CryptJsonBody> cancelApi(@RequestBody CryptJsonBody cryptJsonBody);
}
