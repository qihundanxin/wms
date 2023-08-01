package com.deer.wms.ASN.manage.service;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptExCriteria;
import com.deer.wms.ASN.manage.model.AcceptExDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by  on 2021/01/02.
 */
public interface AcceptExService extends Service<AcceptEx, Integer> {

    List<AcceptExDto> findList(AcceptExCriteria criteria);

    List<AcceptEx> findCanTongBu();

}
