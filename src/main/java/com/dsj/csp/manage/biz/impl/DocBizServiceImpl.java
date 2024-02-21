package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.DocBizService;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.DocService;
import lombok.RequiredArgsConstructor;
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
    public void auditSubmit(Long docId) {
        boolean isValid = isValidJudge(docId, 6);
        if (!isValid){
            throw new BusinessException("只有'待提交'的文档才能提交,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        // 状态0: 待审核
        updateWrapper.set(DocEntity::getStatus, 0);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    public void auditWithdraw(Long docId) {
        boolean isValid = isValidJudge(docId, 0);
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能撤回,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        // 状态6: 未提交
        updateWrapper.set(DocEntity::getStatus, 6);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    public void auditPass(Long docId, String note) {
        boolean isValid = isValidJudge(docId, 0);
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能审核通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        // 状态1: 审核通过
        updateWrapper.set(DocEntity::getStatus, 1);
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    public void auditNotPass(Long docId, String note) {
        boolean isValid = isValidJudge(docId, 0);
        if (!isValid){
            throw new BusinessException("只有'待审核'的文档才能审核不通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 2);
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getUpdateTime, new Date());
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        docService.update(updateWrapper);
    }

    @Override
    public void auditPublish(Long docId, String note) {
        boolean isValid = isValidJudge(docId, 1);
        if (!isValid){
            throw new BusinessException("只有'审核通过'的文档才能发布,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 3);
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
    public void auditOffline(Long docId, String note) {
        boolean isValid = isValidJudge(docId, 3) || isValidJudge(docId, 4);
        if (!isValid){
            throw new BusinessException("只有'已发布'或'已上线'的文档才能下线,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 5);
        updateWrapper.set(!ObjectUtil.isEmpty(note), DocEntity::getNote, note);
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
//        // 文档关联的接口是否存在并且已发布
//        AbilityApiEntity api = abilityApiService.getById(docEntity.getApiId());
//        if (api==null || api.getStatus()!=4){
//            throw new BusinessException("申请的接口不存在了，或已下线！");
//        }
        // 审核操作是否有效
        return docEntity.getStatus()==targetPrevStatus;
    }

}
