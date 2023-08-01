package com.deer.wms.ASN.manage.model.in;

import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;

import lombok.Data;

/**
 * 验收WebHook
 * 
 * @author xuesinuo
 *
 */
@Data
public class AcceptWebHook {
    private String _sku = "SKU";
    private String sku;

    private String _inventoryId = "库存ID";
    private Integer inventoryId;

    private String _wareId = "仓库ID";
    private Integer wareId;

    private String _shopId = "SHOPID";
    private Integer shopId;

    private String _toCellCode = "库位号";
    private String toCellCode;

    private String _toQuantity = "入库数量";
    private Double toQuantity;

    private String _expectQuantity = "预期数量";
    private Double expectQuantity;

    private String _toPackDetailId = "入库单位ID";
    private Long toPackDetailId;

    private String _toPackDescribe = "入库单位名";
    private String toPackDescribe;

    private String _toTransRatio = "单位比";
    private Double toTransRatio;

    private String _acceptUserId = "验收人ID";
    private Integer acceptUserId;

    private String _acceptUserName = "验收人";
    private String acceptUserName;

    private String _acceptTime = "验收时间";
    private String acceptTime;

    private String _billType = "0-默认验收 1-采购入库 2-ASN单 3-盲收 4-冻结 5-扫描验收 6-异常验收";
    private Integer billType;

    private String _asnBillNo = "入库单号";
    private String asnBillNo;

    private String _asnDetailNo = "入库单详情号";
    private String asnDetailNo;

    private String _requestBuyBillNo = "采购单号";
    private String requestBuyBillNo;

    private String _requestBuyDetailNo = "采购单详情号";
    private String requestBuyDetailNo;

    private String _requestBuyDetail = "采购单详情对象";
    private RequestBuyDetail requestBuyDetail;

    private String _availableQuantity = "拣货位+仓储位=可售卖；拣货位+仓储位+验收位=库存实际持有（需成本均摊）；";

    private String _availableQuantityPickCell = "拣货位可用库存";
    private Double availableQuantityPickCell;

    private String _availableQuantityStorageCell = "仓储位可用库存";
    private Double availableQuantityStorageCell;

    private String _availableQuantityAcceptanceCell = "验收位可用库存（含本次验收）";
    private Double availableQuantityAcceptanceCell;
}
