package com.deer.wms.system.manage.model;

import com.deer.wms.project.root.core.service.QueryCriteria;

import lombok.ToString;

/**
 * Created by on 2019/11/25.
 */
@ToString
public class DataDictDetailCriteria extends QueryCriteria {
    private String dataDictCode;

    private String dataDictName;

    private String dictKey;

    public String getDataDictCode() {
        return dataDictCode;
    }

    public void setDataDictCode(String dataDictCode) {
        this.dataDictCode = dataDictCode;
    }

    public String getDataDictName() {
        return dataDictName;
    }

    public void setDataDictName(String dataDictName) {
        this.dataDictName = dataDictName;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }
}
