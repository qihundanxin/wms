package com.deer.wms.ware.task.model.statistics;

import java.util.List;

import lombok.Data;

/**
 * 首页数据查询接口返回结果
 */
@Data
public class TopStatisticsDto {

    /** 出库单数 */
    private int soCount;
    /** 缺货数 */
    private int oosCount;
    /** 待拣货 */
    private int waitPickupTotal;
    /** 未发运 */
    private int waitShippedCount;
    /** 入库单数量 */
    private int asnCount;
    /** 未验收 */
    private int unQcCount;
    /**
     * 折线图数据
     */
    private List<StatByDateDto> statByDateList;
    /** 货位利用率（%） */
    private int cellUsedRate;
    /** 入库单总数 */
    private int receiveTotal;
    /** 验收总数 */
    private int acceptedCount;
    /** 退货单总数 */
    private int returnTotal;
    /** 完成退货总数 */
    private int returnedCount;
    /** 拣货单总数 */
    private int pickupTotal;
    /** 完成拣货总数 */
    private int pickedCount;
}
