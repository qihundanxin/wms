package com.deer.wms.base.system.model.Cell;

import java.util.List;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/27.
 */
@ToString
public class CellInfoCriteria extends QueryCriteria {
    private Integer cellId;
    private String cellCode;
    private String cellName;
    private Integer shelfInfoId;
    private Integer cellUseType;
    private String areaCode;
    private Integer isEmptyCell;
    private Integer cellType;
    private Integer cellAbcLevel;
    private String pickOrder;
    private String state;
    private Integer cellPickType;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private String shelfCode;
    private List<Integer> cellUseTypes;

    //查询相同物料的相邻货位时使用
    private String borderItemCode;

    public String getBorderItemCode() {
        return borderItemCode;
    }

    public void setBorderItemCode(String borderItemCode) {
        this.borderItemCode = borderItemCode;
    }

    public List<Integer> getCellUseTypes() {
        return cellUseTypes;
    }

    public void setCellUseTypes(List<Integer> cellUseTypes) {
        this.cellUseTypes = cellUseTypes;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public Integer getCellType() {
        return cellType;
    }

    public void setCellType(Integer cellType) {
        this.cellType = cellType;
    }

    public Integer getCellAbcLevel() {
        return cellAbcLevel;
    }

    public void setCellAbcLevel(Integer cellAbcLevel) {
        this.cellAbcLevel = cellAbcLevel;
    }

    public String getPickOrder() {
        return pickOrder;
    }

    public void setPickOrder(String pickOrder) {
        this.pickOrder = pickOrder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCellPickType() {
        return cellPickType;
    }

    public void setCellPickType(Integer cellPickType) {
        this.cellPickType = cellPickType;
    }

    public Integer getIsEmptyCell() {
        return isEmptyCell;
    }

    public void setIsEmptyCell(Integer isEmptyCell) {
        this.isEmptyCell = isEmptyCell;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public String getCellCode() {
        return cellCode;
    }

    public void setCellCode(String cellCode) {
        this.cellCode = cellCode;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Integer getShelfInfoId() {
        return shelfInfoId;
    }

    public void setShelfInfoId(Integer shelfInfoId) {
        this.shelfInfoId = shelfInfoId;
    }

    public Integer getCellUseType() {
        return cellUseType;
    }

    public void setCellUseType(Integer cellUseType) {
        this.cellUseType = cellUseType;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
