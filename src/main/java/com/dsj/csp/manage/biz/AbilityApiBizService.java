package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiQueryVO;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AbilityApiBizService {
    List<String> getApiList(String appCode);

    void saveApi(AbilityApiVO apiVO);

    boolean updateApi(AbilityApiVO apiVO);

    AbilityApiVO getApiInfo(Long apiId);

    Page pageApi(AbilityApiQueryVO apiQueryVO);

    List<AbilityApiEntity> getApplyApiList(Long abilityApplyId);

    List<AbilityApiEntity> getAbilityApiList(Long abilityId);

    // 根据appId查询该应用下申请的所有接口
    List<AbilityApiVO> getAppApiList(Long appId);

    // 根据userId查询该用户
    List<AbilityApiVO> getUserApiList(Long userId);

    Page pageApplyApis(Long userId, Long appId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime);

}
