package com.deer.wms.ware.task.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.deer.wms.ware.task.model.statistics.StatByDateDto;
import com.deer.wms.ware.task.model.statistics.StatisticsParam;
import com.deer.wms.ware.task.model.statistics.TopStatisticsDto;
import com.deer.wms.ware.task.model.statistics.TopStatisticsRequest;

public interface StatisticsMapper {

    TopStatisticsDto topSoStatistics(@Param("request") TopStatisticsRequest request);

    TopStatisticsDto topAcceptStatistics(@Param("request") TopStatisticsRequest request);

    TopStatisticsDto topAcceptExStatistics(@Param("request") TopStatisticsRequest request);

    List<StatByDateDto> getInboundCountByDate(@Param("request") TopStatisticsRequest request);

    List<StatByDateDto> getOutboundCountByDate(@Param("request") TopStatisticsRequest request);

    List<Map<String, Object>> getCellStat(@Param("request") TopStatisticsRequest request);

    Map<String, Object> getPickupStat(@Param("request") TopStatisticsRequest request);

    Map<String, Object> getReturnStat(@Param("request") TopStatisticsRequest request);

    Map<String, Object> getInboundStat(@Param("request") TopStatisticsRequest request);

    Integer getSoCount(StatisticsParam param);

    Integer getOosCount(StatisticsParam param);

    Integer getWaitPickupTotal(StatisticsParam param);

    Integer getWaitShippedCount(StatisticsParam param);

    Integer getAsnCount(StatisticsParam param);

    Integer getUnQcCount(StatisticsParam param);

    List<String> getCellCode(StatisticsParam param);

    List<String> getCellUsedCode(@Param("param") StatisticsParam param, @Param("cellCodes") List<String> cellCodes);

    Integer getReceiveTotal(StatisticsParam param);

    Integer getAcceptedCount(StatisticsParam param);

    Integer getReturnTotal(StatisticsParam param);

    Integer getReturnedCount(StatisticsParam param);

    Integer getPickupTotal(StatisticsParam param);

    Integer getPickedCount(StatisticsParam param);

    Double getAsnQty(StatisticsParam param);

    Double getSoQty(StatisticsParam param);

}
