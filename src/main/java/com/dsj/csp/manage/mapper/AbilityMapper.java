package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsj.csp.manage.dto.AbilityListDTO;
import com.dsj.csp.manage.entity.AbilityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Repository
public interface AbilityMapper extends BaseMapper<AbilityEntity> {
    List<AbilityListDTO> getAbilityList();

    void insertAbility(AbilityEntity ability);
}
