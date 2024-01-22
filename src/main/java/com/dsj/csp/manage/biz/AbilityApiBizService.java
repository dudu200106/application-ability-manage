package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityApiVO;

import java.util.List;

public interface AbilityApiBizService {
    List<String> getApiList(String appCode);

    void saveApi(AbilityApiVO apiVO);

    boolean updateApi(AbilityApiVO apiVO);

    Object getApiInfo(Long apiId);

}
