package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = "能力接口管理", description = "用于管理能力api接口")
public class AbilityApiController {

    // TODO  之前忘记划分控制层, 之后在进行接口的划分

    private final AbilityApiBizService abilityApiBizService;
    private final AbilityApiService abilityApiService;

//    @AopLogger(describe = "查询接口简单目录", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询接口简单目录", description = "查询接口简单目录")
    @GetMapping("/get-api-catalog")
    public Result<?> getApiCatalog(@Parameter(description = "是否过滤未发布的接口") boolean onlyPublished,
                                   @Parameter(description = "请求方式") String reqMethod,
                                   @Parameter(description = "状态") Integer status,
                                   @Parameter(description = "用户ID") Long userId,
                                   @Parameter(description = "能力ID") Long abilityId) {
        return Result.success(abilityApiBizService.getApiCatalog(onlyPublished, reqMethod, status, userId, abilityId));
    }

    @AopLogger(describe = "批量删除接口", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "批量删除接口")
    @PostMapping("/delete-batch")
    public Result<?> removeApiBatch(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiIds = deleteDTO.getApiIds();
        List<Long> ids = Arrays.stream(apiIds.split(",")).map(Long::parseLong).toList();
        Boolean delFlag = abilityApiService.removeBatchByIds(ids);
        return Result.success("批量删除接口完成! ", delFlag);
    }

    @AopLogger(describe = "删除接口", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除接口")
    @PostMapping("/delete")
    public Result<?> removeApi(@RequestBody AbilityApiEntity apiEntityi){
        Boolean delFlag = abilityApiService.removeById(apiEntityi.getApiId());
        return Result.success("删除接口完成! ", delFlag);
    }

}
