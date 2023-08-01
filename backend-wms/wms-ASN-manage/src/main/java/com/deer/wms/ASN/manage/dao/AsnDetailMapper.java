package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.asn.*;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AsnDetailMapper extends Mapper<AsnDetail> {
    List<AsnDetailDto> findList(AsnDetailCriteria criteria);

    List<AsnDetail> findByBillNo(@Param("billNo") String billNo);

    List<AsnDetailDto> findByDetailNo(@Param("detailNo") String detailNo);

    int update(AsnDetail asnDetail);

    AsnDetailDto findByDetailNoAndVersion(@Param("detailNo")String detailNo, @Param("version")Integer version);
}