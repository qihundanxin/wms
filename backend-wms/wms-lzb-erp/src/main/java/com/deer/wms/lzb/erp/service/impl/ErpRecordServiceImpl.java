package com.deer.wms.lzb.erp.service.impl;

import static com.deer.wms.project.root.util.FunctionUtil.nvl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

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
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.asn.AsnShipDetail;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.service.AcceptExImgService;
import com.deer.wms.ASN.manage.service.AcceptExService;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.ASN.manage.service.AsnShipDetailService;
import com.deer.wms.inventory.dao.ErpRecordMapper;
import com.deer.wms.inventory.model.ErpRecord;
import com.deer.wms.inventory.model.ErpRecordCriteria;
import com.deer.wms.inventory.model.InventoryAdjustDetailDto;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.service.InventoryAdjustDetailService;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.lzb.erp.model.ErpInBoundGoodsData;
import com.deer.wms.lzb.erp.model.ErpInBoundNotifyData;
import com.deer.wms.lzb.erp.service.ErpRecordService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.DeerHttpClient;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.service.SoMasterService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by  on 2021/02/14.
 */
@Service
@Transactional
@Slf4j
public class ErpRecordServiceImpl extends AbstractService<ErpRecord, Integer> implements ErpRecordService {

    @Autowired
    private ErpRecordMapper erpRecordMapper;

    @Override
    public List<ErpRecord> findList(ErpRecordCriteria criteria) {
        return erpRecordMapper.findList(criteria);
    }

    @Autowired
    private ErpRecordService erpRecordService;

    @Autowired
    private AsnShipDetailService asnShipDetailService;

    @Autowired
    private AcceptExImgService acceptExImgService;

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AcceptRecordService acceptRecordService;

    @Resource
    private InventoryAdjustDetailService inventoryAdjustDetailService;

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

