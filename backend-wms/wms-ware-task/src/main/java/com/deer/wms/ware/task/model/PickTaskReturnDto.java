package com.deer.wms.ware.task.model;

import java.util.List;

import com.deer.wms.base.system.model.ItemUpc;

import lombok.ToString;

@ToString
public class PickTaskReturnDto extends PickTaskReturn{
   private List<ItemUpc> upcs;

    public List<ItemUpc> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<ItemUpc> upcs) {
        this.upcs = upcs;
    }
}
