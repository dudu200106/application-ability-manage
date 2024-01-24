package com.dsj.csp.manage.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/17 9:48
 * @Todo:
 */
@Data
public class ManageApplictionDto {

//    private String appCode;
    //    appid


    //    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @JsonProperty


    /**
     * 密令
     */
    private String appSecret;

    private String appName;
    //创建时间
    private Date appCreatetime;

    //应用图标
    private String appIconpath;
    //应用簡介
    private String appSynopsis;
    private String userId;
}
