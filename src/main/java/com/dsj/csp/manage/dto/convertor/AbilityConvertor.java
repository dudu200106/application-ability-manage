package com.dsj.csp.manage.dto.convertor;

import com.dsj.csp.manage.dto.AbilityDTO;
import com.dsj.csp.manage.entity.AbilityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */

// componentModel = "spring" : 开启spring依赖注入, 可以以组件的形式注册到ApplicationContext中
@Mapper(componentModel = "spring")
public interface AbilityConvertor {
    AbilityConvertor INSTANCE = Mappers.getMapper(AbilityConvertor.class);

    AbilityDTO toDTO(AbilityEntity abilityEntity);
    List<AbilityDTO> toDTOs(List<AbilityDTO> abilityEntity);
    AbilityEntity toEntity(AbilityDTO abilityDTO);
}
