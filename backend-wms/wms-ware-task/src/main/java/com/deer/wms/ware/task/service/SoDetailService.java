package com.deer.wms.ware.task.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SoDetailStockoutInfo;
import com.deer.wms.ware.task.model.Return.SoLogItemDto;
import com.deer.wms.ware.task.model.SO.SoDayList;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoDetailQh;

/**
 * Created by guo on 2020/02/02.
 */
public interface SoDetailService extends Service<SoDetail, Integer> {
    void updateState(SoDetail soDetail);

      void updateState2( Integer id,   Integer newState,  Integer oldState) ;

        void cancel(String billNo);

    List<SoDetailDto> findList(SoDetailCriteria criteria);

    List<SoDetailDto> findBaoKuanByItemCode(String itemCode, Integer wareId, Integer orgId);

    List<SoDetail> findByBillNo(String billNo);

    /**
     * 进查询详情，不关联拣货
     * 
     * @param billNo 单号
     * @return 全部详情
     */
    List<SoDetail> findSelfByBillNo(String billNo);
    List<SoDetailQh> findQhQuantity();
    List<SoDetailDto> findUndistributedSoDetail(Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail2(Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail3(Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail4(Integer wareId);
    List<SoDetailDto> findUndistributedSoDetail5(Integer wareId);

    void reduceSkuQuantity(SoDetail soDetail);
    void reduceSkuQuantityFor0And0(SoDetail soDetail);


    List<SoDetail> findByMasterId(Integer soId);

    List<SoDetailDto> getSeedingByItemCode(String itemCode, Integer wareId, Integer waveId);

    List<SoDayList> findDayList(SoDetailCriteria criteria);

    List<SoLogItemDto> findLogItemsByBillNos(List<String> billNos);

    List<SoDetailDto> findDetailsByWaveId(Integer wareId, Integer waveId);

    List<SoDetailDto> getSeedingYdYpByWaveMasterId(@Param("wareId") Integer wareId, @Param("waveMasterId") Integer waveMasterId);

	List<SoDetail> findPickUPOrderByMasterId(Integer parseInt);

    List<SoDetailDto> findForUpdatePickTask(Integer wareId, LocalDateTime dateTime);

    void cancelSoDetail(SoDetailDto soDetailDto);

	List<SoDetail> pickupfindByBillNo(String billNo);

    List<SoDetailDto> findListAndMaster(SoDetailCriteria criteria);

    /**
     * 查询缺货信息
     * 
     * @param billNo
     */
    List<SoDetailStockoutInfo> getStockoutInfo(String billNo);

    /**
     * 根据出库单号与sku 查询出库详情
     * @return
     */
    SoDetailDto findByBillNoAndItemCode( String billNo, String itemCode);


    /**
     * 查询出库任务
     * @param criteria
     * @return
     */
    List<SoDetailDto> findListAndSend(SoDetailCriteria criteria);
}
