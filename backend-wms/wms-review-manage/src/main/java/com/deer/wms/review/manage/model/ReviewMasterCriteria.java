package com.deer.wms.review.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2018/10/24.
 */
@ToString
public class ReviewMasterCriteria extends QueryCriteria {

    private Integer billTypeId;

    public Integer getBillTypeId() {
        return billTypeId;
    }

    public void setBillTypeId(Integer billTypeId) {
        this.billTypeId = billTypeId;
    }
}
