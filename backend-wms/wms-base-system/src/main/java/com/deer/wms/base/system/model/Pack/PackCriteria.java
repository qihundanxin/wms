package com.deer.wms.base.system.model.Pack;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/18.
 */
@ToString
public class PackCriteria extends QueryCriteria {
    private Integer id;
    private String packData;
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackData() {
        return packData;
    }

    public void setPackData(String packData) {
        this.packData = packData;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
