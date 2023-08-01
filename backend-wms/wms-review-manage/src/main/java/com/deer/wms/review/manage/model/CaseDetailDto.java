package com.deer.wms.review.manage.model;

import java.util.List;

import lombok.ToString;

@ToString
public class CaseDetailDto extends CaseDetail {
    private String billNo;
    private String createTime;

    private List<CaseUser> caseUsers;

    public List<CaseUser> getCaseUsers() {
        return caseUsers;
    }

    public void setCaseUsers(List<CaseUser> caseUsers) {
        this.caseUsers = caseUsers;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

}
