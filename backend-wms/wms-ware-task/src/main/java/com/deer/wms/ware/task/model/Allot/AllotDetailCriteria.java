package com.deer.wms.ware.task.model.Allot;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/04.
 */
@ToString
public class AllotDetailCriteria extends QueryCriteria {
    private Integer allotDetailId;
    private String billNo;
    private String detailNo;
    private Integer state;
    private String itemCode;
    private Integer packDetailId;
    private Integer customerId;
    private String packDescribe;
    private String startActualDeliveryTime;
    private String endActualDeliveryTime;

    public Integer getAllotDetailId() {
        return allotDetailId;
    }

    public void setAllotDetailId(Integer allotDetailId) {
        this.allotDetailId = allotDetailId;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getPackDescribe() {
        return packDescribe;
    }

    public void setPackDescribe(String packDescribe) {
        this.packDescribe = packDescribe;
    }

    public String getStartActualDeliveryTime() {
        return startActualDeliveryTime;
    }

    public void setStartActualDeliveryTime(String startActualDeliveryTime) {
        this.startActualDeliveryTime = startActualDeliveryTime;
    }

    public String getEndActualDeliveryTime() {
        return endActualDeliveryTime;
    }

    public void setEndActualDeliveryTime(String endActualDeliveryTime) {
        this.endActualDeliveryTime = endActualDeliveryTime;
    }
}
