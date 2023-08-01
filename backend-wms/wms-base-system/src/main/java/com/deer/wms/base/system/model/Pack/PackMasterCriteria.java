package com.deer.wms.base.system.model.Pack;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/18.
 */
@ToString
public class PackMasterCriteria extends QueryCriteria {

    private Integer packMasterId;

    private String packCode;

    private String packName;

    public Integer getPackMasterId() {
        return packMasterId;
    }

    public void setPackMasterId(Integer packMasterId) {
        this.packMasterId = packMasterId;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

}
