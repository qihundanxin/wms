package com.deer.wms.system.manage.service;

import com.deer.wms.system.manage.model.DataDictDetail;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.DataDictDetailCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by  on 2019/11/25.
 */
public interface DataDictDetailService extends Service<DataDictDetail, Integer> {

    List<DataDictDetail> findList(DataDictDetailCriteria criteria);

    List<DataDictDetail> findBydDataDictCode(String dataDictCode);
}
