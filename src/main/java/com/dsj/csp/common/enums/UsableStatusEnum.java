package com.dsj.csp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Getter
@AllArgsConstructor
public enum UsableStatusEnum {
    /**
     * 未实名
     */
    START(0, "启用"),

    /**
     * 待审核
     */
    BAN(1, "禁用");

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
