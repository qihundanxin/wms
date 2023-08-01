package com.deer.wms.ware.task.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.Return.SoLogItemDto;
import com.deer.wms.ware.task.model.SO.SoDayList;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoDetailQh;

public interface SoDetailMapper extends Mapper<SoDetail> {

    Integer updateState(SoDetail soDetail);
    Integer updateState2(@Param("id") Integer id,@Param("newState") Integer newState,@Param("oldState") Integer oldState);

    void cancel(@Param("billNo") String billNo);

    List<SoDetailDto> findList(SoDetailCriteria criteria);

    List<SoDetail> findByBillNo(@Param("billNo") String billNo);

    List<SoDetail> findSelfByBillNo(@Param("billNo") String billNo);
    List<SoDetailQh> findQhQuantity();

    List<SoDetailDto> findUndistributedSoDetail(@Param("wareId")Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail2(@Param("wareId")Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail3(@Param("wareId")Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail4(@Param("wareId")Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail5(@Param("wareId")Integer wareId);

    List<SoDetailDto> findDetailsByWaveId(@Param("wareId") Integer wareId, @Param("waveId") Integer waveId);

    List<SoDetail> findByMasterId(@Param("soId") Integer soId);

    List<SoDayList> findDayList(SoDetailCriteria criteria);

    List<SoDetailDto> findBaoKuanByItemCode(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("orgId") Integer orgId);

    List<SoLogItemDto> findLogItemsByBillNos(@Param("list") List<String> billNos);

    List<SoDetailDto> getSeedingByItemCode(@Param("itemCode") String itemCode, @Param("wareId") Integer wareId, @Param("waveId") Integer waveId);

    List<SoDetailDto> getSeedingYdYpByWaveMasterId(@Param("wareId") Integer waveId, @Param("waveMasterId") Integer waveMasterId);

    int update(SoDetail soDetail);
    
	List<SoDetail> findPickUPOrderByMasterId(@Param("soId") Integer soId);

    List<SoDetailDto> findForUpdatePickTask(@Param("wareId") Integer wareId, @Param("dateTime") LocalDateTime dateTime);
	List<SoDetail> pickupfindByBillNo(@Param("billNo") String billNo);

    /**
     * 4ChaoticState: 为混乱的状态数据定制的查询方式
     */
    List<SoDetailDto> findListAndMaster4ChaoticState(SoDetailCriteria criteria);
    List<SoDetailDto> findListAndMaster(SoDetailCriteria criteria);

    SoDetailDto findByBillNoAndItemCode(@Param("billNo") String billNo,@Param("itemCode") String itemCode);

}
