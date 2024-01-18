package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Repository
public interface AbilityApiMapper extends BaseMapper<AbilityApiEntity> {

    List<String> getApiIdList(Long abilityId);
}
