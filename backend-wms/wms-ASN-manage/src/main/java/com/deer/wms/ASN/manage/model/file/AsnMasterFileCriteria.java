package com.deer.wms.ASN.manage.model.file;

import javax.persistence.Column;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/07.
 */
@ToString
public class AsnMasterFileCriteria extends QueryCriteria {
    private Integer asnMasterFileId;
    private String billNo;
    private Integer billType;
    private String requestBuyBillNo;
    private Integer state;
    private Integer asnState;
    private Integer orderNo;
    private String createTime;
    private Integer createUserId;
    private String createUserName;
    private Integer acceptState;
    private String expectTime;
    private String asnUdfHs1;
    private String asnUdfHs2;
    private String asnUdfHs3;
    private Integer supplierId;
    private String carrierCode;
    private Integer qualityState;
    private String platformCode;
    private String salesman;
    private String insertType;
    private String carrierName;
    private String linkMan;
    private String linkPhone;
    private Integer type;
    private String carType;
    private String carNo;
    private String driver;
    private String driverPhone;
    private Integer asnType;

    /**
     * 归档时间
     */
    @Column(name = "file_time")
    private String fileTime;

    /**
     * 归档人ID
     */
    @Column(name = "file_user_id")
    private Integer fileUserId;

    /**
     * 归档人
     */
    @Column(name = "file_user_name")
    private String fileUserName;

    private Integer billSource;

    private String memo;

    private Integer isSerialNo;

    private Integer isPrint;

    public Integer getAsnMasterFileId() {
        return asnMasterFileId;
    }

    public void setAsnMasterFileId(Integer asnMasterFileId) {
        this.asnMasterFileId = asnMasterFileId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getRequestBuyBillNo() {
        return requestBuyBillNo;
    }

    public void setRequestBuyBillNo(String requestBuyBillNo) {
        this.requestBuyBillNo = requestBuyBillNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAsnState() {
        return asnState;
    }

    public void setAsnState(Integer asnState) {
        this.asnState = asnState;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(Integer acceptState) {
        this.acceptState = acceptState;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getAsnUdfHs1() {
        return asnUdfHs1;
    }

    public void setAsnUdfHs1(String asnUdfHs1) {
        this.asnUdfHs1 = asnUdfHs1;
    }

    public String getAsnUdfHs2() {
        return asnUdfHs2;
    }

    public void setAsnUdfHs2(String asnUdfHs2) {
        this.asnUdfHs2 = asnUdfHs2;
    }

    public String getAsnUdfHs3() {
        return asnUdfHs3;
    }

    public void setAsnUdfHs3(String asnUdfHs3) {
        this.asnUdfHs3 = asnUdfHs3;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public Integer getQualityState() {
        return qualityState;
    }

    public void setQualityState(Integer qualityState) {
        this.qualityState = qualityState;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }

    public String getInsertType() {
        return insertType;
    }

    public void setInsertType(String insertType) {
        this.insertType = insertType;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public Integer getFileUserId() {
        return fileUserId;
    }

    public void setFileUserId(Integer fileUserId) {
        this.fileUserId = fileUserId;
    }

    public String getFileUserName() {
        return fileUserName;
    }

    public void setFileUserName(String fileUserName) {
        this.fileUserName = fileUserName;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsSerialNo() {
        return isSerialNo;
    }

    public void setIsSerialNo(Integer isSerialNo) {
        this.isSerialNo = isSerialNo;
    }

    public Integer getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Integer isPrint) {
        this.isPrint = isPrint;
    }

    public Integer getAsnType() {
        return asnType;
    }

    public void setAsnType(Integer asnType) {
        this.asnType = asnType;
    }
}
