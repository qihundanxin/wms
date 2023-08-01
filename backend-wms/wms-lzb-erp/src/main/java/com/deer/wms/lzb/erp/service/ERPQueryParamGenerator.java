package com.deer.wms.lzb.erp.service;

import com.deer.wms.ASN.manage.model.in.AcceptRecord;

public class ERPQueryParamGenerator {

    //入库节点推送参数
    public static String genInParam(AcceptRecord acceptRecord) {
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptRecord.getAsnBillNo() + "\",   // 必填 // wms的入库单号 (创建入库单的时候wms生成)\n" +
                "   \"serialNo\": \"1234\", // 必填 // 为了防止重复请求给ERP，同一个序列号 ERP只会入库一次\n" +
                "   \"labelBarcode\": \"" + acceptRecord.getShipBillNo() + "\",  // 必填  面单条码，此处填中国快递单号 (创建入库单的时候ERP传给wms)\n" +
                "   \"orderNo\": \"" + acceptRecord.getAsnBillNo() + "\", // 必填 // erp采购系统的订单号 (创建入库单的时候ERP传给wms)\n" +
                "   \"weight\": 2.45,  // 入库重量(单位g, 最多两位小数)\n" +
                "   \"length\": 1.12, // 长度(单位cm,最多两位小数)\n" +
                "   \"width\": 1.22, // 宽度(cm,最多两位小数)\n" +
                "   \"height\": 1.22, // 高度(cm,最多两位小数)\n" +
                "   \"operationTime\": \"" + acceptRecord.getAsnBillNo() + "\", // 操作时间\n" +
                "   \"operationUser\": \"" + acceptRecord.getAcceptUserName() + "\", // 操作人\n" +
                "   \"success\": true, // 入库成功(true) 入库异常(false)\n" +
                "   \"details\":  // 入库商品明细\n" +
                "   [\n" +
                "     {\n" +
                "      \"goodsNo\" : \"" + acceptRecord.getItemCode() + "\", // 商品编码\n" +
                "      \"goodsQuantity\": " + acceptRecord.getQuantity() + ", // 商品数量\n" +
                "      \"goodsBadQuantity\": 5, // 有多少个因为退件或者其他原因入不了库的\n" +
                "       \"qcNo\": \"1234\" // 没有就为空\n" +
                "     },\n" +
                "     {\n" +
                "     }\n" +
                "   ],\n" +
                "   \"errors\":   //  异常list集合\n" +
                "    [\n" +
                "      {\n" +
                "        \"exceptionCode\": \"\", // 必填 / 异常code\n" +
                "        \"exceptionDesc\": \"\", // 必填 // 异常描述\n" +
                "        \"exceptionImg\": [\n" +
                "          \"http://xxx.jpg\",\n" +
                "          \"http://yyy.jpg\"\n" +
                "        ],\n" +
                "        \"type\": 2, // 必填 // int类型，1:包裹异常;2:商品异常 （目前QC应该都是商品异常）\n" +
                "        \"goodsNo\" : \"\", // 异常商品编号\n" +
                "        \"goodsQuantity\": 4,  // 异常商品数量\n" +
                "        \"qcNo\": \"1234\" // qc编号\n" +
                "      },\n" +
                "      {\n" +
                "         \n" +
                "      }\n" +
                "    ]\n" +
                "}";
        return param;
    }

    //入库节点推送参数（成功）
    public static String genInTrueParam(AcceptRecord acceptRecord) {
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptRecord.getAsnBillNo() + "\",   // 必填 // wms的入库单号 (创建入库单的时候wms生成)\n" +
                "   \"serialNo\": \"1234\", // 必填 // 为了防止重复请求给ERP，同一个序列号 ERP只会入库一次\n" +
                "   \"labelBarcode\": \"" + acceptRecord.getShipBillNo() + "\",  // 必填  面单条码，此处填中国快递单号 (创建入库单的时候ERP传给wms)\n" +
                "   \"orderNo\": \"" + acceptRecord.getAsnBillNo() + "\", // 必填 // erp采购系统的订单号 (创建入库单的时候ERP传给wms)\n" +
                "   \"weight\": 2.45,  // 入库重量(单位g, 最多两位小数)\n" +
                "   \"length\": 1.12, // 长度(单位cm,最多两位小数)\n" +
                "   \"width\": 1.22, // 宽度(cm,最多两位小数)\n" +
                "   \"height\": 1.22, // 高度(cm,最多两位小数)\n" +
                "   \"operationTime\": \"" + acceptRecord.getAsnBillNo() + "\", // 操作时间\n" +
                "   \"operationUser\": \"" + acceptRecord.getAcceptUserName() + "\", // 操作人\n" +
                "   \"success\": true, // 入库成功(true) 入库异常(false)\n" +
                "   \"details\":  // 入库商品明细\n" +
                "   [\n" +
                "     {\n" +
                "      \"goodsNo\" : \"" + acceptRecord.getItemCode() + "\", // 商品编码\n" +
                "      \"goodsQuantity\": " + acceptRecord.getQuantity() + " // 商品数量\n" +
                "     }\n" +
                "   ]\n" +
                "}";
        return param;
    }
}
