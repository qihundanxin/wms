package com.deer.wms.base.system.model.Cell;

import com.deer.wms.base.system.model.ImportModel;

import lombok.ToString;

@ToString
public class CellImportModel extends ImportModel {

    private Integer wareId;

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }
}
