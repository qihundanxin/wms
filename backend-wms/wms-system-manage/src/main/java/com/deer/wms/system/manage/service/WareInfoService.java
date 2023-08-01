package com.deer.wms.system.manage.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.model.ware.WareInfoCriteria;
import com.deer.wms.system.manage.model.ware.WareInfoDto;
import com.deer.wms.system.manage.model.ware.WareInfoDto2;

import java.util.List;

/**
 * Created by guo on 2019/11/06.
 */
public interface WareInfoService extends Service<WareInfo, Integer> {

    void deleteByCodeAndCom(WareInfoCriteria criteria);

    List<WareInfoDto> findList(WareInfoCriteria criteria);

    List<WareInfoDto2> findByName(String wareName);

    List<WareInfoDto2> findSome(WareInfoCriteria criteria);

    WareInfo createWare(WareInfoDto wareInfo);

    List<Integer> searchAllWareId();

	void updateStatus(String wareId, String status);

	List<WareInfo> findAllByStatus();

}
