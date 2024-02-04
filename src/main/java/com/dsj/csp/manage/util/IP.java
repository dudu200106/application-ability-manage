package com.dsj.csp.manage.util;

import jakarta.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/2/4 13:40
 * @Todo:
 */
public class IP {

    public static String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(",") >0 ){
            String[] parts = ip.split(",");
            for (String part : parts) {
                if (!part.isEmpty() && !"unknown".equalsIgnoreCase(part)) {
                    ip = part.trim();
                    break;
                }
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        System.out.println(ip);
        return ip;
    }

}
