package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.ApiStatusEnum;
import com.dsj.csp.common.enums.DocStatusEnum;
import com.dsj.csp.manage.biz.DocBizService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-20
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class DocBizServiceImpl implements DocBizService {
    
    private final AbilityApiService abilityApiService;
    private final DocService docService;
    
    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditSubmit(Long docId) {
        boolean isValid = isValidJudge(docId, DocStatusEnum.NOT_SUBMIT.getCode());
        if (!isValid){
            throw new BusinessException("只有'待提交'的文档才能提交,请刷新后重试!");
        }
        docService.lambdaUpdate().eq(DocEntity::getDocId, docId)
                // 状态0: 待审核
                .set(DocEntity::getStatus, DocStatusEnum.WAIT_AUDIT.getCode())
                .set(DocEntity::getUpdateTime, new Date())
                .update();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditWithdraw(Long docId) {
        boolean isValid = isValidJudge(docId, DocStatusEnum.WAIT_AUDIT.getCode());
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能撤回,请刷新后重试!");
        }
        docService.lambdaUpdate().eq(DocEntity::getDocId, docId)
                // 状态6: 未提交
                .set(DocEntity::getStatus, DocStatusEnum.NOT_SUBMIT.getCode())
                .set(DocEntity::getUpdateTime, new Date())
                .update();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditPass(Long docId, String note) {
        boolean isValid = isValidJudge(docId, DocStatusEnum.WAIT_AUDIT.getCode());
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能审核通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        // 状态1: 审核通过
        updateWrapper.set(DocEntity::getStatus, DocStatusEnum.PASSED.getCode());
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditNotPass(Long docId, String note) {
        boolean isValid = isValidJudge(docId, DocStatusEnum.WAIT_AUDIT.getCode());
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能审核不通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, DocStatusEnum.NOT_PASSED.getCode());
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditPublish(Long docId, String note) {
        DocEntity docEntity = docService.getById(docId);
        if (docEntity==null){
            throw new BusinessException("审核失败! 文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!=DocStatusEnum.PASSED.getCode()){
            throw new BusinessException("只有'审核通过'的文档才能发布,请刷新后重试!");
        }
        // 文档关联的接口是否存在并且已发布
        if (docEntity.getApiId()!=null){
            AbilityApiEntity api = abilityApiService.getById(docEntity.getApiId());
            if (api==null || api.getStatus()!= ApiStatusEnum.PUBLISHED.getCode()){
                throw new BusinessException("文档关联的接口不存在,或者还未发布！");
            }
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, DocStatusEnum.PUBLISHED.getCode());
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        updateWrapper.set(DocEntity::getSubmitTime, new Date());
        docService.update(updateWrapper);
    }

//    @Override
//    public void auditOnline(Long docId, String note) {
//        boolean isValid = isValidJudge(docId, 3);
//        if (!isValid){
//            throw new BusinessException("只有'已发布'的文档才能上线,请刷新后重试!");
//        }
//        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
//        updateWrapper.eq(DocEntity::getDocId, docId);
//        updateWrapper.set(DocEntity::getStatus, 4);
//        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
//        updateWrapper.set(DocEntity::getOperator, operatorName);
//        updateWrapper.set(DocEntity::getApproveTime, new Date());
//        docService.update(updateWrapper);
//    }

    @Override
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public void auditOffline(Long docId, String note) {
        boolean isValid = isValidJudge(docId, DocStatusEnum.PUBLISHED.getCode());
        if (!isValid){
            throw new BusinessException("只有'已发布的文档才能下线,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, DocStatusEnum.OFFLINE.getCode());
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getSubmitTime, null);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        docService.update(updateWrapper);
    }

    /**
     * 审核操作前的判断,
     * @param docId 文档ID
     * @param targetPrevStatus 期待的前任审核状态
     * @return 审核操作是否有效
     */
    public boolean isValidJudge(Long docId, int targetPrevStatus){
        // 审核文档是否还存在
        DocEntity docEntity = docService.getById(docId);
        if (docEntity==null){
            throw new BusinessException("审核失败! 文档不存在,请刷新页面后重试...");
        }
        // 审核操作是否有效
        return docEntity.getStatus()==targetPrevStatus;
    }

}
