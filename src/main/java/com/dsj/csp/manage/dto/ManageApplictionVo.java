package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/17 9:48
 * @Todo:
 */
@Data
public class ManageApplictionVo {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 企业名称
     */
    private String userName;

    private String companyName;


    /**
     * 政府部门名称
     */
    private String govName;

    //    应用名称
    private String appName;


    private String appKey;

    private String appCode;
    //    appid
    private String appId;

    /**
     * 密令
     */
    private String appSecret;


    //创建时间
    private Date appCreatetime;


    private String nljkUrl;

    private String appIconpath;

}
