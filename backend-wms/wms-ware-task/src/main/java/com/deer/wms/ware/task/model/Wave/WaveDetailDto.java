package com.deer.wms.ware.task.model.Wave;

import lombok.ToString;

@ToString
public class WaveDetailDto extends WaveDetail {

    private String billNo;
    private String wareName;
    private String organizationName;
    private String customerName;
    private String createTime;
    private String createUserName;
    private String deliveryTime;
    private String expectTimeFm;
    private String expectTimeTo;
    private String memo;
    private String carrierName;
    private Integer state;
    private Integer organizationId;
    private Integer customerId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getExpectTimeFm() {
        return expectTimeFm;
    }

    public void setExpectTimeFm(String expectTimeFm) {
        this.expectTimeFm = expectTimeFm;
    }

    public String getExpectTimeTo() {
        return expectTimeTo;
    }

    public void setExpectTimeTo(String expectTimeTo) {
        this.expectTimeTo = expectTimeTo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
