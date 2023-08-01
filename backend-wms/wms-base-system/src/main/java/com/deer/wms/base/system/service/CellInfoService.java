package com.deer.wms.base.system.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Cell.CellInfoCriteria;
import com.deer.wms.base.system.model.Cell.CellInfoDto;
import com.deer.wms.base.system.model.Cell.CellInfoImportParam;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.ValidatorCellInfoModel;

/**
 * Created by guo on 2019/11/27.
 */
public interface CellInfoService extends Service<CellInfo, Integer> {

    int update(CellInfo cellInfo);

    void deleteById(Integer id);

    void deleteByIds(String ids);

    String getSysAcceptCell(Integer wareId);
    CellInfo getSysAcceptCellInfo(Integer wareId);

    String getSysJhCell(Integer wareId);

    List<CellInfoDto> findList(CellInfoCriteria criteria);

    List<CellInfo> findByShelfInfoId(@Param("shelfInfoId") Integer shelfInfoId);

    void deleteByShelfId(@Param("shelfInfoId") Integer shelfInfoId);

    void modifyCellState(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId, String sate, CurrentUser currentUser);

    CellInfoDto findByCodeAndWareId(@Param("cellCode") String cellCode, @Param("wareId") Integer wareId);

    /**
     * 仅根据货位编码查询（不区分仓库！）
     *
     * @param cellCode 货位编码
     * @return 查询结果
     */
    List<CellInfo> findByCode(String cellCode);

    List<CellInfoDto> findByCellUseTypes(CellInfoCriteria criteria);

    String getNewInCell(Integer wareId);

    @Deprecated // TODO 改用findByCodeAndWareId
    Integer getCellType(String cellCode, Integer wareId);

    void updateCellInfo(CellInfo cellInfo, Integer wareId);

    CellInfo findCellInfo(CellInfo cellInfo);
    
    void saveExcelMode(CellInfoImportParam cellInfoImportParam);

    /**
     * @Author luolin
     * @Description 验证货位重复
     * @Date 10:18 2023/6/26
     * @param model
     * @return int
     **/
    int validatorChongfu(ValidatorCellInfoModel model);

    /**
     * @Author luolin
     * @Description 保存货位
     * @Date 10:53 2023/6/26
     * @param cellInfo
     * @return
     **/
    void saveCellInfo(CellInfo cellInfo);

}
