package com.deer.wms.ware.task.model.SO;

import java.util.List;

import com.deer.wms.ware.task.model.pickTask.PickTask;

import lombok.ToString;

@ToString
public class DeliveryData {
    private List<PickTask> pickTasks;

    public List<PickTask> getPickTasks() {
        return pickTasks;
    }

    public void setPickTasks(List<PickTask> pickTasks) {
        this.pickTasks = pickTasks;
    }
}
