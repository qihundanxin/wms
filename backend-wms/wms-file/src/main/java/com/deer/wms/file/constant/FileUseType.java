package com.deer.wms.file.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件使用方式 对应的文件Path
 * 
 * @author xuesinuo
 *
 */
@Getter
@AllArgsConstructor
public enum FileUseType {
    /**
     * 导出：入库日报表
     */
    EXPORT_ASN_DAY_LIST("/export/asnDayList"),
    /**
     * 导出：入库单
     */
    EXPORT_ASN("/export/asn"),
    /**
     * 导出：采购信息
     */
    EXPORT_PO("/export/po"),
    /**
     * 导出：库存余量
     */
    EXPORT_BALANCE("/export/balance"),
    /**
     * 导出：库存信息
     */
    EXPORT_INVENTORY("/export/inventory"),
    /**
     * 导出：库存SKU数量统计
     */
    EXPORT_SKU_INVENTORY("/export/skuInventory"),
    /**
     * 导出：盘点任务
     */
    EXPORT_STOCK_TAKE_TASK("/export/stockTakeTask"),
    /**
     * 导出：任务调度单
     */
    EXPORT_SYS_JOB("/export/sysJob"),
    /**
     * 导出：任务调度日志
     */
    EXPORT_SYS_JOB_LOG("/export/sysJobLog"),
    /**
     * 导出：出库单发票
     */
    EXPORT_SO_INVOICE("/export/soInvoice"),
    /**
     * 导出：出库日报表
     */
    EXPORT_SO_DAY_LIST("/export/soDayList"),
    /**
     * 导出：出库单
     */
    EXPORT_SO_MASTER("/export/soMaster"),
    /**
     * 导出：缺货清单
     */
    EXPORT_RESTOCKING("/export/restocking"),
    /**
     * 上传：采购单发票
     */
    UPLOAD_CG_INVOICE("/upload/cgInvoice"),

    /**
     * 上传：基础移库图片
     */
    MOVE_IMG("/upload/move"),
    /**
     * PDA上传：异常波次拣货图片
     */
    UPLOAD_PDA_PICK_BATCH_EXCEPTION("/upload/pdaPickBatchException"),
    ;

    /**
     * 路径
     */
    private String path;
}
