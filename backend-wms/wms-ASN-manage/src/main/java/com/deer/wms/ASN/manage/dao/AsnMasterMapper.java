package com.deer.wms.ASN.manage.dao;

import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.asn.AsnMasterCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnMasterDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AsnMasterMapper extends Mapper<AsnMaster> {
    List<AsnMasterDto> findList(AsnMasterCriteria criteria);
    List<AsnMasterDto> pdaAccept(AsnMasterCriteria criteria);

    int deleteByBillNo(@Param("billNo") String billNo, @Param("version") Integer version);

    AsnMasterDto findByBillNo(@Param("billNo") String billNo);

    AsnMaster findSoByBillNo(@Param("billNo") String billNo, @Param("wareCode") String wareCode);

    int update(AsnMaster asnMaster);
    List<AsnMaster> findByRequestBuyBillNO(String billNO);

    AsnMaster findBySoBillNo(@Param("soBillNo") String soBillNo, @Param("asnType") Integer asnType, @Param("wareId") Integer wareId);
}
