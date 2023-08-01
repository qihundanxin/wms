package com.deer.wms.ware.task.model.SO;

import com.deer.wms.base.system.model.ImportModel;

import lombok.ToString;

@ToString
public class SoImport extends ImportModel {
    private Integer wareId;

    private Integer insertType; //0-提交  -2保存为草稿

    public Integer getInsertType() {
        return insertType;
    }

    public void setInsertType(Integer insertType) {
        this.insertType = insertType;
    }

    public Integer getWareId() {
        return wareId;
    }

    public void setWareId(Integer wareId) {
        this.wareId = wareId;
    }
}