        List<SoMaster> soMasters = soMasterService.findCanTongBu();
        for (SoMaster soMaster : soMasters) {
            out(soMaster);
        }

    }

    //入库节点推送参数（成功）
    public void inTrue(AcceptRecord acceptRecord) {

        acceptRecord.setState(1);
        acceptRecordService.update(acceptRecord);
        AsnShipDetail asnShipDetail = asnShipDetailService
                .findBy("shipBillCode", acceptRecord.getShipBillNo());
        Organization organization = organizationService.findById(acceptRecord.getOrganizationId());
        ErpInBoundNotifyData notifyData = new ErpInBoundNotifyData();
        notifyData.setWmsNo(acceptRecord.getAsnBillNo());
        notifyData.setOrgCode(organization.getOrganizationCode());
        notifyData.setSerialNo("AcceptRecord-" + acceptRecord.getAcceptRecordId());
        notifyData.setLabelBarcode(acceptRecord.getShipBillNo());
        notifyData.setOrderNo(acceptRecord.getAsnBillNo());
        if (asnShipDetail.getWeight() != null) {
            notifyData.setWeight(new BigDecimal(asnShipDetail.getWeight()));
        }
        if (asnShipDetail.getLength() != null) {
            notifyData.setLength(new BigDecimal(asnShipDetail.getLength()));
        }
        if (asnShipDetail.getWidth() != null) {
            notifyData.setWidth(new BigDecimal(asnShipDetail.getWidth()));
        }
        if (asnShipDetail.getHeight() != null) {
            notifyData.setHeight(new BigDecimal(asnShipDetail.getHeight()));
        }
        notifyData.setOperationTime(acceptRecord.getAcceptTime());
        notifyData.setOperationUser(acceptRecord.getAcceptUserName());
        notifyData.setSuccess(true);

        List<ErpInBoundGoodsData> details = new ArrayList<>();
        ErpInBoundGoodsData detail = new ErpInBoundGoodsData();
        details.add(detail);
        detail.setGoodsNo(acceptRecord.getItemCode());
        if (acceptRecord.getAcceptExId() != null) {
            detail.setQcNo(String.valueOf(acceptRecord.getAcceptExId()));
        }
        if (acceptRecord.getQuantity() != null) {
            detail.setGoodsQuantity(acceptRecord.getQuantity().intValue());
        }

        notifyData.setDetails(details);
        String param = JSON.toJSONString(notifyData);

        Map<String, String> map = requestERP(param, inUrl);
        String code = map.get("code");
        String message = map.get("msg");
        String success = map.get("success");
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(1);
        erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);
            acceptRecord.setState(2);
            acceptRecordService.update(acceptRecord);
        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);

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
        Organization organization = organizationService.findById(acceptEx.getOrganizationId());
        String param = "{\n" +
                "   \"wmsNo\": \"" + acceptEx.getBillNo() + "\",  " +
                "   \"orgCode\": \"" + organization.getOrganizationCode() + "\", " +
                "   \"serialNo\": \"" + "AcceptEx-" + acceptEx.getAcceptExId() + "\", " +
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
                "        \"exceptionDesc\": \"" + acceptEx.getMemo() + "\",  " +
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
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(2);
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);
            acceptEx.setState(2);
            acceptExService.update(acceptEx);

        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);

        }

    }

    @Autowired
    private SoMasterService soMasterService;

    //出库节点推送参数（成功）
    public void out(SoMaster soMaster) {

        soMaster.setErpState(1);
        Organization organization = organizationService.findById(soMaster.getOrganizationId());
        String param = "{\n" +
                "   \"deliveryCompanyBarcode\": \"" + soMaster.getShipBillCode() + "\",   " +
                "   \"orgCode\": \"" + organization.getOrganizationCode() + "\", " +
                "   \"deliveryCompanyCode\": \"" + soMaster.getShipCode() + "\",  " +
                "   \"serialNo\": \"" + "SoMaster-" + soMaster.getSoMasterId() + "\",  " +
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
        String code = map.get("code");
        String message = map.get("msg");
        String success = map.get("success");
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(3);
        if (success.equals("true")) {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(2);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);
            soMaster.setErpState(2);
            soMasterService.update(soMaster);

        } else {
            erpRecord.setCode(map.get("code"));
            erpRecord.setMsg(map.get("msg"));
            erpRecord.setParam(param);
            erpRecord.setState(1);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecordService.save(erpRecord);
            soMasterService.update(soMaster);
        }

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
            log.info("调用erp接口， 返回值：{}", res);
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
            log.error("请求erp失败", ex);
        }
        return map;
    }

    @Resource
    private WareInfoService wareInfoService;
    @Autowired
    private InventoryService inventoryService;
    @Value("${lzb.erp.synStockUrl}")
    private String synStockUrl;

    @Override
    public void inventoryAdjust(InventoryAdjustDetailDto detail) {
        if (detail.getWareId() == null) {
            Inventory queryInventory = inventoryService.findById(detail.getInventoryId());
            detail.setWareId(queryInventory.getWareId());
            detail.setOrganizationId(queryInventory.getOrganizationId());
        }
        // 查询item各库位的库存及库位
        List<InventoryDto> inventoryCells = inventoryService.getInventoryByCellType(
                detail.getItemCode(), detail.getWareId(), detail.getOrganizationId()
        );
        JSONObject request = new JSONObject();
        request.put("itemCode", detail.getItemCode());
        WareInfo wareInfo = wareInfoService.findById(detail.getWareId());
        request.put("wareCode", wareInfo.getWareCode());
        Organization organization = organizationService.findById(detail.getOrganizationId());
        request.put("orgCode", organization.getOrganizationCode());
        request.put("serialNo", "InventoryAdjust-" + detail.getAdjustDetailId());
        JSONArray inventoryListJson = new JSONArray();
        for (InventoryDto item : inventoryCells) {
            JSONObject itemJson = new JSONObject().fluentPut("cellCode", item.getCellCode());
            if (Objects.equals(item.getInventoryId(), detail.getInventoryId())) {
                if (Objects.equals(nvl(item.getSumQuantity() - nvl(detail.getDifferQuantity())), nvl(detail.getAdjustQuantity()))) {
                    // 库存没变化不需要同步
                    log.info("库存同步时库存数量未发生变化，inventoryAdjustDetailId:{}", detail.getAdjustDetailId());
                    // 改为已同步
                    detail.setSynErp(1);
                    inventoryAdjustDetailService.update(detail);
                    return;
                }
                itemJson.put("beforeQuantity", nvl(item.getSumQuantity()) - nvl(detail.getDifferQuantity()));
                itemJson.put("quantity", nvl(detail.getAdjustQuantity()));
            } else {
                itemJson.put("beforeQuantity", nvl(item.getSumQuantity()));
                itemJson.put("quantity", nvl(item.getSumQuantity()));
            }
            inventoryListJson.add(itemJson);
        }
        request.put("inventoryList", inventoryListJson);
        ErpRecord erpRecord = new ErpRecord();
        erpRecord.setType(5);
        erpRecord.setCreateTime(DateUtils.getNowDateTimeString());
        // 发送请求
        try {
            String params = request.toJSONString();
            erpRecord.setParam(params);
            erpRecord.setRequestTime(DateUtils.getNowDateTimeString());
            Map<String, String> map = DeerHttpClient.requestERP(params, baseUrl + synStockUrl, HttpMethod.POST, token);
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            if (StringUtils.equals("true", map.get("success"))) {
                erpRecord.setCode(map.get("code"));
                erpRecord.setMsg(map.get("msg"));
                erpRecord.setState(2);
                detail.setSynErp(1);
            } else {
                erpRecord.setCode(map.get("code"));
                erpRecord.setMsg(map.get("msg"));
                erpRecord.setState(1);

            }
        } catch (Exception e) {
            erpRecord.setReturnTime(DateUtils.getNowDateTimeString());
            erpRecord.setState(1);
            erpRecord.setCode("500");
            erpRecord.setMsg(e.getMessage());
        }
        erpRecordMapper.insert(erpRecord);
        inventoryAdjustDetailService.update(detail);
    }

}
