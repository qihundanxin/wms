package com.deer.wms.ware.task.model.Box;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/15.
 */
@ToString
public class BoxDetailCriteria extends QueryCriteria {
    private Integer boxDetailId;
    private String BillNo;
    private String itemCode;
    private String boxNo;
    private Integer packDetailId;

    private Integer soDetailId;
    private Integer soMasterId;
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBoxDetailId() {
        return boxDetailId;
    }

    public void setBoxDetailId(Integer boxDetailId) {
        this.boxDetailId = boxDetailId;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBoxNo() {
        return boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
    }

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }
}
