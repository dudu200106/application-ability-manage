package com.dsj.csp.manage.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传返回结果
 * Created by macro on 2019/12/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinioUploadDto {

    //文件访问URL
    private String url;

    //文件名称
    private String name;
}
