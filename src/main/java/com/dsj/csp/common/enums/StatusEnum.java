package com.dsj.csp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 功能说明：状态枚举
 *
 * @author 蔡云
 * 2023/12/29
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 删除
     */
    DEL(1, "删除"),

    /**
     * 锁定
     */
    LOCK(2, "锁定"),

    /**
     * 待审核
     */
    PENDING(3, "待审核"),

    /**
     * 未通过
     */
    REFUSE(4, "未通过");

    /**
     * 类型
     */
    private final Integer status;
    /**
     * 描述
     */
    private final String description;

    public static String of(Integer status) {
        return Stream.of(StatusEnum.values())
                .filter(it -> it.getStatus().equals(status))
                .findFirst()
                .map(it -> it.description)
                .orElse("");
    }

    public static List<Integer> notDelStatuses() {
        return Stream.of(StatusEnum.values())
                .filter(it -> it != StatusEnum.DEL)
                .map(StatusEnum::getStatus)
                .collect(Collectors.toList());
    }
}
