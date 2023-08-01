package com.deer.wms.inventory.model.Inventory;

import lombok.ToString;

@ToString
public class CellUseTypeList extends Inventory {

    private Integer itemTypeId;
    private String itemTypeName;
    private String cellUseType;
    private String spec;
    private String model;

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


    public void setCellUseType(String cellUseType) {
        this.cellUseType = cellUseType;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
