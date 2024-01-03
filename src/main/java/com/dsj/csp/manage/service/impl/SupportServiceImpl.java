package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.util.MybatisUtil;
import com.dsj.csp.manage.dto.SupportCommunicationDto;
import com.dsj.csp.manage.dto.request.*;
import com.dsj.csp.manage.dto.response.SupportCommunicationHistoryResponse;
import com.dsj.csp.manage.entity.SupportCommunicationEntity;
import com.dsj.csp.manage.entity.SupportEntity;
import com.dsj.csp.manage.entity.constant.SupportStatus;
import com.dsj.csp.manage.mapper.SupportCommunicationMapper;
import com.dsj.csp.manage.mapper.SupportMapper;
import com.dsj.csp.manage.service.SupportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 11:20
 */
@Service
public class SupportServiceImpl implements SupportService {
    @Autowired
    private SupportMapper supportMapper;
    @Autowired
    private SupportCommunicationMapper supportCommunicationMapper;

    @Override
    public Page<SupportEntity> selectSupportList(SupportQueryRequest request) {
        return supportMapper.selectPage(new Page<>(request.getPageNum(), request.getPageSize()),
                new LambdaQueryWrapper<SupportEntity>()
                        .eq(SupportEntity::getIsDelete, 0)
                        .like(null != request.getAbilityName(), SupportEntity::getAbilityName, MybatisUtil.likeBoth(request.getAbilityName()))
                        .like(null != request.getApiName(), SupportEntity::getApiName, MybatisUtil.likeBoth(request.getApiName()))
                        .eq(null != request.getStatus(), SupportEntity::getStatus, request.getStatus())
                        .le(null != request.getCreateTimeBegin(), SupportEntity::getCreateTime, request.getCreateTimeBegin())
                        .ge(null != request.getCreateTimeEnd(), SupportEntity::getCreateTime, request.getCreateTimeEnd())
                        .le(null != request.getFinishTimeBegin(), SupportEntity::getFinishTime, request.getFinishTimeBegin())
                        .ge(null != request.getFinishTimeEnd(), SupportEntity::getFinishTime, request.getFinishTimeEnd())
        );
    }

    @Override
    public SupportEntity selectSupportById(Long supportId) {
        SupportEntity supportEntity = supportMapper.selectById(supportId);
        if (null == supportEntity || supportEntity.isDeleted()) {
            throw new BusinessException("工单不存在");
        }
        return supportEntity;
    }

    @Override
    public SupportEntity acceptSupport(Long supportId, SupportAcceptRequest request) {
        SupportEntity supportEntity = selectSupportById(supportId);
        SupportStatus status = SupportStatus.of(supportEntity.getStatus());
        if (Objects.requireNonNull(status) != SupportStatus.SUBMITTED) {
            throw new BusinessException("工单不允许受理");
        }
        supportEntity.setAcceptUserId(request.getAcceptUserId());
        supportEntity.setAcceptUserName(request.getAcceptUserName());
        supportEntity.setStatus(SupportStatus.PROCESSING.getCode());
        updateInternal(supportEntity);
        return supportEntity;
    }

    @Override
    public SupportEntity createSupport(SupportCreateRequest request) {
        SupportEntity supportEntity = new SupportEntity();
        BeanUtils.copyProperties(request, supportEntity);
        supportEntity.setStatus(SupportStatus.SUBMITTED.getCode());
        int successRow = supportMapper.insert(supportEntity);
        if (successRow != 1) {
            throw new BusinessException("创建工单失败");
        }
        return supportEntity;
    }

    @Override
    public SupportEntity updateSupport(Long supportId, SupportUpdateRequest request) {
        if (Objects.nonNull(request.getStatus())) {
            SupportStatus status = SupportStatus.of(request.getStatus());
            if (Objects.isNull(status)) {
                throw new BusinessException("工单状态错误");
            }
        }
        SupportEntity supportEntity = selectSupportById(supportId);
        BeanUtils.copyProperties(request, supportEntity);
        supportMapper.updateById(supportEntity);
        return supportEntity;
    }

