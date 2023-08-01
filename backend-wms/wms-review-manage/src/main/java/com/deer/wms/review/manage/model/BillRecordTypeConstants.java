package com.deer.wms.review.manage.model;

/**
 * 系统常量定义
 * <p>
 * Created by guo on 2018/11/15.
 */
public final class BillRecordTypeConstants {

    /**
     * 盘点单生成库存调整单草稿
     */
    public static final Integer INVENTORY_CHECK_CREATE_INVENTORY_ADJUST_DRAFT = 100;
    /**
     * 盘点单生成库存调整单
     */
    public static final Integer INVENTORY_CHECK_CREATE_INVENTORY_ADJUST = 101;
    /**
     * 手动选择库存生成库存调整单草稿
     */
    public static final Integer MANUAL_CREATE_INVENTORY_ADJUST_DRAFT = 102;
    /**
     * 手动选择库存生成库存调整单草稿
     */
    public static final Integer MANUAL_CREATE_INVENTORY_ADJUST = 103;
    /**
     * 盘点单生成的库存调整单草稿转成正式单据
     */
    public static final Integer UPDATE_INVENTORY_CHECK_CREATE_INVENTORY_ADJUST_DRAFT_TO_OFFICIAL = 104;
    /**
     * 手动选择库存生成的库存调整单草稿转成正式单据
     */
    public static final Integer UPDATE_MANUAL_CREATE_INVENTORY_ADJUST_DRAFT_TO_OFFICIAL = 105;
    /**
     * 库存调整
     */
    public static final Integer INVENTORY_ADJUST = 106;
}
