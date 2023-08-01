package com.deer.wms.base.system.model.Cell;

import java.util.List;

import lombok.ToString;

@ToString
public class PlPrintCell {
    private List<CellInfoDto> cells;
    private Integer wareId;

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }

    public List<CellInfoDto> getCells() {
        return cells;
    }

    public void setCells(List<CellInfoDto> cells) {
        this.cells = cells;
    }
}
