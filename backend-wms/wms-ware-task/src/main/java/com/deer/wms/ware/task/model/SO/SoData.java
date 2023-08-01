package com.deer.wms.ware.task.model.SO;

import java.util.List;

import lombok.ToString;

@ToString
public class SoData {
    private SoMaster soMaster;
    private List<SoDetail> soDetails;
    private Integer insertType;  //0-提交  -2保存为草稿
    private Integer billSource; //单据来源 1-新建  2-合并 3-调拨生成

    public SoMaster getSoMaster() {
        return soMaster;
    }

    public void setSoMaster(SoMaster soMaster) {
        this.soMaster = soMaster;
    }

    public List<SoDetail> getSoDetails() {
        return soDetails;
    }

    public void setSoDetails(List<SoDetail> soDetails) {
        this.soDetails = soDetails;
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
