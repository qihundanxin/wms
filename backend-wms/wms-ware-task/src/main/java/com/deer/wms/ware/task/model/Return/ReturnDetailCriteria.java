package com.deer.wms.ware.task.model.Return;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/13.
 */
@ToString
public class ReturnDetailCriteria extends QueryCriteria {

    private String billNo;
    private String detailNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDetailNo() {
        return detailNo;
    }

    public void setDetailNo(String detailNo) {
        this.detailNo = detailNo;
    }
}
