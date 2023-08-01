package com.deer.wms.ware.task.model.Return;

import java.util.List;

import lombok.ToString;

@ToString
public class DeliverData {
    private List<Deliver> delivers;

    public List<Deliver> getDelivers() {
        return delivers;
    }

    public void setDelivers(List<Deliver> delivers) {
        this.delivers = delivers;
    }
}
