package com.deer.wms.ASN.manage.model.buy;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2020/01/02.
 */
@ToString
public class RequestBuyCriteria extends QueryCriteria {
    private Integer requestBuyId;
    private String billNo;
    private Integer buyType;
    private Integer state;
    private Integer acceptState;
    private Integer supplierId;
    private Integer createUserId;
    private String createTime;
    private String expectTime;
    private Integer insertType;
    private Integer billSource;

    private String startCreateTime;
    private String endCreateTime;
    private String startExpectTime;
    private String endExpectTime;

    /**
     * 预期到货时间排序 ASC 时间正序，DESC 时间倒序
     */
    private String expectTimeSort;

    public String getExpectTimeSort() {
        return expectTimeSort;
    }

    public void setExpectTimeSort(String expectTimeSort) {
        this.expectTimeSort = expectTimeSort;
    }

    public Integer getRequestBuyId() {
        return requestBuyId;
    }

    public void setRequestBuyId(Integer requestBuyId) {
        this.requestBuyId = requestBuyId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getBuyType() {
        return buyType;
    }

    public void setBuyType(Integer buyType) {
        this.buyType = buyType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(Integer acceptState) {
        this.acceptState = acceptState;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
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

    public String getStartExpectTime() {
        return startExpectTime;
    }

    public void setStartExpectTime(String startExpectTime) {
        this.startExpectTime = startExpectTime;
    }

    public String getEndExpectTime() {
        return endExpectTime;
    }

    public void setEndExpectTime(String endExpectTime) {
        this.endExpectTime = endExpectTime;
    }
}
