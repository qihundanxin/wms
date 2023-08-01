package com.deer.wms.ASN.manage.model.in;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.ItemBatch;

import lombok.ToString;

@ToString
public class AcceptInsert extends ItemBatch {
    private Integer wareId;
    private String wareName;
    private String organizationName;
    private String itemName;
    private Integer toPackDetailId;
    private String toPackDescribe;
    private Double toTransRatio;
    private Double expectQuantity; //预期数量
    private Double toQuantity;  //收货数量
    private String toBoxCode; //调入追踪号 -验收记录、事务用到
    private String toCellCode; //调入货位  -验收记录、事务用到

    private Integer acceptUserId;
    private String acceptUserName;
    private String acceptTime;

    private Integer billType; //0-默认验收 1-采购入库  2-ASN单 3-盲收 4-冻结  5-扫描验收  6-异常验收

    private String asnBillNo;
    private String asnDetailNo;
    private String requestDetailDetailNo;
    private String upc;

    private String shipBillNo;

    private Integer acceptExId;
    private Boolean autoPrint;
    private Boolean baoKuan;

    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Boolean getBaoKuan() {
        return baoKuan;
    }

    public void setBaoKuan(Boolean baoKuan) {
        this.baoKuan = baoKuan;
    }

    public Boolean getAutoPrint() {
        return autoPrint;
    }

    public void setAutoPrint(Boolean autoPrint) {
        this.autoPrint = autoPrint;
    }

    public Integer getAcceptExId() {
        return acceptExId;
    }

    public void setAcceptExId(Integer acceptExId) {
        this.acceptExId = acceptExId;
    }

    public String getShipBillNo() {
        return shipBillNo;
    }

    public void setShipBillNo(String shipBillNo) {
        this.shipBillNo = shipBillNo;
    }

    private List<String> serialNos;

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Double getToTransRatio() {
        return toTransRatio;
    }

    public void setToTransRatio(Double toTransRatio) {
        this.toTransRatio = toTransRatio;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(Integer acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public Integer getToPackDetailId() {
        return toPackDetailId;
    }

    public void setToPackDetailId(Integer toPackDetailId) {
        this.toPackDetailId = toPackDetailId;
    }

    public String getToPackDescribe() {
        return toPackDescribe;
    }

    public void setToPackDescribe(String toPackDescribe) {
        this.toPackDescribe = toPackDescribe;
    }

    public Double getExpectQuantity() {
        return expectQuantity;
    }

    public void setExpectQuantity(Double expectQuantity) {
        this.expectQuantity = expectQuantity;
    }

    public Double getToQuantity() {
        return toQuantity;
    }

    public void setToQuantity(Double toQuantity) {
        this.toQuantity = toQuantity;
    }

    public String getToBoxCode() {
        return toBoxCode;
    }

    public void setToBoxCode(String toBoxCode) {
        this.toBoxCode = toBoxCode;
    }

    public String getToCellCode() {
        return toCellCode;
    }

    public void setToCellCode(String toCellCode) {
        this.toCellCode = toCellCode;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAsnBillNo() {
        return asnBillNo;
    }

    public void setAsnBillNo(String asnBillNo) {
        this.asnBillNo = asnBillNo;
    }

    public String getAsnDetailNo() {
        return asnDetailNo;
    }

    public void setAsnDetailNo(String asnDetailNo) {
        this.asnDetailNo = asnDetailNo;
    }

    public String getRequestDetailDetailNo() {
        return requestDetailDetailNo;
    }

    public void setRequestDetailDetailNo(String requestDetailDetailNo) {
        this.requestDetailDetailNo = requestDetailDetailNo;
    }

    public List<String> getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(List<String> serialNos) {
        this.serialNos = serialNos;
    }

}
