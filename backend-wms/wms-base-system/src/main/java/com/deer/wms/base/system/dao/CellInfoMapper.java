package com.deer.wms.base.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Cell.CellInfoCriteria;
import com.deer.wms.base.system.model.Cell.CellInfoDto;
import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.ValidatorCellInfoModel;

public interface CellInfoMapper extends Mapper<CellInfo> {

    void deleteByIds2(String ids);
    List<CellInfoDto> findList(CellInfoCriteria criteria);

    CellInfoDto findByCodeAndWareId(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId);

    List<CellInfo> findByShelfInfoId(@Param("shelfInfoId") Integer shelfInfoId);

    void deleteByShelfId(@Param("shelfInfoId") Integer shelfInfoId);

    List<CellInfoDto> findByCellUseTypes(CellInfoCriteria criteria);

    CellInfo getSysCell(@Param("wareId") Integer wareId, @Param("type") Integer type);

    String getNewInCell(Integer wareId);

    /**
     * 仅根据货位编码查询
     * 
     * @param cellCode 货位编码
     * @return 结果
     */
    List<CellInfo> findByCode(String cellCode);

    /**
     * @Author luolin
     * @Description //TODO
     * @Date 10:21 2023/6/26
     * @param model
     * @return int
     **/
    int validatorChongfu(ValidatorCellInfoModel model);

    CellInfo findCellInfo(CellInfo cellInfo);

//    Integer getSjcTasks(@Param("cellCode") String cellCode,@Param("wareId") Integer wareId);
//    Integer getInventorys(@Param("cellCode") String cellCode,@Param("wareId") Integer wareId);

}