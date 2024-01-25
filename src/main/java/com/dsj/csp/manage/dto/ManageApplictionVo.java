package com.dsj.csp.manage.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/17 9:48
 * @Todo:
 */
@Data
public class ManageApplictionVo implements Serializable {
    /**
     * 用户id
     */

    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 企业名称
     */
    private String companyName;


    /**
     * 政府部门名称
     */
    private String govName;

    //    应用名称
    private String appName;


    private String appKey;

//    private String appCode;
    //    appid


    //    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @JsonProperty
    private String appId;

    /**
     * 密令
     */
    private String appSecret;


    //创建时间
//    private Date appCreatetime/1000;
    private Date appCreatetime;
//  private  Timestamp timestamp = new Timestamp(appCreatetime.getTime());


    //应用图标
    private String appIconpath;
    //应用簡介
    private String appSynopsis;
    private  Long apptime;
    private  String appUserId;
}
