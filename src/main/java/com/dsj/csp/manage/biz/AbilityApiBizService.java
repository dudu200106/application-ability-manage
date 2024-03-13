package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.AbilityApiEntity;

import java.util.Date;
import java.util.List;

public interface AbilityApiBizService {
    /**
     * 新增接口
     * @param apiVO
     * @param userApproveRequest
     * @return
     */
    boolean saveApi(AbilityApiVO apiVO, UserApproveRequest userApproveRequest);

    /**
     * 通用审核接口
     * @param auditVO
     * @return
     */
    boolean auditApi(AbilityAuditVO auditVO);

    /**
     * 撤回接口注册
     * @param apiId
     * @param note
     * @return
     */
    boolean auditWithdraw(Long apiId, String note);

    /**
     * 提交接口注册
     * @param apiId
     * @param note
     * @return
     */
    boolean auditSubmit(Long apiId, String note);

    /**
     * 审核接口注册不通过
     * @param apiId
     * @param note
     * @return
     */
    boolean auditNotPass(Long apiId, String note);

    /**
     * 审核接口注册通过
     * @param apiId
     * @param note
     * @return
     */
    boolean auditPass(Long apiId, String note);

    /**
     * 发布接口
     * @param apiId
     * @param note
     * @return
     */
    boolean auditPublish(Long apiId, String note);

    /**
     * 下线接口, 不能存在接口正在被使用中
     * @param apiId
     * @param note
     * @return
     */
    boolean auditOffline(Long apiId, String note);
    /**
     * 批量删除接口
     * @param apiList
     * @return
     */
    boolean deleteApiBatch(List<AbilityApiEntity> apiList);

    /**
     * 更新接口
     * @param apiVO
     * @return
     */
    boolean updateApi(AbilityApiVO apiVO);

    /**
     * 获取接口信息, 包括接口参数列表
     * @param apiId
     * @return
     */
    AbilityApiVO getApiInfo(Long apiId);

    /**
     * 通过能力分类Id, 获取接口列表
     * @param abilityId
     * @return
     */
    List<AbilityApiEntity> getAbilityApiList(Long abilityId);

    /**
     * 根据appId查询该应用下申请的所有接口
     * @param appId
     * @return
     */
    List<AbilityApiVO> getAppApiList(Long appId);

    /**
     * 根据userId查询该用户
     * @param userId
     * @return
     */
    List<AbilityApiVO> getUserApiList(Long userId);

    /**
     * 查询申请到的接口列表分页(审核通过的api申请)
     * @param userId
     * @param appId
     * @param abilityId
     * @param keyword
     * @param size
     * @param current
     * @param startTime
     * @param endTime
     * @return 返回申请审核通过的api列表
     */
    Page pagePassedApis(Long userId, Long appId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime);

    /**
     * 查询接口目录分页列表
     * @param onlyPublished 是否过滤所有未发布的接口
     * @param userId
     * @param abilityId
     * @param keyword
     * @param size
     * @param current
     * @param startTime
     * @param endTime
     * @return 接口目录
     */
    Page pageApiCatalog(Boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId, String keyword, int current, int size, Date startTime, Date endTime);

    /**
     * 查询全部接口目录列表
     * @param onlyPublished
     * @param reqMethod
     * @param status
     * @param userId
     * @param abilityId
     * @return
     */
    List<AbilityApiEntity> getApiCatalog(boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId);
}
