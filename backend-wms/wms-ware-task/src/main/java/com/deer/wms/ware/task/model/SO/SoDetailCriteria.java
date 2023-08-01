package com.deer.wms.ware.task.model.SO;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/02/02.
 */
@ToString
public class SoDetailCriteria extends QueryCriteria {

    private Integer soDetailId;
    private String billNo;
    private String detailNo;
    private Integer state;
    private String itemCode;
    private Integer packDetailId;
    private String mergeNo;
    private Integer customerId;
    private String packDescribe;
    private String startActualDeliveryTime;
    private String endActualDeliveryTime;
    private Integer bomSplit;
    private String allotDetailNo;

    private List<String> billNos;

    private Integer wareId;


    private Integer masterState;

    public Integer getMasterState() {
        return masterState;
    }

    public void setMasterState(Integer masterState) {
        this.masterState = masterState;
    }

    @Override
    public Integer getWareId() {
        return wareId;
    }

    @Override
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
    }

    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    private String startCreateTime;
    private String endCreateTime;


    public Integer getBomSplit() {
        return bomSplit;
    }

    public void setBomSplit(Integer bomSplit) {
        this.bomSplit = bomSplit;
    }

    public Integer getSoDetailId() {
        return soDetailId;
    }

    public void setSoDetailId(Integer soDetailId) {
        this.soDetailId = soDetailId;
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

    public String getAllotDetailNo() {
        return allotDetailNo;
    }

    public void setAllotDetailNo(String allotDetailNo) {
        this.allotDetailNo = allotDetailNo;
    }
}
