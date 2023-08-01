package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.model.ware.WareInfoCriteria;
import com.deer.wms.system.manage.model.ware.WareInfoDto;
import com.deer.wms.system.manage.model.ware.WareInfoDto2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WareInfoMapper extends Mapper<WareInfo> {

    void deleteByCodeAndCom(WareInfoCriteria criteria);

    List<WareInfoDto> findList(WareInfoCriteria criteria);

    List<WareInfoDto2> findSome(WareInfoCriteria criteria);

    List<WareInfo> searchBarWareList(WareInfoCriteria criteria);

    List<WareInfoDto2> findByName(@Param("wareName") String wareName);

    List<Integer> searchAllWareId();

	void updateStatus(@Param("wareId") String wareId,@Param("status")  String status);

	List<WareInfo> findAllByStatus();
}
