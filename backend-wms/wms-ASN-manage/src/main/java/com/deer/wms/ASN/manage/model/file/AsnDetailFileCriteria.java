package com.deer.wms.ASN.manage.model.file;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/07.
 */
@ToString
public class AsnDetailFileCriteria extends QueryCriteria {
    private Integer asnDetailFileId;
    private String billNo;
    private String detailNo;
    private String requestDetailDetailNo;
    private Integer state;
    private String itemCode;
    private String itemName;
    private Integer packDetailId;
    private Double expectQuantity;
    private Double acceptQuantity;
    private String acceptTime;
    private Integer supplierId;

    public Integer getAsnDetailFileId() {
        return asnDetailFileId;
    }

    public void setAsnDetailFileId(Integer asnDetailFileId) {
        this.asnDetailFileId = asnDetailFileId;
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
}
