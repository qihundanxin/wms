package com.deer.wms.base.system.model.PageSet;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by guo on 2019/11/27.
 */
@ToString
public class PageSetCriteria extends QueryCriteria {
    private String pageSetCode;
    private String pageSetName;
    private String value;
    private String tableName;

    public String getPageSetCode() {
        return pageSetCode;
    }

    public void setPageSetCode(String pageSetCode) {
        this.pageSetCode = pageSetCode;
    }

    public String getPageSetName() {
        return pageSetName;
    }

    public void setPageSetName(String pageSetName) {
        this.pageSetName = pageSetName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
