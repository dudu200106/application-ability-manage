package com.dsj.csp.manage.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.exception.FlowException;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
public class IdentifyUser {
    public static UserApproveRequest getUserInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            throw new BusinessException("用户请求上下文获取失败");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = getTokenFromRequest(request);
        UserApproveRequest identify = identify(token);
        return identify;
    }

    private static String getTokenFromRequest(HttpServletRequest request) {
        Enumeration<String> authorization = request.getHeaders("Authorization");
        if (authorization == null || !authorization.hasMoreElements()) {
            throw new FlowException(CodeEnum.TOKEN_ERROR);
        }
        return authorization.nextElement().replace("Bearer ", "");
    }

    private static UserApproveRequest identify(String accessToken) throws RuntimeException {
        RestTemplate restTemplate = new RestTemplate();
        String serverURL = "http://106.227.94.62:8001";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String url = serverURL + "/auth/userInfo?accessToken=" + accessToken;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String responseBody = response.getBody();
        JSONObject responseJson = JSON.parseObject(responseBody);
        JSONObject dataJson = JSON.parseObject(responseJson.getString("data"));
        int code = responseJson.getInteger("code");
        if (code==-1) {
            throw new FlowException(CodeEnum.TOKEN_ERROR);
        }
        UserApproveRequest userApproveRequest = new UserApproveRequest();
        userApproveRequest.setUserId(dataJson.getString("id"));
        userApproveRequest.setUserName(dataJson.getString("name"));
        userApproveRequest.setStatus(dataJson.getInteger("smzt"));
        userApproveRequest.setPhone(dataJson.getString("phone"));
        userApproveRequest.setLoginName(dataJson.getString("loginName"));
        return userApproveRequest;
    }
}
