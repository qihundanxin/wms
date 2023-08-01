package com.deer.wms.system.manage.service;

public interface ValidatorService {
    Integer getNextId(String tableName);

    public String getNextIdString(String tableName);
}
