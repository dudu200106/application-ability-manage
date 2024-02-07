package com.dsj.csp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/2/6 10:18
 * @Todo:日志枚举
 */
@Getter
@AllArgsConstructor
public enum LogEnum {
    SELECT(1,"操作类型查询"),
    INSERT(2,"操作类型添加"),
    UPDATE(3,"操作类型修改"),
    DELECT(4,"操作类型删除"),
    IMPORT(5,"操作类型导入"),
    EXPORT(6,"操作类型导出"),
    LOGTYPE(1,"登录日志"),
    OPERATETYPE(2,"操作日志"),
    ;

    private final Integer code;
    /**
     * 描述
     */
    private final String description;


}
