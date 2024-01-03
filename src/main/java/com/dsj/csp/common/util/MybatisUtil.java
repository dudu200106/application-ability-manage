package com.dsj.csp.common.util;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 11:31
 */
public class MybatisUtil {
    private MybatisUtil() {
    }

    public static String likeLeft(String str) {
        return str + "%";
    }

    public static String likeRight(String str) {
        return "%" + str;
    }

    public static String likeBoth(String str) {
        return "%" + str + "%";
    }
}
