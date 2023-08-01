package com.deer.wms.lzb.erp.service;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.inventory.model.ErpRecord;
import com.deer.wms.inventory.model.ErpRecordCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoMaster;

import java.util.List;

/**
 * Created by  on 2021/02/14.
 */
public interface ErpRecordService extends Service<ErpRecord, Integer> {

    List<ErpRecord> findList(ErpRecordCriteria criteria);

    void tongbuRRP();

    void inTrue(AcceptRecord acceptRecord);

    void inEx(AcceptEx acceptEx);

    void out(SoMaster soMaster);

    void returnInfo(AcceptEx acceptEx);

    void inventoryAdjust(InventoryAdjustDetailDto adjustDetail);

}
