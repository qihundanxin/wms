package com.deer.wms.project.root.constant;

public class WmsStatus {
    /**
     * AsnMaster - asn_state
     */
    // 0-初始化  2-部分收货  3-全部收货

    /**
     * AsnDetail - state
     */
    // 0-初始化  2-部分收货  3-全部收货

    /**
     * AcceptRecord - state
     */
    // 0-默认 1-同步erp中 2-同步成功

    /**
     * AcceptEx - state
     */
    // 0-默认 1-同步erp中 2-同步成功

    /**
     * PickTask - state
     */
    // -1-已取消 0-已分配   2-完成拣货 4-被合并 5-已经发运

    /**
     * SeedingWall - state
     */
    // 这里状态没用，根据波次状态来判断

    /**
     * SeedingCell - state
     */
    // 这里状态没用，根据波次状态来判断

    /**
     * SoMaster - state
     */
    // 0-初始化 1-订单拣货完成或者复核完成->退回商品 3-部分分配 4-分配完成 6-拣货完成 7-复核完成  8-完全发运

    /**
     * SoDetail - state
     */
    //  0-新建  1-部分分配  2-全部分配

    /**
     * WaveMaster - state
     */
    //  0-订单接收中  1-待货中 11-待货中  2-拣货完成 3-复合完成 4-发运完成 5-完结

    /**
     * PickTaskReturn - type
     */
    // 3-多拿，创建还货任务，直接放回不需要改库存

}
