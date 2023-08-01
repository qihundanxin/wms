package com.deer.wms.ASN.manage.model.buy;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/02.
 */
@ToString
public class RequestBuyDetailCriteria extends QueryCriteria {
    private Integer requestBuyDetailId;
    private String billNo;
    private String detailNo;
    private String itemCode;
    private Integer packDetailId;
    private Integer state;
    private String acceptTime;
    private String batchTacticCode;
    private Double expectQuantity;
    private Double acceptQuantity;

    public Integer getRequestBuyDetailId() {
        return requestBuyDetailId;
    }

    public void setRequestBuyDetailId(Integer requestBuyDetailId) {
        this.requestBuyDetailId = requestBuyDetailId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
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

}
