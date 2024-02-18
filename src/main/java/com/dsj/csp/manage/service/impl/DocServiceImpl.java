package com.dsj.csp.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.mapper.DocMapper;
import com.dsj.csp.manage.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DocServiceImpl extends ServiceImpl<DocMapper, DocEntity> implements DocService {
    @Override
    public void auditPass(Long docId, String note, String operator) {
        DocEntity docEntity = this.getById(docId);
        if (docEntity==null){
            throw new BusinessException("审核失败! 文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!=0){
            throw new BusinessException("只有'待审核'的文档才能审核通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 1);
        updateWrapper.set(ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getOperator, operator);
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        this.update(updateWrapper);
    }

    @Override
    public void auditNotPass(Long docId, String note, String operatorName) {
        DocEntity docEntity = this.getById(docId);
        if (docEntity==null){
            throw new BusinessException("审核失败! 文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!=0){
            throw new BusinessException("只有'待审核'的文档才能审核不通过,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 2);
        updateWrapper.set(ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getOperator, operatorName);
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        this.update(updateWrapper);
    }

    @Override
    public void auditPublish(Long docId, String note, String operatorName) {
        DocEntity docEntity = this.getById(docId);
        if (docEntity==null){
            throw new BusinessException("发布失败!文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!=1){
            throw new BusinessException("只有'审核通过'的文档才能发布,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 3);
        updateWrapper.set(ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getOperator, operatorName);
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        this.update(updateWrapper);
    }

    @Override
    public void auditOnline(Long docId, String note, String operatorName) {
        DocEntity docEntity = this.getById(docId);
        if (docEntity==null){
            throw new BusinessException("上线失败!文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!=3){
            throw new BusinessException("只有'已发布'的文档才能上线,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 4);
        updateWrapper.set(ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getOperator, operatorName);
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        this.update(updateWrapper);
    }

    @Override
    public void auditOffline(Long docId, String note, String operatorName) {
        DocEntity docEntity = this.getById(docId);
        if (docEntity==null){
            throw new BusinessException("下线失败!文档不存在,请刷新页面后重试...");
        }
        if (docEntity.getStatus()!= 3 && docEntity.getStatus()!= 4){
            throw new BusinessException("只有'已发布'的文档才能下线,请刷新后重试!");
        }
        LambdaUpdateWrapper<DocEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DocEntity::getDocId, docId);
        updateWrapper.set(DocEntity::getStatus, 5);
        updateWrapper.set(ObjectUtil.isEmpty(note), DocEntity::getNote, note);
        updateWrapper.set(DocEntity::getOperator, operatorName);
        updateWrapper.set(DocEntity::getApproveTime, new Date());
        this.update(updateWrapper);
    }


}
