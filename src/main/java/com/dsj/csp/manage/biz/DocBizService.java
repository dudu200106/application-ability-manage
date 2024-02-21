package com.dsj.csp.manage.biz;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-20
 */
public interface DocBizService {
    /**
     * 提交文档
     * @param docId
     */
    void auditSubmit(Long docId);

    /**
     * 撤回文档
     * @param docId
     */
    void auditWithdraw(Long docId);

    void auditPass(Long docId, String note);

    void auditNotPass(Long docId, String note);

    void auditPublish(Long docId, String note);

    // 取消'已上线'状态
//    void auditOnline(Long docId, String note);

    void auditOffline(Long docId, String note);

}
