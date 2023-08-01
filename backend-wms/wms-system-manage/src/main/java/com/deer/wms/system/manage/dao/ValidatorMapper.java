package com.deer.wms.system.manage.dao;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.system.manage.model.ValidatorModel;

public interface ValidatorMapper {

    String findKey(@Param("tableName") String tableName);

    Integer findAddValidator(ValidatorModel validatorModel);

    Integer findUpdateValidator(ValidatorModel validatorModel);

    Integer getNextId(@Param("tableName") String tableName, @Param("dbName") String dbName);

    // 查询主键列名称
    String findPrimaryKey(ValidatorModel validatorModel);
}