package com.deer.wms.ware.task.model.Allot;

import java.util.List;

import lombok.ToString;

@ToString
public class AllotData {
    private AllotMaster allotMaster;
    private List<AllotDetail> allotDetails;
    private String allotBillNo;
    private Integer insertType;  //0-提交  -2保存为草稿

    public String getAllotBillNo() {
        return allotBillNo;
    }

    public void setAllotBillNo(String allotBillNo) {
        this.allotBillNo = allotBillNo;
    }

    public AllotMaster getAllotMaster() {
        return allotMaster;
    }

    public void setAllotMaster(AllotMaster allotMaster) {
        this.allotMaster = allotMaster;
    }

    public List<AllotDetail> getAllotDetails() {
        return allotDetails;
    }

    public void setAllotDetails(List<AllotDetail> allotDetails) {
        this.allotDetails = allotDetails;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }
}
