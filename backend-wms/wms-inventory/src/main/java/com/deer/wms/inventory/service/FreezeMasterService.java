package com.deer.wms.inventory.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.freeze.FreezeMaster;
import com.deer.wms.inventory.model.freeze.FreezeMasterCriteria;
import com.deer.wms.inventory.model.freeze.FreezeMasterDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/12/25.
 */
public interface FreezeMasterService extends Service<FreezeMaster, Integer> {

    List<FreezeMasterDto> findList(FreezeMasterCriteria criteria);

    void deleteByBillNo(String billNo);

    void deleteByIdAndReleaseDetail(Integer id, CurrentUser currentUser);
}
