package com.deer.wms.ASN.manage.model.buy;

import lombok.ToString;

import java.util.List;

@ToString
public class UpdateData {

    private RequestBuyDto requestBuy;
    private List<RequestBuyDetailDto> requestBuyDetails;

    private Integer insertType; //0-提交  -2保存为草稿
    private Integer billSource; //0-公司新建  2-供应商新建

    public RequestBuyDto getRequestBuy() {
        return requestBuy;
    }

    public void setRequestBuy(RequestBuyDto requestBuy) {
        this.requestBuy = requestBuy;
    }

    public List<RequestBuyDetailDto> getRequestBuyDetails() {
        return requestBuyDetails;
    }

    public void setRequestBuyDetails(List<RequestBuyDetailDto> requestBuyDetails) {
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
