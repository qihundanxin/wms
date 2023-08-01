package com.deer.wms.system.manage.model.organization;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/12/31.
 */
@ToString
public class OrganizationTacticCriteria extends QueryCriteria {

    private String upperTacticCode;

    public String getUpperTacticCode() {
        return upperTacticCode;
    }

    public void setUpperTacticCode(String upperTacticCode) {
        this.upperTacticCode = upperTacticCode;
    }
}
