package com.dsj.csp.manage.dto.convertor;

import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */
// 开启spring依赖注入
@Mapper(componentModel = "spring")
public interface AbilityApiConvertor {
    // 获取映射转换器实例
    AbilityApiConvertor INSTANCE = Mappers.getMapper(AbilityApiConvertor.class);

    // 同时使用lombok和MapStruct时, 需要在annotationProcessorPaths--注解处理器中指定执行顺序,不然会预编译会冲突
    AbilityApiVO toVO(AbilityApiEntity apiEntity);
    List<AbilityApiVO> toVOs(List<AbilityApiEntity> apiEntityList);
    AbilityApiEntity toEntity(AbilityApiVO apiVO);

}
