package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;

import java.util.Date;
import java.util.List;

public interface AbilityApiBizService {
    void saveApi(AbilityApiVO apiVO);

    boolean updateApi(AbilityApiVO apiVO);

    AbilityApiVO getApiInfo(Long apiId);

    List<AbilityApiEntity> getAbilityApiList(Long abilityId);

//    // 根据appId查询该应用下申请的所有接口
//    List<AbilityApiVO> getAppApiList(Long appId);
//
//    // 根据userId查询该用户
//    List<AbilityApiVO> getUserApiList(Long userId);

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
    Page pageApiCatalog(Boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime);

}
