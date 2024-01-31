package com.dsj.csp.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.api.rpc.RpcUserApi;
import com.dsj.csp.common.constant.AccountLoginWay;
import com.dsj.csp.common.dto.UserChangePasswordDTO;
import com.dsj.csp.common.dto.UserSmztDTO;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.enums.UserStatusEnum;
import com.dsj.csp.common.exception.FlowException;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.UserApproveMapper;
import com.dsj.csp.manage.service.UserApproveService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;

/**
 * @author Stars
 * @description 针对表【MANAGE_USER(用户信息表（个人信息，认证信息）)】的数据库操作Service实现
 * @createDate 2024-01-09 14:16:26
 */
@Service
@RequiredArgsConstructor
public class UserApproveApproveServiceImpl extends ServiceImpl<UserApproveMapper, UserApproveEntity> implements UserApproveService {
    @Resource
    private RpcUserApi rpcUserApi;

    //远程调用用户接口，根据token识别用户
    public UserApproveRequest identify(String accessToken) throws RuntimeException {
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
        String code = responseJson.getString("code");
        if (code.equals("-1")) {
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

    //远程调用用户实名状态更新接口
    @Override
    public void approveFeign(String userId, Integer status) {
        UserSmztDTO userSmztDTO = new UserSmztDTO();
        userSmztDTO.setId(userId);
        userSmztDTO.setSmzt(status);
        rpcUserApi.updateSmztById(userSmztDTO);
    }

    @Override
    public Result<Boolean> updatePassword(String password, String newPassword, String newPassword2, String accessToken) {
        UserApproveRequest identify = identify(accessToken);
        UserChangePasswordDTO userChangePasswordDTO = new UserChangePasswordDTO();
        if (newPassword.equals(newPassword2)) {
            userChangePasswordDTO.setPassword(password);
            userChangePasswordDTO.setNewPassword(newPassword);
            userChangePasswordDTO.setLoginWay(AccountLoginWay.of(2));
            userChangePasswordDTO.setLoginName(identify.getLoginName());
            return rpcUserApi.changePassword(userChangePasswordDTO);
        } else {
            return Result.failed("两次密码不一致，请重新输入");
        }
    }


    /**
     * 用户实名认证申请模块
     */
    @Override
    public String approve(UserApproveEntity user, String accessToken) {
        UserApproveRequest user2 = identify(accessToken);
        user.setUserId(user2.getUserId());
        user.setUserName(user2.getUserName());
        UserApproveEntity userApproveEntity = baseMapper.selectById(user);
        if (userApproveEntity != null) {
            Integer status = user2.getStatus();
            if (status.equals(UserStatusEnum.NOAPPROVE.getStatus()) || status.equals(UserStatusEnum.SUCCESS.getStatus()) || status.equals(UserStatusEnum.FAIL.getStatus())) {
                approveFeign(user2.getUserId(), UserStatusEnum.WAIT.getStatus());
                user.setStatus(UserStatusEnum.WAIT.getStatus());
                user.setNote("审核中");
                user.setCreateTime(new Date());
                baseMapper.updateById(user);
                return "实名认证已提交，请等待管理员审核";
            } else {
                throw new FlowException(CodeEnum.APPROVE_ERROR);
            }
        } else {
            approveFeign(user2.getUserId(), UserStatusEnum.WAIT.getStatus());
            user.setStatus(UserStatusEnum.WAIT.getStatus());
            user.setCreateTime(new Date());
            baseMapper.insert(user);
            return "实名认证已提交，请等待管理员审核";
        }
    }

    //回显用户信息到前端
    @Override
    public UserApproveEntity echo(String accessToken) {
        UserApproveRequest identify = identify(accessToken);
        return baseMapper.selectById(identify.getUserId());
    }


    /**
     * 管理员实名认证审核模块
     */
    @Override
    public Page<UserApproveEntity> select(String status, String keyword, Date startTime, Date endTime, int page, int size) {
        QueryWrapper<UserApproveEntity> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(Objects.nonNull(status), UserApproveEntity::getStatus, status)
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), UserApproveEntity::getCreateTime, startTime, endTime)
                .and(StringUtils.isNotBlank(keyword), lambdaQuery -> {
                    lambdaQuery
                            .like(UserApproveEntity::getGovName, keyword)
                            .or()
                            .like(UserApproveEntity::getCompanyName, keyword);
                });
        return baseMapper.selectPage(new Page(page, size), wrapper);
    }

    @Override
    public UserApproveEntity find(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public void approveSuccess(UserApproveRequest user, String accessToken) {
        identify(accessToken);
        approveFeign(user.getUserId(), UserStatusEnum.SUCCESS.getStatus());
        UserApproveEntity userApproveEntity = baseMapper.selectById(user.getUserId());
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(userApproveEntity.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.SUCCESS.getStatus())
                .set(UserApproveEntity::getNote, "实名认证已完成")
                .update();
        if (!updateResult) {
            log.error("更新失败");
            throw new FlowException(CodeEnum.UPDATE_ERROR);
        }
//          FIXME  return updateResult; 在controller做判断并进行不同的响应
        // TODO 为每一次的sql结果负责
    }

    @Override
    public void approveFail(UserApproveRequest user, String accessToken) {
        identify(accessToken);
        approveFeign(user.getUserId(), UserStatusEnum.FAIL.getStatus());
        UserApproveEntity userApproveEntity = baseMapper.selectById(user.getUserId());
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(userApproveEntity.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.FAIL.getStatus())
                .set(UserApproveEntity::getNote, user.getNote())
                .set(UserApproveEntity::getUserType, 0)
                .update();
        if (!updateResult) {
            log.error("更新失败");
            throw new FlowException(CodeEnum.UPDATE_ERROR);
        }
    }

    @Override
    public Long userCount() {
        return baseMapper.selectCount(null);
    }
}