package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.AppEntity;
import com.dsj.csp.manage.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能说明：应用表Controller
 *
 * @author 蔡云
 * @date 2023/12/28
 */
@RequiredArgsConstructor
@RestController
public class AppController {

    public final AppService appService;

    /**
     * 查询应用表列表
     */
    @GetMapping("/app/list")
    public Result<List<AppEntity>> list() {
        return Result.success(appService.list());
    }

    /**
     * 分页查询应用表列表
     */
    @PostMapping("/app/page")
    public Result<Page<AppEntity>> page(Page<AppEntity> page, AppEntity app) {
        return Result.success(appService.page(page, Wrappers.query(app)));
    }
}
