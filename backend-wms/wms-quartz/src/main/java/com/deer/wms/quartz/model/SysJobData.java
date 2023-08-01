package com.deer.wms.quartz.model;

import java.util.List;

import lombok.ToString;

@ToString
public class SysJobData {
    private List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
