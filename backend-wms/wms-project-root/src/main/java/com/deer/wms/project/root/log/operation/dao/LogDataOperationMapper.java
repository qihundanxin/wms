package com.deer.wms.project.root.log.operation.dao;

import org.apache.ibatis.annotations.Mapper;

import com.deer.wms.project.root.log.operation.model.LogDataOperation;

/**
 * 数据操作日志表 Mapper
 * 
 * @author xuesinuo
 *
 */
@Mapper
public interface LogDataOperationMapper {
    /**
     * 保存日志
     * 
     * @param logDo 日志信息
     */
    void save(LogDataOperation logDo);
}
