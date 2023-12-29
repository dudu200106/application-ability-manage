package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: yuan qi yan
 * @Date:2023/12/29 16:53
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("ability")
public class AbilityController {

    public final AbilityService abilityService;

    /**
     * 新增能力
     * @param ability
     * @return 执行结果
     */
    @PostMapping("add")
    public Result<?> add(@RequestBody AbilityEntity ability){
        ability.setStatus(StatusEnum.NORMAL.getStatus());
        return Result.success(abilityService.save(ability));
    }

    /**
     * 修改能力
     * @param ability
     * @return 执行结果
     */
    @PutMapping("update")
    public Result<?> update(@RequestBody AbilityEntity ability){
        abilityService.update(ability,null);
        return Result.success("更新成功");
    }

    /**
     * 删除能力（逻辑删除）
     * @param ability
     * @return 执行结果
     */
    @DeleteMapping("delete")
    public Result<?> delete(AbilityEntity ability){
        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("IS_DELETE",ability.getIsDelete()==0);
        abilityService.update(ability,queryWrapper);
        return Result.success("删除成功");
    }

    /**
     * 查询能力
     * @return
     */
    @GetMapping("/list")
    public Result<List<AbilityEntity>> list(){
        return Result.success(abilityService.list());
    }

    /**
     * 分页查询
     * @param page
     * @param ability
     * @return 查询结果
     */
    @GetMapping("/page")
    public Result<?> page(Page page,AbilityEntity ability){
        return Result.success(abilityService.page(page, Wrappers.query(ability)));
    }


}
