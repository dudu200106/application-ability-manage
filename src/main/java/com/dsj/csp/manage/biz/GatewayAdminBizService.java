package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
public interface GatewayAdminBizService {

//    应用
    /**
     * 向网关中新增app
     * @param appEntity app实体
     * @return 是否新增成功
     */
    boolean addGatewayApp(ManageApplicationEntity appEntity);

    /**
     * 禁用网关中的app
     * @param appEntity app实体
     * @return 是否禁用成功
     */
    boolean cancelGatewayApp(ManageApplicationEntity appEntity);

    // 接口
    /**
     * 向网关中新增api
     * @param apiEntity api实体
     * @return ApiHandleVO
     */
    boolean addGatewayApi(AbilityApiEntity apiEntity);

    /**
     * 禁用api
     * @param apiEntity api实体
     * @return ApiHandleVO
     */
    boolean cancelGatewayApi(AbilityApiEntity apiEntity);

    // 应用申请接口
    /**
     * 向网关中添加应用接口申请
     * @param applyEntity 申请实体
     * @return 申请处理VO
     */
    boolean addGatewayApply(AbilityApiApplyEntity applyEntity);

    /**
     * 保存申请，包括app和api的新增修改
     * @param app
     * @param api
     * @param apply
     * @return
     */
    boolean saveApplyComplete(ManageApplicationEntity app, AbilityApiEntity api, AbilityApiApplyEntity apply);

    /**
     * 解绑网关中应用接口申请
     * @param applyEntity 申请实体
     * @return 申请处理VO
     */
    boolean unbindApply(AbilityApiApplyEntity applyEntity);

    /**
     * 批量解绑申请
     * @param applyIdList 申请ID列表
     * @return 申请处理VO
     */
    boolean unbindBatchApply(List<Long> applyIdList);
}
