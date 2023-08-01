package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.model.buy.RequestBuyLog;
import com.deer.wms.ASN.manage.service.RequestBuyLogService;
import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guo on 2020/01/02.
 */
@Service
@Transactional
public class RequestBuyLogServiceImpl extends AbstractService<RequestBuyLog, Long> implements RequestBuyLogService {

}
