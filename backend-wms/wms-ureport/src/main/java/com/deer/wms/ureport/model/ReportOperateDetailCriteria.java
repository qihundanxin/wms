package com.deer.wms.ureport.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2021/10/27.
 */
@ToString
public class ReportOperateDetailCriteria extends QueryCriteria {
    private Integer reportOperateId;
    private String itemCode;
    private String userName;
    private Integer userId;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getReportOperateId() {
        return reportOperateId;
    }

    public void setReportOperateId(Integer reportOperateId) {
        this.reportOperateId = reportOperateId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
