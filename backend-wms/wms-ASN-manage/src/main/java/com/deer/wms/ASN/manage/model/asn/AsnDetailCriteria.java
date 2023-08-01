package com.deer.wms.ASN.manage.model.asn;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/03.
 */
@ToString
public class AsnDetailCriteria extends QueryCriteria {
    private Integer asnDetailId;
    private String billNo;
    private String detailNo;
    private String requestDetailDetailNo;
    private Integer state;
    private Integer qcState;
    private String itemCode;
    private String itemName;
    private Integer packDetailId;
    private Double expectQuantity;
    private Double acceptQuantity;
    private String acceptTime;
    private Integer supplierId;
    private String supplierCode;
    private Integer deliveryState;
    private Integer isAccept;

    private List<Integer> asnTypes;  //0  2

    private List<Integer> states;

    private String allotDetailNo;

    public Integer getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(Integer isAccept) {
        this.isAccept = isAccept;
    }

    public Integer getAsnDetailId() {
        return asnDetailId;
    }

    public void setAsnDetailId(Integer asnDetailId) {
        this.asnDetailId = asnDetailId;
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

    public String getRequestDetailDetailNo() {
        return requestDetailDetailNo;
    }

    public void setRequestDetailDetailNo(String requestDetailDetailNo) {
        this.requestDetailDetailNo = requestDetailDetailNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getQcState() {
        return qcState;
    }

    public void setQcState(Integer qcState) {
        this.qcState = qcState;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPackDetailId() {
        return packDetailId;
    }

    public void setPackDetailId(Integer packDetailId) {
        this.packDetailId = packDetailId;
    }

    public Double getExpectQuantity() {
        return expectQuantity;
    }

    public void setExpectQuantity(Double expectQuantity) {
        this.expectQuantity = expectQuantity;
    }

    public Double getAcceptQuantity() {
        return acceptQuantity;
    }

    public void setAcceptQuantity(Double acceptQuantity) {
        this.acceptQuantity = acceptQuantity;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public List<Integer> getAsnTypes() {
        return asnTypes;
    }

    public void setAsnTypes(List<Integer> asnTypes) {
        this.asnTypes = asnTypes;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Integer getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(Integer deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getAllotDetailNo() {
        return allotDetailNo;
    }

    public void setAllotDetailNo(String allotDetailNo) {
        this.allotDetailNo = allotDetailNo;
    }

    public List<Integer> getStates() {
        return states;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }
}
