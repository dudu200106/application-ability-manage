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

}
