package com.dsj.csp.manage.converter;

import com.dsj.csp.manage.dto.SupportDto;
import com.dsj.csp.manage.entity.SupportEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/29 16:58
 */
public class SupportConverter {
    private SupportConverter() {
    }

    public static SupportDto toSupportDto(SupportEntity entity) {
        SupportDto dto = new SupportDto();
        dto.setSupportId(entity.getSupportId());
        dto.setAppId(entity.getAppId());
        dto.setTitle(entity.getTitle());
        dto.setAbilityId(entity.getAbilityId());
        dto.setAbilityName(entity.getAbilityName());
        dto.setApiId(entity.getApiId());
        dto.setApiName(entity.getApiName());
        dto.setCreateUserId(entity.getCreateUserId());
        dto.setCreateUserName(entity.getCreateUserName());
        dto.setAcceptUserId(entity.getAcceptUserId());
        dto.setAcceptUserName(entity.getAcceptUserName());
        dto.setDescription(entity.getDescription());
        dto.setRequestJson(entity.getRequestJson());
        dto.setResponseJson(entity.getResponseJson());
        dto.setStatus(entity.getStatus());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setFinishTime(entity.getFinishTime());
        return dto;
    }

    public static List<SupportDto> toSupportDtoList(Collection<SupportEntity> entityList) {
        return entityList.stream()
                .map(SupportConverter::toSupportDto)
                .toList();
    }
}
