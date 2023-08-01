package com.deer.wms.ware.task.model.Return;

import java.util.List;

import com.deer.wms.ware.task.model.SoLog;

import lombok.ToString;

@ToString
public class SoLogDto extends SoLog {

    private List<SoLogItemDto> items;

    public List<SoLogItemDto> getItems() {
        return items;
    }

    public void setItems(List<SoLogItemDto> items) {
        this.items = items;
    }
}
