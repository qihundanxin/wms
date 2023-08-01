package com.deer.wms.ware.task.model.Load;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by ll on 2020/02/17.
 */
@ToString
public class LoadMasterCriteria extends QueryCriteria {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
