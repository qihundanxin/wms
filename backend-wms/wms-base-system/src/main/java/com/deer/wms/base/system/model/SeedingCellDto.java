package com.deer.wms.base.system.model;

import lombok.ToString;

@ToString
public class SeedingCellDto extends SeedingCell {
    private Integer soState;
    private Integer soMasterId;

    public Integer getSoMasterId() {
        return soMasterId;
    }

    public void setSoMasterId(Integer soMasterId) {
        this.soMasterId = soMasterId;
    }

    public Integer getSoState() {
        return soState;
    }

    public void setSoState(Integer soState) {
        this.soState = soState;
    }
}
