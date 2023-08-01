package com.deer.wms.system.manage.model.dept;

import lombok.ToString;

@ToString
public class DeptDto extends Dept {
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
