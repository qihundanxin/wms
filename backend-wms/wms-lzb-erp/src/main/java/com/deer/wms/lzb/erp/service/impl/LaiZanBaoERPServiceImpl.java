package com.deer.wms.lzb.erp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.service.AcceptExImgService;
import com.deer.wms.ASN.manage.service.AcceptExService;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.ASN.manage.service.AsnShipDetailService;
import com.deer.wms.inventory.model.ErpRecord;
import com.deer.wms.lzb.erp.service.ErpRecordService;
import com.deer.wms.lzb.erp.service.LaiZanBaoERPService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.SO.SoMaster;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ll on 2020/02/06.
 */
@Service
@Transactional
@Slf4j
public class LaiZanBaoERPServiceImpl implements LaiZanBaoERPService {

    @Autowired
    private ErpRecordService erpRecordService;

    @Autowired
    private AsnShipDetailService asnShipDetailService;

    @Autowired
    private AcceptExImgService acceptExImgService;

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private AcceptRecordService acceptRecordService;
    @Value("${lzb.erp.baseUrl}")
    private String baseUrl;
    @Value("${lzb.erp.inUrl}")
    private String inUrl;
    @Value("${lzb.erp.outUrl}")
    private String outUrl;
    @Value("${lzb.erp.returnUrl}")
    private String returnUrl;
    @Value("${lzb.erp.token}")
    private String token;

    public void tongbuRRP() {
        List<AcceptRecord> acceptRecords = acceptRecordService.findCanTongbu();
        for (AcceptRecord acceptRecord : acceptRecords) {
            inTrue(acceptRecord);
        }

        List<AcceptEx> acceptExes = acceptExService.findCanTongBu();
        for (AcceptEx acceptEx : acceptExes) {
            inEx(acceptEx);
        }

    }

