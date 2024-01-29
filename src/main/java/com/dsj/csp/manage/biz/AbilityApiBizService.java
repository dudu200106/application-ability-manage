package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiQueryVO;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.AbilityApplyDTO;
import com.dsj.csp.manage.entity.AbilityApiEntity;

import java.util.List;

public interface AbilityApiBizService {
    List<String> getApiList(String appCode);

    void saveApi(AbilityApiVO apiVO);

    boolean updateApi(AbilityApiVO apiVO);

    AbilityApiVO getApiInfo(Long apiId);

    Page pageApi(AbilityApiQueryVO apiQueryVO);

    List<AbilityApiEntity> getApplyApiList(Long abilityApplyId);

    List<AbilityApiEntity> getAbilityApiList(Long abilityId);

    // 统计用户申请的能力接口数
    long countUserApplyApi(String userId);

    // 根据appId查询该应用下申请的所有接口
    List<AbilityApiEntity> getAppApiList(Long appId);

    // 根据userId查询该用户
    List<AbilityApiEntity> getUserApiList(Long userId);

}
