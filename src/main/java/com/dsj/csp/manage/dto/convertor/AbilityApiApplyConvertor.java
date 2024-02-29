package com.dsj.csp.manage.dto.convertor;

import com.dsj.csp.manage.dto.AbilityApiApplyDTO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */
@Mapper(componentModel = "spring")
public interface AbilityApiApplyConvertor {
    AbilityApiApplyConvertor INSTANCE = Mappers.getMapper(AbilityApiApplyConvertor.class);

    AbilityApiApplyDTO toDTO(AbilityApiApplyEntity apiApplyEntity);
    List<AbilityApiApplyDTO> toDTOs(List<AbilityApiApplyDTO> apiApplyEntityList);
    AbilityApiApplyEntity toEntity(AbilityApiApplyDTO apiApplyDTO);
}
