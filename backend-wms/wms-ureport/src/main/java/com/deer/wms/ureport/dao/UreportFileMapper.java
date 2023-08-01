package com.deer.wms.ureport.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ureport.model.UreportFile;

import java.util.List;

public interface UreportFileMapper extends Mapper<UreportFile> {

    /**
     * 根据报表名称检查报表是否存在
     *
     * @param name 报表名称
     * @return
     */
    public int checkExistByName(String name);

    /**
     * 根据报表名称删除报表
     *
     * @param name
     * @return
     */
    public int deleteReportFileByName(String name);

}
