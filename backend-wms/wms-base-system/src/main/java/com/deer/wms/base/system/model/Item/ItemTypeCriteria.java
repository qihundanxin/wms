package com.deer.wms.base.system.model.Item;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/19.
 */
@ToString
public class ItemTypeCriteria extends QueryCriteria {
    private Integer itemTypeId;

    private String itemTypeCode;

    private String itemTypeName;

    private String batchTacticCode;
    private String upperTacticCode;
    private String upperTacticName;
    private String batchTacticName;
    private String qcTacticCode;

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getBatchTacticCode() {
        return batchTacticCode;
    }

    public void setBatchTacticCode(String batchTacticCode) {
        this.batchTacticCode = batchTacticCode;
    }

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }

    public String getBatchTacticName() {
        return batchTacticName;
    }

    public void setBatchTacticName(String batchTacticName) {
        this.batchTacticName = batchTacticName;
    }

    public String getQcTacticCode() {
        return qcTacticCode;
    }

    public void setQcTacticCode(String qcTacticCode) {
        this.qcTacticCode = qcTacticCode;
    }
}
