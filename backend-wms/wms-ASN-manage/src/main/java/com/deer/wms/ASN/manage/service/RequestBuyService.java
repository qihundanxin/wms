package com.deer.wms.ASN.manage.service;

import java.util.List;

import com.deer.wms.ASN.manage.model.buy.InsertData;
import com.deer.wms.ASN.manage.model.buy.RequestBuy;
import com.deer.wms.ASN.manage.model.buy.RequestBuyCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDto;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;

/**
 * Created by guo on 2020/01/02.
 */
public interface RequestBuyService extends Service<RequestBuy, Integer> {

    List<RequestBuyDto> findList(RequestBuyCriteria criteria);

    void createRequestBuy(InsertData insertData, CurrentUser currentUser);

    void deleteByBillNo(String billNo);

    void createFile(String billNo, CurrentUser currentUser);

    RequestBuyDto findByIdAndVersion(Integer requestBuyId, Integer version);

    int update(RequestBuyDto requestBuyDto);

    /**
     * 仅更新发票文件 并记录日志节点
     */
    void updateInvoice(RequestBuyDto dto, CurrentUser currentUser);
}
