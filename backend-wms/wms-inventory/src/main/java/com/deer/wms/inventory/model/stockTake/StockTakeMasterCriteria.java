package com.deer.wms.inventory.model.stockTake;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by 郭靖勋 on 2020/02/06.
 */
@ToString
public class StockTakeMasterCriteria extends QueryCriteria {
    private Integer stockTakeMasterId;
    private String billNo;
    private Integer type;
    private Integer state;
    private Integer priority;
    private Integer createUserId;
    private String itemCode;
    private String boxCode;
    private Integer ifRandom;
    private List<Integer> ids;
    private Integer caseState;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getStockTakeMasterId() {
        return stockTakeMasterId;
    }

    public void setStockTakeMasterId(Integer stockTakeMasterId) {
        this.stockTakeMasterId = stockTakeMasterId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public Integer getIfRandom() {
        return ifRandom;
    }

    public void setIfRandom(Integer ifRandom) {
        this.ifRandom = ifRandom;
    }

    public Integer getCaseState() {
        return caseState;
    }

    public void setCaseState(Integer caseState) {
        this.caseState = caseState;
    }

    public StockTakeMasterCriteria() {
    }

    public StockTakeMasterCriteria(Integer stockTakeMasterId) {
        this.stockTakeMasterId = stockTakeMasterId;
    }
}
