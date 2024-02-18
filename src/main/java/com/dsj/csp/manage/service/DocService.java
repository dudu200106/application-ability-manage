package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.DocEntity;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
public interface DocService extends IService<DocEntity> {
    void auditPass(Long docId, String note, String operator);

    void auditNotPass(Long docId, String note, String operatorName);

    void auditPublish(Long docId, String note, String operatorName);

    void auditOnline(Long docId, String note, String operatorName);

    void auditOffline(Long docId, String note, String operatorName);
}
