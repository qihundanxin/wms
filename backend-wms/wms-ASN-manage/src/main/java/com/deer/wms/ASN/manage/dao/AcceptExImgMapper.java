package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.AcceptExImg;
import com.deer.wms.ASN.manage.model.AcceptExImgCriteria;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcceptExImgMapper extends Mapper<AcceptExImg> {

    List<String> findByAcceptExId(@Param("acceptExId") Integer acceptExId);

    List<AcceptExImg> findByShipBillCode2(@Param("shipBillCode") String shipBillCode);

    List<AcceptExImg> findList(AcceptExImgCriteria criteria);
}