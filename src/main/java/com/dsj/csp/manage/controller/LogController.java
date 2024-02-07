package com.dsj.csp.manage.controller;


import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.LogEntity;

import com.dsj.csp.manage.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Tag(name = "日志管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@Slf4j
public class LogController {

    public final LogService logService;

    /**
     * 全部清除
     */
    private static final String ALL_ClEAR = "allclear";


    @Operation(summary = "按条件分页查询日志")
    @GetMapping("/list")
    public Result<?> list(
                            @Parameter(description = "名称关键字")String keyword,
                            @Parameter(description = "开始时间") Date startTime,
                            @Parameter(description = "操作类型")String operateType,
                            @Parameter(description = "最后时间")Date endTime, int page, int size){
        return Result.success(logService.select(keyword, startTime, endTime,page,size,operateType));
    }

    /**
     * @功能：删除单个日志记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<LogEntity> delete(@RequestParam(name="id",required=true) String id) {
        Result<LogEntity> result = new Result<LogEntity>();
        LogEntity logEntity = logService.getById(id);
        if(logEntity==null) {
            Result.failed("未找到对应实体");
        }else {
            boolean ok = logService.removeById(id);
            if(ok) {
                result.success("删除成功!");
            }
        }
        return result;
    }

    /**
     * @功能：批量，全部清空日志记录
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<LogEntity> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        Result<LogEntity> result = new Result<LogEntity>();
        if(ids==null || "".equals(ids.trim())) {
            result.failed("参数不识别！");
        }else {
            if(ALL_ClEAR.equals(ids)) {
                this.logService.removeAll();
                result.success("清除成功!");
            }
            this.logService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
}
