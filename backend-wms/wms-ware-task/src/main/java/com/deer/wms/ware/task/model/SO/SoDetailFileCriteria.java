package com.deer.wms.ware.task.model.SO;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/03.
 */
@ToString
public class SoDetailFileCriteria extends QueryCriteria {
    private Integer soDetailFileId;
    private String billNo;
    private String detailNo;
    private Integer state;
    private String itemCode;
    private Integer packDetailId;
    private String mergeNo;

    public Integer getSoDetailFileId() {
        return soDetailFileId;
    }

    public void setSoDetailFileId(Integer soDetailFileId) {
        this.soDetailFileId = soDetailFileId;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public String getMergeNo() {
        return mergeNo;
    }

    public void setMergeNo(String mergeNo) {
        this.mergeNo = mergeNo;
    }
}
