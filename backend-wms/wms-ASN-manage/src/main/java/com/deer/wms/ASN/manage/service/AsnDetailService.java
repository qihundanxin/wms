package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.project.root.core.service.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by guo on 2020/01/03.
 */
public interface AsnDetailService extends Service<AsnDetail, Integer> {

    List<AsnDetailDto> findList(AsnDetailCriteria criteria);

    List<AsnDetail> findByBillNo(String billNo);

    List<AsnDetailDto> findByDetailNo(String detailNo);

    AsnDetailDto findByDetailNoAndVersion(String detailNo,Integer version);
}
