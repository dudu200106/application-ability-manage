package com.dsj.csp.manage.service;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@FeignClient(name = "feignTest", url = "http://106.227.94.62:8001/smart-gateway-admin/allow/apply")
public interface ApplyFeignService {

    /**
     * 新增申请
     */
    @PostMapping("/bind")
    Result<CryptJsonBody> bindApply(@RequestBody CryptJsonBody cryptJsonBody);

    /**
     * 新增申请，包括app和api的新增修改
     */
    @PostMapping("/add")
    Result<CryptJsonBody> addApply(@RequestBody CryptJsonBody cryptJsonBody);

    /**
     * 解绑申请
     */
    @PostMapping("/unbind")
    Result<CryptJsonBody> unbindApply(@RequestBody CryptJsonBody cryptJsonBody);

    /**
     * 批量解绑申请
     */
    @PostMapping("/unbind-batch")
    public Result<Boolean> unbindBatchApply(@RequestBody CryptJsonBody cryptJsonBody);
}
