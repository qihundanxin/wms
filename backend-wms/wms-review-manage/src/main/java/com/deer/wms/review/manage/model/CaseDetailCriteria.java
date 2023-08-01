package com.deer.wms.review.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2018/10/24.
 */
@ToString
public class CaseDetailCriteria extends QueryCriteria {
    private Integer caseDetailId;
    private Integer nodeUserId;
    private Integer caseMasterId;
    private Integer reviewNodeId;
    private String billNo;
    private Integer userId;

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getReviewNodeId() {
        return reviewNodeId;
    }

    public void setReviewNodeId(Integer reviewNodeId) {
        this.reviewNodeId = reviewNodeId;
    }

    public Integer getCaseMasterId() {
        return caseMasterId;
    }

    public void setCaseMasterId(Integer caseMasterId) {
        this.caseMasterId = caseMasterId;
    }

    public Integer getNodeUserId() {
        return nodeUserId;
    }

    public void setNodeUserId(Integer nodeUserId) {
        this.nodeUserId = nodeUserId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
