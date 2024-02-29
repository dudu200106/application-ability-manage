package com.dsj.csp.manage.dto.convertor;

import com.dsj.csp.manage.dto.DocDto;
import com.dsj.csp.manage.entity.DocEntity;
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
public interface DocConvertor {
    DocConvertor INSTANCE = Mappers.getMapper(DocConvertor.class);

    DocDto toDTO(DocEntity doc);
    List<DocDto> toDTOs(List<DocEntity> docList);
    DocEntity toEntity(DocDto docDto);
}
