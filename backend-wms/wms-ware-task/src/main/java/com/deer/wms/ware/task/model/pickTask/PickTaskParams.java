package com.deer.wms.ware.task.model.pickTask;

import lombok.ToString;

@ToString
public class PickTaskParams {
    private double needAllottedQuantity;
    private double allottedQuantity;

    public double getNeedAllottedQuantity() {
        return needAllottedQuantity;
    }

    public void setNeedAllottedQuantity(double needAllottedQuantity) {
        this.needAllottedQuantity = needAllottedQuantity;
    }

    public double getAllottedQuantity() {
        return allottedQuantity;
    }

    public void setAllottedQuantity(double allottedQuantity) {
        this.allottedQuantity = allottedQuantity;
    }
}
