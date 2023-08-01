package com.deer.wms.inventory.model.stockTake;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by 郭靖勋 on 2020/02/06.
 */
@ToString
public class StockTakeTaskCriteria extends QueryCriteria {

    private Integer stockTakeTaskId;
    private Integer stockTakeMasterId;
    private String billNo;
    private Integer inventoryId;
    private Integer taskUserId;
    private Integer type;
    private Integer state;
    private List<StockTakeTaskDto> stockTakeTasks;
    private Integer wareId;
    private String masterBillNo;
    private String areaCode;
    private String shelfCode;
    private String cellCode;
    private String itemCode;
    private String boxCode;

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getMasterBillNo() {
        return masterBillNo;
    }

    public void setMasterBillNo(String masterBillNo) {
        this.masterBillNo = masterBillNo;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
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

    @Override
    public Integer getWareId() {
        return wareId;
    }

    @Override
    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public Integer getStockTakeTaskId() {
        return stockTakeTaskId;
    }

    public void setStockTakeTaskId(Integer stockTakeTaskId) {
        this.stockTakeTaskId = stockTakeTaskId;
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

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Integer taskUserId) {
        this.taskUserId = taskUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<StockTakeTaskDto> getStockTakeTasks() {
        return stockTakeTasks;
    }

    public void setStockTakeTasks(List<StockTakeTaskDto> stockTakeTasks) {
        this.stockTakeTasks = stockTakeTasks;
    }

    public StockTakeTaskCriteria() {
    }

    public StockTakeTaskCriteria(Integer stockTakeMasterId) {
        this.stockTakeMasterId = stockTakeMasterId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