    //入库节点推送参数（成功）
    public void inTrue(AcceptRecord acceptRecord) {
        acceptRecord.setState(1);
        acceptRecordService.update(acceptRecord);
        AsnShipDetail asnShipDetail = asnShipDetailService
                .findBy("shipBillCode", acceptRecord.getShipBillNo());
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(1);
        erpRecordService.save(erpRecord);
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptRecord.getAsnBillNo() + "\", " +
                "   \"serialNo\": \"" + erpRecord.getErpRecordId() + "\", " +
                "   \"labelBarcode\": \"" + acceptRecord.getShipBillNo() + "\",  " +
                "   \"orderNo\": \"" + acceptRecord.getAsnBillNo() + "\", " +
                "   \"weight\": " + asnShipDetail.getWeight() + ",  " +
                "   \"length\": " + asnShipDetail.getLength() + ", " +
                "   \"width\": " + asnShipDetail.getWidth() + "," +
                "   \"height\": " + asnShipDetail.getHeight() + ", " +
                "   \"operationTime\": \"" + acceptRecord.getAsnBillNo() + "\", " +
                "   \"operationUser\": \"" + acceptRecord.getAcceptUserName() + "\", " +
                "   \"success\": true, " +
                "   \"details\":  " +
                "   [\n" +
                "     {\n" +
                "      \"goodsNo\" : \"" + acceptRecord.getItemCode() + "\", " +
                "      \"goodsQuantity\": " + acceptRecord.getQuantity() + " " +
                "     }\n" +
                "   ]\n" +
                "}";

        erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
        Map<String, String> map = requestERP(param, inUrl);
        String code = map.get("code");
        String message = map.get("msg");
        String success = map.get("success");
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);
            acceptRecord.setState(2);
            acceptRecordService.update(acceptRecord);
        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);

        }

    }

    //入库节点推送参数（异常）
    public void inEx(AcceptEx acceptEx) {
        acceptEx.setState(1);
        acceptExService.update(acceptEx);
        AsnShipDetail asnShipDetail = asnShipDetailService.findBy("shipBillCode", acceptEx.getShipBillNo());
        List<String> imgs = acceptExImgService.findByAcceptExId(acceptEx.getAcceptExId());
        String imgList = "";
        for (int i = 0; i < imgs.size(); i++) {
            String imgUrl = imgs.get(i);
            if (i == imgs.size() - 1) {
                imgList += "\"" + imgUrl + "\"";
            } else {
                imgList += "\"" + imgUrl + "\",";
            }
        }
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(2);
        erpRecordService.save(erpRecord);
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptEx.getBillNo() + "\",  " +
                "   \"serialNo\": \"" + erpRecord.getErpRecordId() + "\", " +
                "   \"labelBarcode\": \"" + acceptEx.getShipBillNo() + "\", " +
                "   \"orderNo\": \"" + acceptEx.getBillNo() + "\",  " +
                "   \"weight\": " + asnShipDetail.getWeight() + ",   " +
                "   \"length\": " + asnShipDetail.getLength() + ",  " +
                "   \"width\": " + asnShipDetail.getWidth() + ", " +
                "   \"height\": " + asnShipDetail.getHeight() + ",  " +
                "   \"operationTime\": \"" + acceptEx.getCreateTime() + "\",  " +
                "   \"operationUser\": \"" + acceptEx.getCreateUserName() + "\",  " +
                "   \"success\": false,  " +
                "   \"errors\":    " +
                "    [\n" +
                "      {\n" +
                "        \"exceptionCode\": \"" + acceptEx.getReason() + "\", " +
                "        \"exceptionDesc\": \"" + acceptEx.getReason() + "\",  " +
                "        \"exceptionImg\": [\n" +
                imgList +
                "        ],\n" +
                "        \"type\": 2,  " +
                "        \"goodsNo\" : \"" + acceptEx.getItemCode() + "\",  " +
                "        \"goodsQuantity\": " + acceptEx.getQuantity() + ",  " +
                "        \"qcNo\": \"" + acceptEx.getAcceptExId() + "\" " +
                "      }\n" +

                "    ]\n" +
                "}";
        Map<String, String> map = requestERP(param, inUrl);
        String code = map.get("code");
        String message = map.get("msg");
        String success = map.get("success");
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);
            acceptEx.setState(2);
            acceptExService.update(acceptEx);

        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);

        }
    }

    //出库节点推送参数（成功）
    public void out(SoMaster soMaster) {
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(3);
        erpRecordService.save(erpRecord);
        String param = "{\n" +
                "   \"deliveryCompanyBarcode\": \"" + soMaster.getShipBillCode() + "\",   " +
                "   \"deliveryCompanyCode\": \"" + soMaster.getShipCode() + "\",  " +
                "   \"serialNo\": \"" + erpRecord.getErpRecordId() + "\",  " +
                "   \"wmsNo\": \"" + soMaster.getBillNo() + "\",    " +
                "   \"billNo\": \"" + soMaster.getBillNo() + "\",  " +
                "   \"weight\": " + soMaster.getWeight() + ",   " +
                "   \"length\": " + soMaster.getLength() + ",  " +
                "   \"width\": " + soMaster.getWidth() + ", " +
                "   \"height\": " + soMaster.getHeight() + ",  " +
                "   \"operationTime\": \"" + soMaster.getCheckTime() + "\",  " +
                "   \"operationUser\": \"" + soMaster.getCheckUserName() + "\",  " +
                "   \"success\": true,  " +
                "   \"errorCode\" : \"\",\n" +
                "   \"errorMessage\": \"\"\n" +
                "}";
        Map<String, String> map = requestERP(param, outUrl);

    }

    //退件信息
    public void returnInfo(AcceptEx acceptEx) {
        acceptEx.setReturnState(1);
        acceptExService.update(acceptEx);
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(4);
        erpRecordService.save(erpRecord);
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptEx.getBillNo() + "\",  " +
                "   \"orderNo\": \"" + acceptEx.getBillNo() + "\",  " +
                "   \"productList\": [\n" +
                "     \"" + acceptEx.getItemCode() + "\"" +
                "   ],\n" +
                "   \"returnDeliveryCompany\": \"" + acceptEx.getReturnShipCode() + "\",  " +
                "   \"returnDeliveryNo\": \"" + acceptEx.getReturnShipBillCode() + "\",  " +
                "   \"operationTime\": \"" + acceptEx.getReturnTime() + "\",  " +
                "   \"operationUser\": \"" + acceptEx.getReturnUserName() + "\",  " +
                "   \"success\": true,  " +
                "   \"errorCode\" : \"\", " +
                "   \"errorMessage\": \"\" " +
                "}";
        Map<String, String> map = requestERP(param, returnUrl);
        String code = map.get("code");
        String message = map.get("msg");
        String success = map.get("success");
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);
            acceptEx.setReturnState(2);
            acceptExService.update(acceptEx);

        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.update(erpRecord);

        }
    }

    public Map<String, String> requestERP(String body, String url) {
        Map<String, String> map = new HashMap<>();
        try {
            url = baseUrl + url;
            log.info("Request ERP url [" + url + "] param [" + body + "]");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("access-token", token);
            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate client = new RestTemplate();
            ResponseEntity<String> response = client.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String res = response.getBody();
            JSONObject object = JSON.parseObject(res);
            Boolean success = object.getBoolean("success");
            String code = object.getString("code");
            String msg = object.getString("msg");
            map.put("success", success != null ? success.toString() : "false");
            map.put("msg", msg);
            map.put("code", code);
        } catch (Exception ex) {
            map.put("success", "false");
            map.put("msg", ex.getMessage());
            map.put("code", "500");
        }
        return map;
    }

}