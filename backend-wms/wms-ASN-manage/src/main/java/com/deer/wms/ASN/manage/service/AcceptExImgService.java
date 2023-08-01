package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.AcceptExImg;
import com.deer.wms.ASN.manage.model.AcceptExImgCriteria;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/01/02.
 */
public interface AcceptExImgService extends Service<AcceptExImg, Integer> {
    List<String> findByAcceptExId(Integer acceptExId);

    List<AcceptExImg> findByShipBillCode2(String shipBillCode);

    List<AcceptExImg> findList(AcceptExImgCriteria criteria);
}
