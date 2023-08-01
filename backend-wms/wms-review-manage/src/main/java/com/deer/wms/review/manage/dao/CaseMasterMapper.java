package com.deer.wms.review.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.review.manage.model.CaseMaster;
import com.deer.wms.review.manage.model.CaseMasterCriteria;
import com.deer.wms.review.manage.model.CaseMasterDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaseMasterMapper extends Mapper<CaseMaster> {
    List<CaseMasterDto> findList(CaseMasterCriteria criteria);

    void deleteByBillNo(String billNo);

    CaseMaster findByBillNo(@Param("billNo") String billNo);
}