package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.request.SupportAcceptRequest;
import com.dsj.csp.manage.dto.request.SupportQueryRequest;
import com.dsj.csp.manage.dto.request.SupportReplyRequest;
import com.dsj.csp.manage.dto.request.SupportUpdateRequest;
import com.dsj.csp.manage.dto.response.SupportCommunicationHistoryResponse;
import com.dsj.csp.manage.entity.SupportEntity;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 10:39
 */
public interface SupportService {
    Page<SupportEntity> selectSupportList(SupportQueryRequest request);

    SupportEntity selectSupportById(Long supportId);

    SupportEntity acceptSupport(Long supportId, SupportAcceptRequest request);

    SupportEntity updateSupport(Long supportId, SupportUpdateRequest request);

    SupportEntity supportFinish(Long supportId);

    void deleteSupportById(Long supportId);

    SupportCommunicationHistoryResponse replySupport(Long supportId, SupportReplyRequest request);

    SupportCommunicationHistoryResponse getCommunicationBySupportId(Long supportId, Long lastCommunicationId);
}
