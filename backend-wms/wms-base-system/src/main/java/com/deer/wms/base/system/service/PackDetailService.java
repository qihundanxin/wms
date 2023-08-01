package com.deer.wms.base.system.service;

import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Pack.PackDetail2;
import com.deer.wms.base.system.model.Pack.PackDetailCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by guo on 2019/11/18.
 */
public interface PackDetailService extends Service<PackDetail, Integer> {

    List<PackDetailDto> findList(PackDetailCriteria criteria);
    List<PackDetailDto> findByPackCode(String packCode);

    List<PackDetailDto> findByPackId(Integer id);

    void deleteByCode(String packCode);

    PackDetail findLevel(Integer packDetailId);

    List<PackDetail> findByCode(String packCode);

    List<PackDetail2> findByCode2(String packCode);

}
