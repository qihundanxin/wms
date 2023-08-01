package com.deer.wms.ASN.manage.model.buy;

import java.util.List;

import lombok.ToString;

@ToString
public class InsertData {

    private RequestBuyDto requestBuy;
    private List<RequestBuyDetail> requestBuyDetails;

    private Integer insertType; //0-提交  -2保存为草稿
    private Integer billSource; //0-公司新建  2-供应商新建

    public RequestBuyDto getRequestBuy() {
        return requestBuy;
    }

    public void setRequestBuy(RequestBuyDto requestBuy) {
        this.requestBuy = requestBuy;
    }

    public List<RequestBuyDetail> getRequestBuyDetails() {
        return requestBuyDetails;
    }

    public void setRequestBuyDetails(List<RequestBuyDetail> requestBuyDetails) {
        this.requestBuyDetails = requestBuyDetails;
    }

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getBillSource() {
        return billSource;
    }

    public void setBillSource(Integer billSource) {
        this.billSource = billSource;
    }
}
