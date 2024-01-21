package com.dsj.csp.manage.controller;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.request.ContextRequest;
import com.dsj.csp.manage.entity.ContextEntity;
import com.dsj.csp.manage.service.ContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name="应用场景管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/context")
public class ContextController {
    public final ContextService contextService;

    /**
     * 新增应用场景
     * @param context
     * @return
     */
    @Operation(summary = "新增应用场景")
    @PostMapping("/add")
    public Result<?> add(@RequestBody ContextEntity context){
        contextService.add(context);
        return Result.success("新增应用场景");
    }

    /**
     * 修改应用场景
     * @param context
     * @return
     */
    @Operation(summary = "修改应用场景")
    @PostMapping("/update")
    public Result<?> update(@RequestBody ContextRequest context){
        contextService.update(context);
        return Result.success("修改应用场景成功");
    }

    /**
     * 删除应用场景
     * @param context
     * @return
     */
    @Operation(summary = "删除应用场景")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody ContextRequest context){
        contextService.delete(context);
        return Result.success("删除应用场景成功");
    }

    @Operation(summary = "按条件分页查询应用场景")
    @GetMapping("/search")
    public Result<?> search(@Parameter(description = "标题或描述关键字") String keyword,@Parameter(description = "启动状态（0启动  1禁用）") Integer isUsable, int page, int size){;
        return Result.success(contextService.search(keyword, isUsable, page, size));
    }
}
