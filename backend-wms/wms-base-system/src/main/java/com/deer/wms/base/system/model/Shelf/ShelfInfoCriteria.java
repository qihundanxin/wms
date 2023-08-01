package com.deer.wms.base.system.model.Shelf;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/27.
 */
@ToString
public class ShelfInfoCriteria extends QueryCriteria {

    private Integer shelfInfoId;
    private Integer areaId;
    private Integer shelfNo;

    private Integer shelfRow;
    private Integer shelfColumn;
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getShelfInfoId() {
        return shelfInfoId;
    }

    public void setShelfInfoId(Integer shelfInfoId) {
        this.shelfInfoId = shelfInfoId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getShelfNo() {
        return shelfNo;
    }

    public void setShelfNo(Integer shelfNo) {
        this.shelfNo = shelfNo;
    }

    public Integer getShelfRow() {
        return shelfRow;
    }

    public void setShelfRow(Integer shelfRow) {
        this.shelfRow = shelfRow;
    }

    public Integer getShelfColumn() {
        return shelfColumn;
    }

    public void setShelfColumn(Integer shelfColumn) {
        this.shelfColumn = shelfColumn;
    }
}
