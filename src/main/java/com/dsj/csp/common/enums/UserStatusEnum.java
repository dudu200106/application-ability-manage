package com.dsj.csp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户实名认证状态枚举
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    /**
     * 未实名
     */
    NOAPPROVE(0, "未实名"),

    /**
     * 待审核
     */
    WAIT(1, "待审核"),

    /**
     * 审核通过
     */
    SUCCESS(2, "审核通过"),

    /**
     * 审核未通过
     */
    FAIL(3, "审核未通过");

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
                .map(it -> it.getDescription())
                .orElse("");
    }

    public static List<Integer> notDelStatuses() {
        return Stream.of(StatusEnum.values())
                .filter(it -> it != StatusEnum.DEL)
                .map(StatusEnum::getStatus)
                .collect(Collectors.toList());
    }
}
