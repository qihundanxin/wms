package com.deer.wms.lzb.erp.service;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ware.task.model.SO.SoMaster;

/**
 * Created by ll on 2020/02/06.
 */
public interface LaiZanBaoERPService {

    public void tongbuRRP();

    void inTrue(AcceptRecord acceptRecord);

    void inEx(AcceptEx acceptEx);

    void out(SoMaster soMaster);

    void returnInfo(AcceptEx acceptEx);
}