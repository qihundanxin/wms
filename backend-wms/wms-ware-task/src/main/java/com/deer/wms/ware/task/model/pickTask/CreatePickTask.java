package com.deer.wms.ware.task.model.pickTask;

import java.util.List;

import lombok.ToString;

@ToString
public class CreatePickTask {
    private String billNo;
    private Integer soDetailId;
    private List<CreatePickTaskDetail> createPickTaskDetails;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
    }

    public List<CreatePickTaskDetail> getCreatePickTaskDetails() {
        return createPickTaskDetails;
    }

    public void setCreatePickTaskDetails(List<CreatePickTaskDetail> createPickTaskDetails) {
        this.createPickTaskDetails = createPickTaskDetails;
    }
}
