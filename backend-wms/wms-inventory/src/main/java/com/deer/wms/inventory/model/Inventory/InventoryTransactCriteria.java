package com.deer.wms.inventory.model.Inventory;

import java.util.List;

import lombok.ToString;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@ToString
public class InventoryTransactCriteria extends InventoryTransact {

    private Integer itemTypeId;
    private String itemTypeName;

    private List<String> keyWordList;

    private String startCreateTime;
    private String endCreateTime;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页显示的条数
     */
    private Integer pageSize = 199999;

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public List<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
}
