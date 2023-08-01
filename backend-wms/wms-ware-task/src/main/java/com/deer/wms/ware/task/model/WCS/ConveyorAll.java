package com.deer.wms.ware.task.model.WCS;

import lombok.ToString;

@ToString
public class ConveyorAll {
    private Integer runMode;

    private Integer runState;

    public Integer getRunMode() {
        return runMode;
    }

    public void setRunMode(Integer runMode) {
        this.runMode = runMode;
    }

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
    }
}
