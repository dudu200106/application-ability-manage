package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.manage.dto.PageQueryForm;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.service.ManageApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.dsj.csp.manage.util.RandomNumberGenerator.generateNumber;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/9 0009 16:00
 * @Todo:
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping("/magapplication")
public class ManageApplicationController {
    @Autowired
    private ManageApplicationService manageApplicationService;

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询")
    @PostMapping("/list")
    public Result<?> pageAnother(@Valid @RequestBody PageQueryForm<ManageApplication> pageQueryForm) {
        return Result.success(manageApplicationService.page(pageQueryForm.toPage(), pageQueryForm.toQueryWrappers()));
    }
    @Operation(summary = "查询所有")
    @PostMapping("/lists")
    public Result<List<ManageApplication>> list() {
        return Result.success(manageApplicationService.list());
    }

    /**
     * 分页查询
     */
    @Operation(summary = "分页")
    @PostMapping("/page")
    public Result<?> page(Page<ManageApplication> page, ManageApplication app) {
        return Result.success(manageApplicationService.page(page, Wrappers.query(app)));
    }


    /**
     * 新增应用
     */
    @Operation(summary = "添加应用")
    @PutMapping("/add")
    public Result<?> add(@RequestPart("file") MultipartFile file, @Parameter(description = "APP名字") @RequestParam String appName, @RequestParam String appSynopsis) {
        ManageApplication manageApplication = new ManageApplication();
        manageApplication.setAppName(appName);
        manageApplication.setAppSynopsis(appSynopsis);
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            //获取文件名后缀
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 获取文件的字节数组
            //生成舞的义件名
            String newFileName = uuid + "." + fileExtension;
            byte[] bytes = file.getBytes();
            // 构建文件路径
            Path path = Paths.get("D:/picture/" + newFileName);
            // 将文件保存到本地
            Files.write(path, bytes);
            manageApplication.setAppCode(generateNumber(8));//生成appid
            manageApplication.setAppIconpath(String.valueOf(path));//应用路径
//            生成key
//            状态
            manageApplication.setAppStatus(StatusEnum.NORMAL.getStatus());
//            逻辑删除
            manageApplication.setAppIsdelete(0);
            manageApplication.setAppCreatetime(new Date());
            manageApplication.setAppUpdatetime(new Date());
            return Result.success(manageApplicationService.save(manageApplication));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("上传失败");
        }
    }
    @Operation(summary = "删除应用")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam Long appId) {
        return Result.success(manageApplicationService.removeById(appId));
    }

    //查询appid和name
    @Operation(summary = "查询应用")
    @PostMapping("/selectappID")
    public Result selectappID(@RequestParam Long appId, @RequestParam String appUserId) {
        return Result.success(manageApplicationService.selectappID(appId, appUserId));
    }

    //统计应用次数
    @Operation(summary = "统计应用")
    @GetMapping("/all")
    public Result countAll() {
        return Result.success(manageApplicationService.count());
    }

    //用户关联应用查询
    @Operation(summary = "用户下的应用")
    @PostMapping("/selectUserApp")
    public Result selectUserApp(@RequestParam String userId) {
        return Result.success(manageApplicationService.selectUserApp(userId));
    }

    //审核通过后更新key和Secret
//    @PostMapping("/updateSecret")
//    public Result updateSecret(@RequestParam Long appId) {
//        return Result.success(manageApplicationService.updateSecret(appId));
//
//
//    }
    //修改应用信息
    @Operation(summary = "修改应用")
    @PostMapping("/upadataAppList")
    public Result<?> upadataAppList(@RequestPart("file") MultipartFile file, @RequestParam Long appId, @RequestParam String appName, @RequestParam String appSynopsis) throws IOException {

        System.out.println(appId);
        // 获取文件名
        String fileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        //获取文件名后缀
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 获取文件的字节数组
        //生成舞的义件名
        String newFileName = uuid + "." + fileExtension;
        byte[] bytes = file.getBytes();
        // 构建文件路径
        Path path = Paths.get("D:/picture/" + newFileName);
        // 将文件保存到本地
        Files.write(path, bytes);
        String appIconpath = String.valueOf(path);
        return Result.success(manageApplicationService.upadataAppList(appId, appName, appSynopsis, appIconpath));
    }




}