package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.asn.AsnMasterCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnMasterDto;
import com.deer.wms.ASN.manage.model.asn.AsnData;
import com.deer.wms.ASN.manage.model.asn.CreateASN.CreateASN;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.Allot.AllotData;

import java.util.List;

/**
 * Created by guo on 2020/01/03.
 */
public interface AsnMasterService extends Service<AsnMaster, Integer> {

    List<AsnMasterDto> findList(AsnMasterCriteria criteria);
    List<AsnMasterDto> pdaAccept(AsnMasterCriteria criteria);

    void deleteByBillNo(String billNo, Integer version);

    void createAsnMaster(AsnData asnData, CurrentUser currentUser);

    void createFile(String billNo, CurrentUser currentUser);

    void createASN(CreateASN createASN, CurrentUser currentUser);

    AsnMasterDto findByBillNo(String billNo);

    AsnMaster findSoByBillNo(String billNo, String wareCode);

    void allot(AllotData allotData, CurrentUser currentUser);
	void forceCompletion(String billNO);

    List<AsnMaster> findByRequestBuyBillNO(String billNO);

    AsnMaster findByOrderId(String orderId, Integer asnType, Integer wareId);
}
