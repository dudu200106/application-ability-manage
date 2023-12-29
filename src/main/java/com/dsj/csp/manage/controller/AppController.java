package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.manage.entity.AppEntity;
import com.dsj.csp.manage.dto.PageQueryForm;
import com.dsj.csp.manage.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能说明：应用表Controller
 *
 * @author 蔡云
 * 2023/12/28
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class AppController {

    public final AppService appService;

    /**
     * 查询应用表列表
     */
    @GetMapping("/list")
    public Result<List<AppEntity>> list() {
        return Result.success(appService.list());
    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Result<?> pageTest(Page page, AppEntity app) {
        return Result.success(appService.page(page, Wrappers.query(app)));
    }
    /**
     * 新增应用
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody AppEntity app) {
        app.setAppStatus(StatusEnum.PENDING.getStatus());
        return Result.success(appService.save(app));
    }
}
