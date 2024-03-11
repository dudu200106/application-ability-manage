package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiApplyDTO;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AbilityApiApplyBizService {

    /**
     * 新增接口使用申请
     * @param applyVO
     * @param userApproveRequest
     */
    void saveApiApply(AbilityApiApplyEntity applyVO, UserApproveRequest userApproveRequest);

    /**
     * 获取接口申请信息
     * @param apiApplyId
     * @return
     */
    AbilityApiApplyDTO getApplyInfo(Long apiApplyId);

    /**
     * 分页获取接口申请列表
     * @param onlySubmitted
     * @param appId
     * @param userId
     * @param abilityId
     * @param keyword
     * @param status
     * @param startTime
     * @param endTime
     * @param current
     * @param size
     * @return
     */
    Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId, String keyword, Integer status, Date startTime, Date endTime, int current, int size);

    /**
     * 批量审核接口
     * @param applyEntities 要审核的申请列表
     * @param auditStatus 审核状态
     * @param note
     * @return
     */
    boolean auditApplyBatch(List<AbilityApiApplyEntity> applyEntities, Integer auditStatus, String note);

    /**
     * 撤回申请
     * @param applyId
     * @param note
     * @return
     */
    boolean auditWithdraw(Long applyId, String note);

    /**
     * 提交申请
     * @param applyId
     * @param note
     * @return
     */
    boolean auditSubmit(Long applyId, String note);

    /**
     * 申请审核通过
     * @param applyId
     * @param note
     * @return
     */
    boolean auditPass(Long applyId, String note);
    /**
     * 申请审核不通过
     * @param applyId
     * @param note
     * @return
     */
    boolean auditNotPass(Long applyId, String note);

    /**
     * 停用申请
     * @param applyId
     * @param note
     * @return
     */
    boolean auditStop(Long applyId, String note);

    /**
     * 获取用户ID, 通过用户名称模糊查询
     * @param keyword
     * @return
     */
    Set<String> getUserIds(String keyword);

    /**
     * 获取应用ID, 通过应用ID和名称模糊查询
     * @param userId
     * @param keyword
     * @return
     */
    Set<String> getAppIds(Long userId, String keyword);

    /**
     * 批量申请接口
     * @param applyList
     * @param userApproveRequest
     */
    void saveApiApplyBatch(List<AbilityApiApplyEntity> applyList, UserApproveRequest userApproveRequest);

    /**
     * 根据应用ID, 删除应用关联所有接口申请
     * @param appId 应用ID
     * @return
     */
    void deleteApiApplyByAppId(Long appId);
}
