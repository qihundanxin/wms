package com.deer.wms.ware.task.model.Return;

import java.util.List;

import lombok.ToString;

@ToString
public class ReturnData {
    private ReturnMaster returnMaster;
    private List<ReturnDetail> returnDetails;
    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource;  //单据来源

    public ReturnMaster getReturnMaster() {
        return returnMaster;
    }

    public void setReturnMaster(ReturnMaster returnMaster) {
        this.returnMaster = returnMaster;
    }

    public List<ReturnDetail> getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(List<ReturnDetail> returnDetails) {
        this.returnDetails = returnDetails;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }
}
