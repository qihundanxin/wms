package com.deer.wms.ware.task.model.Allot;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/03/04.
 */
@ToString
public class AllotMasterCriteria extends QueryCriteria {
    private Integer allotMasterId;
    private String billNo;
    private Integer state;
    private Integer caseState;
    private Integer customerId;
    private Integer createUserId;
    private Integer insertType;
    private Integer receivingAddressId;
    private Integer carrierId;
    private List<String> billNos;

    private Integer fromWareId;
    private Integer toWareId;

    private String startCreateTime;
    private String endCreateTime;
    private String startDeliveryTime;
    private String endDeliveryTime;
    private String startExpectTimeFm;
    private String endExpectTimeFm;
    private String startExpectTimeTo;
    private String endExpectTimeTo;

    /**
     * 优先级
     */
    private Integer orderNo;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getAllotMasterId() {
        return allotMasterId;
    }

    public void setAllotMasterId(Integer allotMasterId) {
        this.allotMasterId = allotMasterId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCaseState() {
        return caseState;
    }

    public void setCaseState(Integer caseState) {
        this.caseState = caseState;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getReceivingAddressId() {
        return receivingAddressId;
    }

    public void setReceivingAddressId(Integer receivingAddressId) {
        this.receivingAddressId = receivingAddressId;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
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

    public String getStartDeliveryTime() {
        return startDeliveryTime;
    }

    public void setStartDeliveryTime(String startDeliveryTime) {
        this.startDeliveryTime = startDeliveryTime;
    }

    public String getEndDeliveryTime() {
        return endDeliveryTime;
    }

    public void setEndDeliveryTime(String endDeliveryTime) {
        this.endDeliveryTime = endDeliveryTime;
    }

    public String getStartExpectTimeFm() {
        return startExpectTimeFm;
    }

    public void setStartExpectTimeFm(String startExpectTimeFm) {
        this.startExpectTimeFm = startExpectTimeFm;
    }

    public String getEndExpectTimeFm() {
        return endExpectTimeFm;
    }

    public void setEndExpectTimeFm(String endExpectTimeFm) {
        this.endExpectTimeFm = endExpectTimeFm;
    }

    public String getStartExpectTimeTo() {
        return startExpectTimeTo;
    }

    public void setStartExpectTimeTo(String startExpectTimeTo) {
        this.startExpectTimeTo = startExpectTimeTo;
    }

    public String getEndExpectTimeTo() {
        return endExpectTimeTo;
    }

    public void setEndExpectTimeTo(String endExpectTimeTo) {
        this.endExpectTimeTo = endExpectTimeTo;
    }

    public Integer getFromWareId() {
        return fromWareId;
    }

    public void setFromWareId(Integer fromWareId) {
        this.fromWareId = fromWareId;
    }

    public Integer getToWareId() {
        return toWareId;
    }

    public void setToWareId(Integer toWareId) {
        this.toWareId = toWareId;
    }
}