    @Override
    public SupportEntity supportFinish(Long supportId) {
        SupportEntity supportEntity = selectSupportById(supportId);
        SupportStatus status = SupportStatus.of(supportEntity.getStatus());
        if (Objects.requireNonNull(status) == SupportStatus.FINISHED) {
            return supportEntity;
        }
        if (Objects.requireNonNull(status) != SupportStatus.PROCESSING) {
            throw new BusinessException("工单状态异常");
        }
        supportEntity.setStatus(SupportStatus.FINISHED.getCode());
        supportEntity.setFinishTime(new Date());
        updateInternal(supportEntity);
        return supportEntity;
    }

    @Override
    public void deleteSupportById(Long supportId) {
        SupportEntity supportEntity = supportMapper.selectById(supportId);
        if (null == supportEntity) {
            throw new BusinessException("工单不存在");
        }
        if (supportEntity.isDeleted()) {
            return;
        }
        supportMapper.deleteById(supportId);
    }

    @Override
    public SupportCommunicationHistoryResponse replySupport(Long supportId, SupportReplyRequest request) {
        if (null == request.getReplyUserId()) {
            throw new BusinessException("必须指定回复人");
        }
        SupportEntity support = selectSupportById(supportId);
        SupportStatus status = SupportStatus.of(support.getStatus());
        if (Objects.requireNonNull(status) != SupportStatus.PROCESSING) {
            throw new BusinessException("工单不允许回复");
        }
        SupportCommunicationEntity communication = new SupportCommunicationEntity();
        communication.setSupportId(supportId);
        communication.setAppId(support.getAppId());
        communication.setSenderId(request.getReplyUserId());
        communication.setSenderName(request.getReplyUserName());
        communication.setReceiverId(support.getAppId());
        communication.setReceiverName(support.getAcceptUserName());
        communication.setContent(request.getContent());

        int successRow = supportCommunicationMapper.insert(communication);
        if (successRow <= 0) {
            throw new BusinessException("回复失败");
        }
        List<SupportCommunicationEntity> communicationList = selectCommunicationByIdOrderByCreateTimeDesc(support.getSupportId());
        return new SupportCommunicationHistoryResponse()
                .setAppId(support.getAppId())
                .setRefresh(true)
                .setCommunicationList(communicationList.stream()
                        .map(SupportServiceImpl::toCommunicationDto)
                        .toList()
                );
    }

    private List<SupportCommunicationEntity> selectCommunicationByIdOrderByCreateTimeDesc(Long supportId) {
        return supportCommunicationMapper.selectList(new LambdaQueryWrapper<SupportCommunicationEntity>()
                .eq(SupportCommunicationEntity::getSupportId, supportId)
                .orderByDesc(SupportCommunicationEntity::getCreateTime)
        );
    }

    @Override
    public SupportCommunicationHistoryResponse getCommunicationBySupportId(Long supportId, Long lastCommunicationId) {
        SupportEntity support = selectSupportById(supportId);
        List<SupportCommunicationEntity> communicationList = selectCommunicationByIdOrderByCreateTimeDesc(support.getSupportId());
        boolean refresh = false;
        if (null != lastCommunicationId && !CollectionUtils.isEmpty(communicationList)) {
            refresh = !communicationList.get(0).getCommunicationId().equals(lastCommunicationId);
        }
        return new SupportCommunicationHistoryResponse()
                .setAppId(support.getAppId())
                .setRefresh(refresh)
                .setCommunicationList(communicationList.stream()
                        .map(SupportServiceImpl::toCommunicationDto)
                        .toList()
                );
    }

    private void updateInternal(SupportEntity supportEntity) {
        int successRow = supportMapper.updateById(supportEntity);
        if (successRow != 1) {
            throw new BusinessException("更新工单失败");
        }
    }

    private static SupportCommunicationDto toCommunicationDto(SupportCommunicationEntity entity) {
        return new SupportCommunicationDto()
                .setCommunicationId(entity.getCommunicationId())
                .setSenderUserId(entity.getSenderId())
                .setSenderUserName(entity.getSenderName())
                .setReceiverUserId(entity.getReceiverId())
                .setReceiverUserName(entity.getReceiverName())
                .setContent(entity.getContent())
                .setCreateTime(entity.getCreateTime());
    }
}
