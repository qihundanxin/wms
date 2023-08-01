package com.deer.wms.ware.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.SO.PdaSearchSoData;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SO.SoMasterYdyp;

public interface SoMasterMapper extends Mapper<SoMaster> {

    void updateState(SoMaster soMaster);

    SoMasterDto findBillDetailByOrderNo(@Param("orderNo") String orderNo, @Param("wareId") Integer wareId);

    SoMaster getByBoxOrCell(@Param("code") String code, @Param("wareId") Integer wareId);

    SoMasterDto getDtoByBoxOrCell(@Param("code") String boxCode, @Param("wareId") Integer wareId);

    List<SoMaster> findCanTongBu();

    List<SoMasterDto> findByWaveId(@Param("waveMasterId") Integer id, @Param("wareId") Integer wareId);

    List<SoMasterDto> findList(SoMasterCriteria criteria);

    List<SoMasterDto> getSeedingByItemCode(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("waveMasterId") Integer waveMasterId);

    int deleteByBillNo(@Param("billNo") String billNo, @Param("version") Integer version);

    List<SoMaster> findSoGroupInfo(SoMasterCriteria criteria);

    List<SoMaster> findSoByWave(SoMasterCriteria criteria);

    List<SoMasterDto> findThatNoCarrier(SoMasterCriteria criteria);

    List<PdaSearchSoData> pdaSearchSo(@Param("trackCode") String trackCode);

    Integer findCountByWaveIdAndState(@Param("waveMasterId") Integer waveMasterId, @Param("state") Integer state);

    SoMasterYdyp getYdyp(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("waveId") Integer waveId);

    SoMasterYdyp getYdyp2(@Param("billNo") String billNo, @Param("wareId") Integer wareId, @Param("waveId") Integer waveId);

    Integer getMaxPriority();

    List<SoMasterDto> getUnCompleteLog();

    int update(SoMaster soMaster);

    List<SoMaster> getReplayOrderIDByID(@Param("orderIDs") String orderIDs);

	List<SoMaster> getReplayOrderIDByIDS(@Param("orderIDs") SoMasterCriteria orderIDs);

	SoMaster findByIdAndWareID(@Param("orderID") String orderID,@Param("wareID")  String wareID,@Param("orderIDLen")  String orderIDLen );

    SoMaster findByBillNo(@Param("billNo") String billNo);



}
