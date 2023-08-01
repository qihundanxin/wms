package com.deer.wms.ware.task.model.Load;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/17.
 */
@ToString
public class LoadDetailCriteria extends QueryCriteria {
    private String loadMasterCode;
    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getLoadMasterCode() {
        return loadMasterCode;
    }

    public void setLoadMasterCode(String loadMasterCode) {
        this.loadMasterCode = loadMasterCode;
    }
}
