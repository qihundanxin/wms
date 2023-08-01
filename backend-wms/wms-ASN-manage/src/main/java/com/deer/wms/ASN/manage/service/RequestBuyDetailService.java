package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2020/01/02.
 */
public interface RequestBuyDetailService extends Service<RequestBuyDetail, Integer> {

    List<RequestBuyDetailDto> findList(RequestBuyDetailCriteria criteria);

    List<RequestBuyDetail> findByBillNo(String billNo);

    RequestBuyDetail findByDetailNo(String detailNo);

    void deleteByBillNo(String billNo);

    int updateDetail(RequestBuyDetailDto detail);
}
