package com.deer.wms.ware.task.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.bstek.ureport.Utils;
import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import com.deer.wms.base.system.enums.SendState;
import com.deer.wms.base.system.model.WareShop;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.WareShopService;
import com.deer.wms.busine.tactic.config.PassQuanConfig;
import com.deer.wms.busine.tactic.config.PassQuanConfig.PassQuanPathConfig;
import com.deer.wms.busine.tactic.model.RouteTactic;
import com.deer.wms.busine.tactic.model.RouteTacticCriteria;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.busine.tactic.service.RouteTacticService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.constant.DataOperationLogModule;
import com.deer.wms.project.root.constant.DataOperationLogOperation;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DataOperationLogTool;
import com.deer.wms.project.root.util.DataOperationLogTool.DataOperationLog;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.HttpClientUtil;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.SSLClient2;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.OrganizationService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.constant.WareTaskConstant;
import com.deer.wms.ware.task.dao.SoMasterMapper;
import com.deer.wms.ware.task.model.PickTaskReturn;
import com.deer.wms.ware.task.model.SO.PdaSearchSoData;
import com.deer.wms.ware.task.model.SO.SoData;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.model.SO.SoDetailCriteria;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.model.SO.SoDetailFile;
import com.deer.wms.ware.task.model.SO.SoMaster;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SO.SoMasterFile;
import com.deer.wms.ware.task.model.SO.SoMasterYdyp;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSo;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSoDeliveryAddress;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSoShippingAddress;
import com.deer.wms.ware.task.model.SO.CreateSo.CreateSoSkuDetail;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendOutCriteria;
import com.deer.wms.ware.task.model.Wave.WaveDetail;
import com.deer.wms.ware.task.model.Wave.WaveMaster;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.model.xhcOrderInfo.Items;
import com.deer.wms.ware.task.model.xhcOrderInfo.Order;
import com.deer.wms.ware.task.model.xhcOrderInfo.Shipping_directions;
import com.deer.wms.ware.task.service.PickTaskReturnService;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SendDetailService;
import com.deer.wms.ware.task.service.So2AsnConnector;
import com.deer.wms.ware.task.service.SoDetailFileService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterFileService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.deer.wms.ware.task.service.WaveDetailService;
import com.deer.wms.ware.task.service.WaveMasterService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by guo on 2020/02/02.
 */
@Service
@Transactional
@Slf4j
public class SoMasterServiceImpl extends AbstractService<SoMaster, Integer> implements SoMasterService {

    @Autowired
    private DataOperationLogTool doLogger;

    @Autowired
    private SoMasterMapper soMasterMapper;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private SoMasterService soMasterService;

    @Autowired
    private SoDetailService soDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private SoMasterFileService soMasterFileService;

    @Autowired
    private SoDetailFileService soDetailFileService;
    @Autowired
    private RouteTacticService routeTacticService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private WareShopService wareShopService;

    @Autowired
    private PackDetailService packDetailService;

    @Autowired
    private So2AsnConnector so2AsnConnector;

    @Autowired
    private SendDetailService sendDetailService;

    @Value("${file.staticFileDir}")
    private String staticFileDir;

    @Value("${file.pdfPath}")
    private String pdfPath;

    @Autowired
    private PassQuanConfig passQuanConfig;

    @Autowired
    private PassQuanPathConfig passQuanPathConfig;

    @Value("${wms.host}")
    private String wmsHost;

    @Value("${wms.path.webhook-so-order}")
    private String wmsWebhookSoOrderPath;

    @Value("${pdf-invoice.host}")
    private String pdfInvoiceHost;

    @Value("${pdf-invoice.path.get-pdf}")
    private String pdfInvoiceGetPdfPath;


    @Autowired
    private WaveMasterService waveMasterService;
    @Autowired
    private WaveDetailService waveDetailService;
    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PickTaskReturnService pickTaskReturnService;
    @Autowired
    private WareInfoService wareInfoService;
    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PrintTempInfoService printTempInfoService;

    public void getOrder10(Order order) {
        List<com.deer.wms.ware.task.model.xhcOrderInfo.Items> list = order.getItems();
        Long time1 = System.currentTimeMillis();

        SoMaster soMaster = new SoMaster();
        // soMaster.setOrderTime(String.valueOf(order.getLocal_time()));
        soMaster.setBillNo("SO-" + String.valueOf(order.getId()));
        soMaster.setTotalPrice(order.getGrand_total());
        soMaster.setAllotBillNo(String.valueOf(order.getId()));
        soMaster.setShopDailyCount(order.getShop_daily_count());
        // soMaster.setDetailAddress(order.getData().getShipping_directions().getEnd_address());
        // berwin add note: pickup-order+null address
        String detailAddress = "";
        if (null != order.getData().getShipping_directions()) {
            Shipping_directions sdObj = null;
            try {
                sdObj = JSON.parseObject(order.getData().getShipping_directions(), Shipping_directions.class);
            } catch (Exception e) {}
            if (sdObj != null) {
                detailAddress = sdObj.getEnd_address();
            }
        }

        if (detailAddress != null && !detailAddress.equals("")) {
            String splitStr = "/u002B";
            String[] detailAddresss = detailAddress.split(splitStr);
            detailAddress = "";
            for (String address : detailAddresss) {
                detailAddress += address;
                detailAddress += " ";
            }
        }
        soMaster.setDetailAddress(detailAddress);
        // 2022-6-3
        String paymentState = order.getPayment_type();
        if (paymentState.equals("cashpay")) {
            paymentState = "Pay on Delivery";
        } else {
            paymentState = "Paid";
        }
        soMaster.setSoUdfHs1(paymentState);

        if (null != order.getComments() && null != order.getComments().getUser() && "" != order.getComments().getUser()) {
            soMaster.setMemo(order.getComments().getUser().replace("\\n", " "));
        }

        List<SoDetail> oldSoDetails = soDetailService.findByBillNo(soMaster.getBillNo());
        if (oldSoDetails.size() > 0) {
            return;
        }

        String createTime = DateUtils.getNowDateTimeString();

        Integer shopId = order.getShop_id();
        WareShop wareShop = wareShopService.findBy("shopId", shopId);
        WareInfo wareInfo;
        if (wareShop == null) {
            return;
        } else {
            wareInfo = wareInfoService.findById(wareShop.getWareId());

        }

        Organization organization = organizationService.findBy("organizationCode", "001");

        soMaster.setOrderTime(DateUtils.stampToDateString(String.valueOf(order.getCreated_at())));

        if (wareInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库编码请检查");
        }
        if (organization == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货主编码请检查");
        }
        Integer state = 2; // 预分配已完成
        soMaster.setState(state);
        soMaster.setCreateTime(createTime);
        soMaster.setCreateUserId(206);
        soMaster.setCreateUserName("xhctest");
        soMaster.setWareId(wareInfo.getWareId());
        soMaster.setWareName(wareInfo.getWareName());
        soMaster.setWareId(wareInfo.getWareId());
        soMaster.setWareCode(wareInfo.getWareCode());
        soMaster.setOrganizationCode(organization.getOrganizationCode());
        soMaster.setOrganizationName(organization.getOrganizationName());
        soMaster.setOrganizationId(organization.getOrganizationId());
        soMaster.setAllotBillNo(String.valueOf(order.getId()));
        soMaster.setInsertType(4);
        soMaster.setBillSource(4); // 4-接口进入
        soMaster.setOrderTime(DateUtils.getNowDateTimeString());
        soMaster.setSoStructure(list.size()); // 该订单的产品数量
        soMasterService.save(soMaster);

        List<SoDetail> soDetails = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            com.deer.wms.ware.task.model.xhcOrderInfo.Items items = list.get(i);
            SoDetail soDetail = new SoDetail();
            soDetail.setOrderQuantity(Double.valueOf(items.getQty())); // 订货数
            ItemInfo itemInfo;
            String sku;
            Long itemId;
            String itemName;
            String imgUrl;
            try {
                sku = items.getRevision().getSku();
                if (null == sku || sku.trim().equals("")) {
                    sku = "匹配不到该商品";
                    itemName = "匹配不到该商品,请检查Pass圈产品SKU";
                    imgUrl = "匹配不到该商品,请检查Pass圈产品SKU";
                    itemId = Long.valueOf(0000000000);
                } else {
                    itemId = items.getRevision().getProductId(); // 商品条形码
                    itemName = items.getRevision().getName();
                    imgUrl = items.getRevision().getImage();
                }
            } catch (Exception e) {
                sku = "匹配不到该商品";
                itemName = "匹配不到该商品,请检查Pass圈产品SKU";
                imgUrl = "匹配不到该商品,请检查Pass圈产品SKU";
                itemId = Long.valueOf(0000000000);

            }
            List<ItemInfo> itemInfos = itemInfoService.findBySkuOrUpc(sku);
            if (itemInfos.size() == 0) {
                itemInfo = new ItemInfo();
                itemInfo.setImgUrl(imgUrl);
                itemInfo.setItemCode(sku);
                itemInfo.setItemBarCode(String.valueOf(itemId));
                itemInfo.setOrganizationId(organization.getOrganizationId());
                itemInfo.setItemName(itemName);
                itemInfo.setPackCode("sys_pack"); // 包装编码
                itemInfo.setBatchTacticCode("sys_batch"); // 批次策略代码
                itemInfoService.save(itemInfo);
                // TODO xuesinuo 新增item的DoLog
            } else {
                itemInfo = itemInfos.get(0);
            }
            soDetail.setBillNo(soMaster.getBillNo());
            soDetail.setSoMasterId(soMaster.getSoMasterId());
            soDetail.setDetailNo(soMaster.getBillNo() + (i + 1));
            soDetail.setDetailIndex(i + 1);
            soDetail.setItemCode(itemInfo.getItemCode());
            soDetail.setItemName(itemInfo.getItemName());
            soDetail.setSpec(itemInfo.getSpec());
            soDetail.setModel(itemInfo.getModel());
            soDetail.setImgUrl(itemInfo.getImgUrl());
            String packCode = itemInfo.getPackCode();
            if (packCode == null || packCode.trim().equals("")) {
                packCode = "sys_pack";
            }
            List<PackDetail> packDetails = packDetailService.findByCode(packCode);
            PackDetail packDetail = new PackDetail();
            for (PackDetail packDetail1 : packDetails) {
                if (packDetail1.getState() && packDetail1.getOutboundLabel()) {
                    packDetail = packDetail1;
                }
            }
            if (packDetail.getPackDetailId() == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "SKU：" + soDetail.getItemCode() + "未设置销售单位");
            }
            soDetail.setPackDetailId(packDetail.getPackDetailId()); // 包装id
            soDetail.setTransRatio(packDetail.getTransRatio()); // 包装转化比
            soDetail.setPackDescribe(packDetail.getPackDescribe()); // 包装描述
            soDetail.setOrderQuantity(Double.valueOf(items.getQty()) * packDetail.getTransRatio()); // 订单数量
            soDetailService.save(soDetail);
            soDetails.add(soDetail);
        }

        // 添加SO单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(soMaster.getBillNo());
        billRecord.setRecordType(22);// SO新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(206);
        billRecord.setCreateUserName("xhctest");
        billRecordService.save(billRecord);

        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<SoData> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.SO);
        doLog.setOperation(DataOperationLogOperation.SO_ADD);
        doLog.setOperationUserId(-1L);
        doLog.setOperationUserName("系统自动触发");
        // DoLog选填项
        SoData soData = new SoData();
        soData.setSoMaster(soMaster);
        soData.setSoDetails(soDetails);
        doLog.setAfterData(soData);
        doLog.setOperationCode(soMaster.getBillNo());
        doLog.setOperationId(soMaster.getSoMasterId().longValue());
        doLog.setTimer(time2 - time1);
        doLog.setWareId(soMaster.getWareId().longValue());
        doLogger.save(doLog);
    }

    public void getOrder11(Order order) {
        Long time1 = System.currentTimeMillis();
        SoMaster soMaster = soMasterService.findBy("allotBillNo", String.valueOf(order.getId()));
        if (soMaster == null) {
            // 订单新增
            getOrder10(order);
            return;
        }
        SoMaster oldSoMaster = new SoMaster();
        BeanUtils.copyBeanProp(oldSoMaster, soMaster);
        List<com.deer.wms.ware.task.model.xhcOrderInfo.Items> list = order.getItems();
        soMaster.setBillNo("SO-" + String.valueOf(order.getId()));
        soMaster.setTotalPrice(order.getGrand_total());
        soMaster.setAllotBillNo(String.valueOf(order.getId()));
        // berwin add note: pickup-order+null address
        String detailAddress = "";
        if (null != order.getData().getShipping_directions()) {
            Shipping_directions sdObj = null;
            try {
                sdObj = JSON.parseObject(order.getData().getShipping_directions(), Shipping_directions.class);
            } catch (Exception e) {}
            if (sdObj != null) {
                detailAddress = sdObj.getEnd_address();
            }
        }
        if (detailAddress == null || detailAddress.equals("")) {
            detailAddress = "";
        } else {
            String splitStr = "/u002B";
            String[] detailAddresss = detailAddress.split(splitStr);
            detailAddress = "";
            for (String address : detailAddresss) {
                detailAddress += address;
                detailAddress += " ";
            }
        }
        soMaster.setDetailAddress(detailAddress);
        String paymentState = order.getPayment_type();
        if (paymentState.equals("cashpay")) {
            paymentState = "Pay on Delivery";
        } else {
            paymentState = "Paid";
        }
        soMaster.setSoUdfHs1(paymentState);
        soMaster.setShopDailyCount(order.getShop_daily_count());
        String createTime = DateUtils.getNowDateTimeString();
        Organization organization = organizationService.findBy("organizationCode", "001");
        soMaster.setOrderTime(DateUtils.stampToDateString(String.valueOf(order.getCreated_at())));
        if (organization == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货主编码请检查");
        }
        soMaster.setInsertType(4);
        soMaster.setBillSource(4); // 4-接口进入
        soMaster.setSoStructure(list.size());
        soMasterService.update(soMaster);
        List<SoDetail> oldSoDetails = soDetailService.findByBillNo(soMaster.getBillNo());
        List<SoDetail> soDetails = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Items items = list.get(i);
            SoDetail soDetail = new SoDetail();
            ItemInfo itemInfo;
            List<ItemInfo> itemInfos = itemInfoService.findBySkuOrUpc(items.getRevision().getSku());
            if (itemInfos.size() == 0) {
                itemInfo = new ItemInfo();
                itemInfo.setImgUrl(items.getRevision().getImage());
                itemInfo.setItemCode(items.getRevision().getSku());
                itemInfo.setItemBarCode(String.valueOf(items.getRevision().getProductId()));
                itemInfo.setOrganizationId(organization.getOrganizationId());
                itemInfo.setItemName(items.getRevision().getName());
                itemInfo.setPackCode("sys_pack");
                itemInfo.setBatchTacticCode("sys_batch");
                itemInfoService.save(itemInfo);
                // TODO xuesinuo 新增item的DoLog
            } else {
                itemInfo = itemInfos.get(0);
            }
            for (SoDetail detail : oldSoDetails) {
                if (detail.getItemCode().equals(items.getRevision().getSku())) {
                    soDetail = detail;
                }
            }
            if (soDetail == null || soDetail.getVersion() == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, order.getId() + "订单内商品不存在于WMS。SKU：" + items.getRevision().getSku() + "在wms中找不到改sku商品");
            }
            String packCode = itemInfo.getPackCode();
            if (packCode == null || packCode.trim().equals("")) {
                packCode = "sys_pack";
            }
            List<PackDetail> packDetails = packDetailService.findByCode(packCode);
            PackDetail packDetail = new PackDetail();
            for (PackDetail packDetail1 : packDetails) {
                if (packDetail1.getState() && packDetail1.getOutboundLabel()) {
                    packDetail = packDetail1;
                }
            }
            if (packDetail.getPackDetailId() == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "SKU：" + soDetail.getItemCode() + "未设置销售单位");
            }
            soDetail.setOrderQuantity(Double.valueOf(items.getQty()) * packDetail.getTransRatio());
            soDetailService.update(soDetail);
            soDetails.add(soDetail);
        }

        // 添加SO单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(soMaster.getBillNo());
        billRecord.setRecordType(5);// SO修改
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(206);
        billRecord.setCreateUserName("xhctest");
        billRecordService.save(billRecord);

        // DoLog
        Long time2 = System.currentTimeMillis();
        DataOperationLog<SoData> doLog = new DataOperationLogTool.DataOperationLog<>();
        // DoLog必填项
        doLog.setModule(DataOperationLogModule.SO);
        doLog.setOperation(DataOperationLogOperation.SO_SET);
        doLog.setOperationUserId(-1L);
        doLog.setOperationUserName("系统自动触发");
        // DoLog选填项
        SoData afterData = new SoData();
        afterData.setSoMaster(soMaster);
        afterData.setSoDetails(soDetails);
        doLog.setAfterData(afterData);
        SoData beforeData = new SoData();
        beforeData.setSoMaster(oldSoMaster);
        beforeData.setSoDetails(oldSoDetails);
        doLog.setBeforeData(beforeData);
        doLog.setOperationCode(soMaster.getBillNo());
        doLog.setOperationId(soMaster.getSoMasterId().longValue());
        doLog.setTimer(time2 - time1);
        doLog.setWareId(soMaster.getWareId().longValue());
        doLogger.save(doLog);
    }

    public void cancel(SoMaster soMaster, CurrentUser currentUser) {
        /*
         * 查找订单 改变订单状态 把这个订单从该波次里面挑出去 查找订单所有拣货任务 还没有拣的任务 取消掉，恢复所分配的库存 已经拣过的任务，生成还货任务
         */
        WaveDetail waveDetail = null;
        WaveMaster waveMaster = null;
        waveDetail = waveDetailService.findBy("soMasterId", soMaster.getSoMasterId());
        if (waveDetail != null) {
            waveMaster = waveMasterService.findById(waveDetail.getWaveMasterId());
        }
        soDetailService.cancel(soMaster.getBillNo());

        List<PickTask> exPickTasks = pickTaskService.findBySoMasterId(soMaster.getSoMasterId());
        for (PickTask exPickTask : exPickTasks) {
            // 还没拣货,而且不是父任务
            if (exPickTask.getState() == 0 && exPickTask.getIsParent() == 0) {
                // 这里没拣的 放到定时任务里面去处理 以防冲突
                exPickTask.setState(-1);
                exPickTask.setWaveMasterId(null);
                pickTaskService.update(exPickTask);

                Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                inventoryService.update(inventory);
            }
            // 被合并 而且还没拣
            else if (exPickTask.getState() == 4) {

                if (exPickTask.getParentTaskId() != null) {
                    PickTask parent = pickTaskService.getTaskById(exPickTask.getParentTaskId());
                    parent.setPickQuantity(parent.getPickQuantity() - exPickTask.getPickQuantity());
                    if (parent.getPickQuantity() > 0.0) {
                        pickTaskService.update(parent);
                    } else {
                        pickTaskService.deleteByIdVersion(parent.getPickTaskId(), parent.getVersion());
                    }
                }
                exPickTask.setState(-1);
                exPickTask.setWaveMasterId(null);
                exPickTask.setParentTaskId(null);
                pickTaskService.update(exPickTask);
                Inventory inventory = inventoryService.findById(exPickTask.getInventoryId());
                inventory.setAllotQuantity(inventory.getAllotQuantity() - exPickTask.getPickQuantity());
                inventoryService.update(inventory);
            }
            // 已经拣过了
            else if (exPickTask.getState() == 2) {
                exPickTask.setState(-1);
                exPickTask.setParentTaskId(null);
                pickTaskService.update(exPickTask);

                PickTaskReturn pickTaskReturn = new PickTaskReturn();
                pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                pickTaskReturn.setWareId(exPickTask.getWareId());
                pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
                pickTaskReturn.setItemCode(exPickTask.getItemCode());
                pickTaskReturn.setItemName(exPickTask.getItemName());
                pickTaskReturn.setBoxCode(exPickTask.getBoxCode());
                pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
                pickTaskReturn.setFromInventoryId(exPickTask.getInventoryId());
                pickTaskReturn.setToCellCode(exPickTask.getCellCode());
                // pickTaskReturn.setFromCellCode(exPickTask.getCellCode());
                pickTaskReturn.setQuantity(exPickTask.getPickQuantity());
                pickTaskReturn.setState(0);
                pickTaskReturn.setType(2);
                pickTaskReturn.setBillNo(soMaster.getBillNo());
                pickTaskReturn.setShipBillNo(soMaster.getShipBillCode());
                if (currentUser != null) {
                    pickTaskReturn.setCreateUserId(currentUser.getUserId());
                    pickTaskReturn.setCreateUserName(currentUser.getUserName());
                } else {
                    pickTaskReturn.setCreateUserName("系统取消");
                }

                pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
                pickTaskReturn.setWaveMasterId(exPickTask.getWaveMasterId());
                if (waveDetail != null && waveMaster != null) {
                    pickTaskReturn.setWaveMasterId(waveDetail.getWaveMasterId());
                    pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
                }
                if (exPickTask.getBoxCode() != null) {
                    pickTaskReturn.setBoxCode(exPickTask.getBoxCode());
                }
                pickTaskReturnService.save(pickTaskReturn);
            }
            // 已经发运了
            else if (exPickTask.getState() == 5) {
                exPickTask.setState(-1);
                exPickTask.setParentTaskId(null);
                pickTaskService.update(exPickTask);

                PickTaskReturn pickTaskReturn = new PickTaskReturn();
                pickTaskReturn.setFromCellCode(exPickTask.getToCellCode());
                pickTaskReturn.setWareId(exPickTask.getWareId());
                pickTaskReturn.setOrgId(exPickTask.getOrganizationId());
                pickTaskReturn.setItemCode(exPickTask.getItemCode());
                pickTaskReturn.setItemName(exPickTask.getItemName());
                pickTaskReturn.setImgUrl(exPickTask.getImgUrl());
                // pickTaskReturn.setFromInventoryId(exPickTask.getInventoryId());
                pickTaskReturn.setToCellCode(exPickTask.getCellCode());
                pickTaskReturn.setQuantity(exPickTask.getPickQuantity());
                pickTaskReturn.setBoxCode(exPickTask.getBoxCode());
                pickTaskReturn.setState(0);
                pickTaskReturn.setType(3);
                pickTaskReturn.setBillNo(soMaster.getBillNo());
                pickTaskReturn.setShipBillNo(soMaster.getShipBillCode());
                if (currentUser != null) {
                    pickTaskReturn.setCreateUserId(currentUser.getUserId());
                    pickTaskReturn.setCreateUserName(currentUser.getUserName());
                } else {
                    pickTaskReturn.setCreateUserName("系统取消");
                }
                pickTaskReturn.setCreateTime(DateUtils.getNowDateTimeString());
                if (waveDetail != null && waveMaster != null) {
                    pickTaskReturn.setWaveMasterId(waveDetail.getWaveMasterId());
                    pickTaskReturn.setBoxCode(waveMaster.getBoxCode());
                }

                pickTaskReturnService.save(pickTaskReturn);
            }

        }
        soMaster.setState(-1);
        soMasterService.updateState(soMaster);
        if (waveDetail != null) {
            waveDetailService.deleteById(waveDetail.getWaveDetailId());
        }

        // 添加SO单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(soMaster.getBillNo());
        billRecord.setRecordType(-1);// SO修改
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(206);
        billRecord.setCreateUserName("xhctest");
        billRecordService.save(billRecord);
    }

    @Override
    public void updateState(SoMaster soMaster) {
        soMasterMapper.updateState(soMaster);
        if (soMaster.getState() >= 4 && soMaster.getWaveMasterId() != null) {
            // waveMasterService.waveStateBySo(soMaster.getWaveMasterId());
        }
    }

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;

    @Override
    public String getPdfUrl(SoMaster soMaster) throws IOException {
        String photoUrl = pdfInvoiceHost + pdfInvoiceGetPdfPath + "?ids=" + soMaster.getAllotBillNo();
        String fileName = soMaster.getAllotBillNo();
        // 保存目录
        saveUrlAs2(photoUrl, fileSetting.getStaticFileDir() + fileSetting.getPdfPath(), fileName + ".pdf", "GET");

        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SO_INVOICE, soMaster.getAllotBillNo() + ".pdf",
                new File(fileSetting.getStaticFileDir() + fileSetting.getPdfPath() + "/" + fileName + ".pdf"));
        return fileS3Tool.getUrl(fullPath);
    }

    @Override
    public String soPickTask(SoMaster soMaster, String driverName, String serialNumber) {
        HtmlReport htmlReport;
        if (null == driverName || "无".equals(driverName) || driverName.isEmpty()) {
            ExportManager exportManager = (ExportManager) Utils.getApplicationContext().getBean(ExportManager.BEAN_ID);
            String fileName = "mysql:soPickTask.ureport.xml";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("billNo", soMaster.getBillNo());
            htmlReport = exportManager.exportHtml(fileName, "/wms", parameters);
        } else {
            ExportManager exportManager = (ExportManager) Utils.getApplicationContext().getBean(ExportManager.BEAN_ID);
            String fileName = "mysql:soPickTask.ureport.xml";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("billNo", soMaster.getBillNo());
            htmlReport = exportManager.exportHtml(fileName, "/wms", parameters);
        }

        return htmlReport.getContent() + "<style>" + htmlReport.getStyle() + "</style>";
    }

    /**
     * @从制定URL下载文件并保存到指定目录
     * @param filePath 文件将要保存的目录
     * @param method   请求方法，包括POST和GET
     * @param url      请求的路径
     * @return
     */

    public static void saveUrlAs2(String url, String filePath, String fileName, String method) throws IOException {
        // System.out.println("fileName---->"+filePath);
        // 创建不同的文件夹目录
        File file = new File(filePath);
        // 判断文件夹是否存在
        if (!file.exists()) {
            // 如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
//        try
        {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            // 以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            // 连接指定的资源
            conn.connect();
            // 获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            // 判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            // 写入到文件（注意文件保存路径的后面一定要加上文件的名称）

            fileOut = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            // 保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.out.println("抛出异常！！");
//        }

        // return file;
    }

    public static String saveUrlAs(String url, String path, String fileName, String aaa) {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpClient httpclient = SSLClient2.getHttpClientWithoutSslValidation()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                if (response != null) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity.getContentType().toString().equals("Content-Type: application/pdf")) {
                        byte[] byteData = EntityUtils.toByteArray(resEntity);
                        getFile(byteData, path, fileName);
                        result = "0";
                    } else {
                        byte[] byteData = EntityUtils.toByteArray(resEntity);
                        result = byteToString(byteData);
                    }
                }
            }
            return result;
        } catch (Exception exception) {
            log.error("调用访问https工具类, 抛错：", exception);
        }
        return result;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                // 如果文件夹不存在，则创建新的的文件夹
                dir.mkdirs();
            }
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        String strContent = "";
        try {
            strContent = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strContent;
    }

    @Override
    public List<SoMasterDto> findList(SoMasterCriteria criteria) {
        return soMasterMapper.findList(criteria);
    }

    @Override
    public List<SoMasterDto> findByWaveId(Integer id, Integer wareId) {
        return soMasterMapper.findByWaveId(id, wareId);
    }

    @Override
    public SoMasterDto getSeedingByItemCode(String itemCode, Integer wareId, Integer waveMasterId) {
        List<SoMasterDto> list = soMasterMapper.getSeedingByItemCode(itemCode, wareId, waveMasterId);
        if (list.size() > 0) {

            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public SoMasterDto findBillDetailByOrderNo(String orderNo, Integer wareId) {
        return soMasterMapper.findBillDetailByOrderNo(orderNo, wareId);
    }

    @Override
    public void deleteByBillNo(String billNo, Integer version) {
        int rowCount = soMasterMapper.deleteByBillNo(billNo, version);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.SO_VERSION, "delete soMaster byBillNo更新了0条数据：" + billNo + "," + version);
        }
    }

    @Override
    public void createSoMaster(SoData soData, CurrentUser currentUser) {
        /**
         * 新建SO单据
         */
        SoMaster soMaster = soData.getSoMaster();
        String tableName = "so_master";
        String nextId = validatorService.getNextIdString(tableName);
        String billNo = "SO-" + nextId;

        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        if (soData.getBillSource() == 1 || soData.getBillSource() == 3 || soData.getBillSource() == 4) {

            // 状态 2-等待审核(初始化) 1-审核通过 0-审核中 -1-审核退回
            Integer state = 1;

            /**
             * 草稿
             */
            if (soData.getInsertType() == -2) {
                state = 1;
                BillRecord billRecord1 = new BillRecord();
                billRecord1.setBillNo(billNo);
                billRecord1.setRecordType(23);// SO草稿
                billRecord1.setCreateTime(createTime);
                billRecord1.setCreateUserId(createUserId);
                billRecord1.setCreateUserName(createUserName);
                billRecordService.createBillRecord(billRecord1, currentUser);
            }
            /**
             * 提交
             */
            else if (soData.getInsertType() == 0) {
                String billNo1 = soData.getSoMaster().getBillNo();

                // 通过草稿提交，则需要先把草稿删掉
                if (billNo1 != null) {
                    soMasterService.deleteByBillNo(billNo1, soMasterService.findBy("billNo", billNo1).getVersion());

                }
                /**
                 * 添加审核流
                 */
                // -1不通过 0-流转中 1-已通过
                state = caseMasterService.createCase(billNo, 3, currentUser.getUserId(), soMaster.getWareId(), soMaster.getOrganizationId());
            }

            soMaster.setCreateUserId(createUserId);
            soMaster.setCreateUserName(createUserName);
            soMaster.setCreateTime(createTime);
            soMaster.setBillNo(billNo);
            soMaster.setAllotBillNo(nextId);
            soMaster.setCaseState(state);
            soMaster.setState(state);
            soMaster.setInsertType(soData.getInsertType());
            if (soMaster.getBillSource() == null) {
                soMaster.setBillSource(1); /// 1-手动新建
            }

            soMaster.setSoStructure(soData.getSoDetails().size());
            /*
             计算运单重量
             */
            Double weight = 0.0;
            List<SoDetail> soDetails = soData.getSoDetails();
            for (int i = 0; i < soDetails.size(); i++) {
                if (ObjectUtil.isNotEmpty(soDetails.get(i).getWeight())) {
                    weight += soDetails.get(i).getWeight() * soDetails.get(i).getOrderQuantity();
                }
            }
            soMaster.setWeight(weight);
            soMasterService.save(soMaster);

            for (int i = 0; i < soDetails.size(); i++) {
                String detailNo = billNo + "-" + (i + 1);
                SoDetail soDetail = soDetails.get(i);
                soDetail.setBillNo(billNo);
                soDetail.setSoDetailId(null);
                soDetail.setSoMasterId(soMaster.getSoMasterId());
                soDetail.setDetailNo(detailNo);
                soDetail.setDetailIndex(i + 1);
                soDetail.setState(0);
                soDetailService.save(soDetail);
            }

            // 添加SO单跟踪信息
            BillRecord billRecord = new BillRecord();
            billRecord.setBillNo(billNo);
            billRecord.setRecordType(22);// SO新增
            billRecord.setCreateTime(createTime);
            billRecord.setCreateUserId(createUserId);
            billRecord.setCreateUserName(createUserName);
            billRecordService.createBillRecord(billRecord, currentUser);

        }

    }

    @Override
    public void createFile(String billNo, CurrentUser currentUser) {
        SoMaster soMaster = soMasterService.findBy("billNo", billNo);
        // 状态为 ASN完成状态
        Integer fileUserId = currentUser.getUserId();
        String fileUserName = currentUser.getUserName();
        String fileTime = DateUtils.getNowDateTimeString();
        if (soMaster.getState() != 10) {
            throw new ServiceException(CommonCode.SO_FILE_ERROR);
        }
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(26);// SO归档
        billRecord.setCreateTime(fileTime);
        billRecord.setCreateUserId(fileUserId);
        billRecord.setCreateUserName(fileUserName);
        billRecordService.createBillRecord(billRecord, currentUser);

        SoMasterFile soMasterFile = new SoMasterFile();
        BeanUtils.copyProperties(soMaster, soMasterFile);

        soMasterFile.setFileUserId(fileUserId);
        soMasterFile.setFileUserName(fileUserName);
        soMasterFile.setFileTime(fileTime);
        soMasterFileService.save(soMasterFile);

        List<SoDetail> soDetails = soDetailService.findByBillNo(billNo);

        for (int i = 0; i < soDetails.size(); i++) {

            SoDetail soDetail = soDetails.get(i);
            SoDetailFile soDetailFile = new SoDetailFile();
            BeanUtils.copyProperties(soDetail, soDetailFile);

            soDetailFileService.save(soDetailFile);

        }
        soMasterService.deleteByBillNo(billNo, soMaster.getVersion());
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 26);// SO归档
        billRecord1.setModifyUserId(fileUserId);
        billRecord1.setModifyUserName(fileUserName);
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecordService.update(billRecord1);
    }

    @Override
    public List<SoMaster> findSoGroupInfo(SoMasterCriteria criteria) {
        return soMasterMapper.findSoGroupInfo(criteria);
    }

    @Override
    public List<SoMaster> findSoByWave(SoMasterCriteria criteria) {
        return soMasterMapper.findSoByWave(criteria);
    }

    @Override
    public Integer findCountByWaveIdAndState(Integer waveMasterId, Integer state) {
        return soMasterMapper.findCountByWaveIdAndState(waveMasterId, state);
    }

    @Override
    public List<SoMaster> findCanTongBu() {
        return soMasterMapper.findCanTongBu();
    }

    @Override
//    public void setCarriersWithRouteTactic(Integer organizationId, Integer wareId) {
    public void setCarriersWithRouteTactic() {
        SoMasterCriteria soMasterCriteria = new SoMasterCriteria();
//        soMasterCriteria.setOrganizationId(organizationId);
//        soMasterCriteria.setWareId(wareId);
        List<SoMasterDto> soMasters = soMasterMapper.findThatNoCarrier(soMasterCriteria);
        RouteTacticCriteria criteria = new RouteTacticCriteria();
//        criteria.setOrganizationId(organizationId);
//        criteria.setWareId(wareId);
        criteria.setState(1);
        List<RouteTactic> routeTactics = routeTacticService.findList(criteria);
        if (routeTactics.size() == 0) {
            throw new ServiceException(CommonCode.NO_ROUTE_TACTIC);
        }

        for (SoMasterDto soMaster1 : soMasters) {
            Double weight = soMaster1.getWeight();
            Double size = soMaster1.getSize();
            tacticLoop: for (RouteTactic routeTactic : routeTactics) {
                Double maxWeight = routeTactic.getMaxWeight();
                Double minWeight = routeTactic.getMinWeight();
                Double maxSize = routeTactic.getMaxSize();
                Double minSize = routeTactic.getMinSize();
                if (weight > minWeight && weight < maxWeight && size > minSize && size < maxSize) {
                    soMaster1.setCarrierId(routeTactic.getCarrierId());
                    soMaster1.setCarrierName(routeTactic.getCarrierName());
                    soMasterService.update(soMaster1);
                    break tacticLoop;
                }
            }
        }
    }

    @Override
    public List<PdaSearchSoData> pdaSearchSo(String trackCode) {
        return soMasterMapper.pdaSearchSo(trackCode);
    }

    @Override
    public SoMasterYdyp getYdyp(String itemCode, Integer wareId, Integer waveId) {
        return soMasterMapper.getYdyp(itemCode, wareId, waveId);
    }

    @Override
    public SoMasterYdyp getYdyp2(String itemCode, Integer wareId, Integer waveId) {
        return soMasterMapper.getYdyp2(itemCode, wareId, waveId);
    }

    @Override
    public void createSO(CreateSo createSo, CurrentUser currentUser) {
        SoMaster soMaster = soMaster = soMasterService.findBy("billNo", createSo.getBillNo());
        if (soMaster != null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "单号重复");
        }
        soMaster = new SoMaster();
        if (createSo.getWareCode() == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库编码不得为空");
        }
        if (createSo.getOrgCode() == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货主编码不得为空");
        }

        String billNo = createSo.getBillNo();
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();
        String wareCode = createSo.getWareCode();
        String orgCode = createSo.getOrgCode();
        WareInfo wareInfo = wareInfoService.findBy("wareCode", wareCode);
        Organization organization = organizationService.findBy("organizationCode", orgCode);

        if (createSo.getOrderTime() == null || createSo.getOrderTime().equals("")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "下单时间不得为空");
        }

        if (wareInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库编码请检查");
        }
        if (organization == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "货主编码请检查");
        }
        Integer state = 2;
        // state = caseMasterService.createCase(billNo, 2, currentUser.getUserId(), wareInfo.getWareId(),organization.getOrganizationId());

        soMaster.setState(state);
        soMaster.setBillNo(billNo);
        soMaster.setCreateTime(createTime);
        soMaster.setCreateUserId(createUserId);
        soMaster.setCreateUserName(createUserName);
        soMaster.setWareId(wareInfo.getWareId());
        soMaster.setWareName(wareInfo.getWareName());
        soMaster.setWareId(wareInfo.getWareId());
        soMaster.setWareCode(wareInfo.getWareCode());
        soMaster.setOrganizationCode(organization.getOrganizationCode());
        soMaster.setOrganizationName(organization.getOrganizationName());
        soMaster.setOrganizationId(organization.getOrganizationId());
        soMaster.setAllotBillNo(createSo.getBillNo());
        soMaster.setShipCode(createSo.getShipCode());
        soMaster.setShipBillCode(createSo.getShipBillCode());
        soMaster.setExpressBillUrl(createSo.getExpressBillUrl());
        soMaster.setInsertType(4);
        soMaster.setOrderTime(createSo.getOrderTime());

        CreateSoShippingAddress shippingAddress = createSo.getShippingAddress();
        soMaster.setShipLinkMan(shippingAddress.getLinkMan());
        soMaster.setShipLinkPhone(shippingAddress.getLinkPhone());
        soMaster.setShipCity(shippingAddress.getCity());
        soMaster.setShipCountry(shippingAddress.getCountry());
        soMaster.setShipProvince(shippingAddress.getProvince());
        soMaster.setShipArea(shippingAddress.getArea());
        soMaster.setShipDetailAddress(shippingAddress.getDetailAddress());
        soMaster.setShipPostCode(shippingAddress.getPostCode());

        CreateSoDeliveryAddress deliveryAddress = createSo.getDeliveryAddress();
        soMaster.setLinkMan(deliveryAddress.getLinkMan());
        soMaster.setLinkPhone(deliveryAddress.getLinkPhone());
        soMaster.setProvince(deliveryAddress.getProvince());
        soMaster.setCity(deliveryAddress.getCity());
        soMaster.setCountry(deliveryAddress.getCountry());
        soMaster.setArea(deliveryAddress.getArea());
        soMaster.setDetailAddress(deliveryAddress.getDetailAddress());
        soMaster.setCountry(deliveryAddress.getCountry());
        soMaster.setLinkPostCode(deliveryAddress.getPostCode());

        soMaster.setLastMileDestination(createSo.getLastMileDestination());
        soMaster.setLastMileLineCode(createSo.getLastMileLineCode());
        soMaster.setLastMileSortingCode(createSo.getLastMileSortingCode());
        soMaster.setSoUdfHs1(createSo.getExt1());
        soMaster.setSoUdfHs2(createSo.getExt2());
        soMaster.setSoUdfHs3(createSo.getExt3());
        soMaster.setSoStructure(createSo.getSoSkuDetails().size());
        soMaster.setTotalPrice(createSo.getCodValue());
        soMaster.setTradeNo(createSo.getTradeNo());
        soMaster.setShop(createSo.getShop());
        soMaster.setCnDeclarationName(createSo.getCnDeclarationName());
        soMaster.setDeclarationName(createSo.getDeclarationName());
        soMasterService.save(soMaster);

        List<CreateSoSkuDetail> asnskuDetails = createSo.getSoSkuDetails();
        for (int i = 0; i < asnskuDetails.size(); i++) {
            CreateSoSkuDetail createSoSkuDetail = asnskuDetails.get(i);

            SoDetail soDetail = new SoDetail();
            soDetail.setBillNo(soMaster.getBillNo());
            soDetail.setSoMasterId(soMaster.getSoMasterId());
            soDetail.setDetailNo(soMaster.getBillNo() + (i + 1));
            soDetail.setDetailIndex(i + 1);
            soDetail.setItemCode(createSoSkuDetail.getSkuCode());

            if (createSoSkuDetail.getSkuCode() == null || createSoSkuDetail.getSkuCode().trim().equals("")) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "SKU无效");
            }
            ItemInfo itemInfo = itemInfoService.findBy("itemCode", createSoSkuDetail.getSkuCode());
            if (itemInfo == null) {
                itemInfo = new ItemInfo();
                itemInfo.setImgUrl(createSoSkuDetail.getImgUrl());
                itemInfo.setItemCode(createSoSkuDetail.getSkuCode());
                itemInfo.setItemName(createSoSkuDetail.getSkuName());
                itemInfo.setPackCode("sys_pack");
                itemInfoService.save(itemInfo);
            }
            soDetail.setItemName(itemInfo.getItemName());
            soDetail.setSpec(itemInfo.getSpec());
            soDetail.setModel(itemInfo.getModel());
            soDetail.setImgUrl(itemInfo.getImgUrl());
            soDetail.setPackDetailId(166);
            soDetail.setTransRatio(1.0);
            soDetail.setPackDescribe("each");
            soDetail.setOrderQuantity(createSoSkuDetail.getQuantity());
            soDetailService.save(soDetail);
        }
    }

    @Override
    public Integer getMaxPriority() {
        return soMasterMapper.getMaxPriority();
    }

    @Override
    public List<SoMasterDto> getUnCompleteLog() {
        return soMasterMapper.getUnCompleteLog();
    }

    @Override
    public SoMaster getByBoxOrCell(String code, Integer wareId) {
        return soMasterMapper.getByBoxOrCell(code, wareId);
    }

    @Override
    public SoMasterDto getDtoByBoxOrCell(String code, Integer wareId) {
        return soMasterMapper.getDtoByBoxOrCell(code, wareId);
    }

    @Override
    public int update(SoMaster soMaster) {
        int rowCount = soMasterMapper.update(soMaster);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.SO_VERSION, "update soMaster更新了0条数据：" + soMaster.getSoMasterId() + "," + soMaster.getVersion());
        }
        return rowCount;
    }

    /**
     * 补单
     **/
    public void replayOrder(List<String> orderIDs) {
        int i = 0;
        String getID;
        String order_ids = "";
        String where_Order_ids = "";
        List<String> where_Order_idsList = new java.util.ArrayList<String>();
        for (String orderID : orderIDs) {
            i++;
            // SoMaster soMaster = soMasterService.findFororderIDLengthBy("allotBillNo",orderID);
            // 经过产品经理设计,重新调整为 ：
            // where_Order_ids += "'"+orderID+"',";
            /*
             * where_Order_idsList.add(orderID); if(i > 50) { i=0; //String orderIDs, String code1, String code2, String orderIDLen order_ids += this.processWhere_Order_ids(where_Order_idsList);
             * where_Order_ids = ""; }
             */
            getID = this.processWhere_Order_id(orderID);
            if (!getID.equals("")) {
                order_ids += getID + ",";
            }
        }

        // if(i > 0) {
        // order_ids += this.processWhere_Order_ids(where_Order_idsList);
        // }

        if (order_ids != null && !order_ids.equals("")) {
            order_ids = order_ids.substring(0, (order_ids.length() - 1));
            this.sendReplayOrderRequest(order_ids);
        }
    }

    private String processWhere_Order_id(String where_Order_id) {

        List<SoMaster> soMaster = soMasterService.getReplayOrderIDByID(where_Order_id);
        for (SoMaster SoMas : soMaster) {
            if (SoMas.getState() == 1) {
                where_Order_id = "";
            }
        }

        return where_Order_id;
    }

    private String processWhere_Order_ids(List<String> where_Order_idsList) {
        String order_ids = "";

        List<SoMaster> soMaster = soMasterService.getReplayOrderIDByIDS(where_Order_idsList);
        for (SoMaster SoMas : soMaster) {
            if (SoMas.getState() == 1) {
                // where_Order_idsList = where_Order_idsList.replace("'"+SoMas.getBillNo()+"',", "");
                where_Order_idsList.remove(SoMas.getBillNo());
            }
        }
        for (String sendOrderID : where_Order_idsList) {
            order_ids += sendOrderID.replace("'", "") + ",";
        }
        return order_ids;
    }

    private void sendReplayOrderRequest(String order_ids) {

        Date curdate = DateUtils.getNowDateTime();
        String fristTime = DateUtils.getFristTimeHalf(curdate);
        String starTime = fristTime.split(",")[0];
        String endTime = fristTime.split(",")[1];
        try {
            String starTimeLong = DateUtils.dateToStampByS(starTime);
            String endTimeLong = DateUtils.dateToStampByS(endTime);

            URIBuilder urlB;
            HttpGet httpGet;
            urlB = new URIBuilder(passQuanConfig.getHost() + passQuanPathConfig.getReplay());

            // urlB.addParameter("order_ids[]", order_ids);
            for (String orderID : order_ids.split(",")) {
                urlB.addParameter("order_ids[]", orderID);
            }

            urlB.addParameter("url", wmsHost + wmsWebhookSoOrderPath);
            log.info("轮询补单寻找订单号：{2} starTime: " + starTime + "   starTimeLong： " + starTimeLong + " endTime: " + endTime + " endTimeLong: " + endTimeLong);

            httpGet = new HttpGet(urlB.build());

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-Type", "application/json");
            for (Map.Entry<String, String> entry : passQuanConfig.getHeads().entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (key != null && val != null) {
                    httpGet.setHeader(key, val);
                }
            }

            String res = HttpClientUtil.doGet(passQuanConfig.getHost() + passQuanPathConfig.getReplay(), httpGet);

            res = res + "   tips";

        } catch (URISyntaxException e) {
            log.error("轮询补单 -- URISyntaxException 错误：{5} ", e.getMessage());
        } catch (ParseException e) {
            log.error("开始轮询补单 -- ParseException 错误：{6} ", e.getMessage());
        }
    }

    public SoMaster findFororderIDLengthBy(String string, String orderID) {
        // return soMasterMapper.getOrderByIDLength(string, orderID);
        return null;
    }

    @Override
    public List<SoMaster> getReplayOrderIDByID(String orderID) {
        return soMasterMapper.getReplayOrderIDByID(orderID);
    }

    @Override
    public List<SoMaster> getReplayOrderIDByIDS(List<String> orderIDs) {
        // if(orderIDs.endsWith(","))
        // orderIDs = orderIDs.substring(0,(orderIDs.length() - 1));

        SoMasterCriteria smc = new SoMasterCriteria();
        smc.setBillNos(orderIDs);

        return soMasterMapper.getReplayOrderIDByIDS(smc);
    }

    @Override
    public SoMaster findByIdAndWareID(String orderID, String wareID, String orderIDLen) {
        return soMasterMapper.findByIdAndWareID(orderID, wareID, orderIDLen);
    }

    @Override
    public SoMaster findByBillNo(String billNo) {
        return soMasterMapper.findByBillNo(billNo);
    }

    /**
     * 发运-单个SKU
     * @param criteria
     * @param currentUser
     * @return
     */
    @Override
    @Transactional
    public Boolean skuSendOut(SendOutCriteria criteria, CurrentUser currentUser) {
        SoMaster soMaster1 = this.findBy("billNo", criteria.getSoBillNo());
        // 如果不是该仓库订单，不允许发运
//        if (soMaster1==null || (soMaster1!=null && Np.i(soMaster1.getWareId()).notEq(criteria.getWareId()))) {
//            throw new ServiceException(CommonCode.SERVICE_ERROR, "非该仓库发运单！");
//        }
        // 如果没有在工作台被复核 则不允许出库
//        if (soMaster1!=null && soMaster1.getState() < 7) {
//            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单未复核，不可发运！");
//        }
        SoDetail soDetail = soDetailService.findByBillNoAndItemCode(criteria.getSoBillNo(),criteria.getItemCode());
        if(soDetail==null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "暂无该SKU订单记录！");
        }
//        if(soDetail!=null && soDetail.getState()<5){
//            throw new ServiceException(CommonCode.SERVICE_ERROR, "该SKU无法发运！");
//        }
        SendDetail sendDetail = sendDetailService.findByBillNoAndSku(criteria.getSoBillNo(), criteria.getItemCode());
        //判断该出库单中该SKU是否已经发运
        if(Np.i(soDetail.getState()).eq(6) || (sendDetail!=null && Np.i(sendDetail.getState()).eq(2))){
            throw new ServiceException(CommonCode.SKU_SEND_END, "此SKU已全部完成发运");
        }

        //新增发运数量加上历史发运数量不能超过下单数
        if(criteria.getSendQuantity()!=null && criteria.getSendQuantity() + soDetail.getDeliveryQuantity() > soDetail.getOrderQuantity()){
            throw new ServiceException(CommonCode.SKU_SEND_OVER, "发运产品数量不能超过下单数量！");
        }

        //发运数量大于等于BATCH_SCAN_LIMIT才可以传数量
        if(soDetail!=null && soDetail.getOrderQuantity() < WareTaskConstant.BATCH_SCAN_LIMIT && criteria.getSendQuantity() > 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该SKU不支持批量扫码");
        }

        if(sendDetail!=null && sendDetail.getSendQuantity()>=10 && criteria.getSendSumQuantity()!=null && criteria.getSendSumQuantity()<=sendDetail.getSendQuantity()){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此SKU输入产品数量需大于已扫码数量");
        }
        // 如果没有发运记录，则创建新的记录
        if(sendDetail==null){
            sendDetail = new SendDetail();
            sendDetail.setSendQuantity(0.0);
            sendDetail.setOrderQuantity(soDetail.getOrderQuantity());
            sendDetail.setItemCode(criteria.getItemCode());
            sendDetail.setItemName(soDetail.getItemName());
            sendDetail.setSoBillNo(criteria.getSoBillNo());
            sendDetailService.save(sendDetail);
        }
        sendDetail.setSendTime(new Date());
        sendDetail.setSendUserId(currentUser.getUserId());
        sendDetail.setSendUserName(currentUser.getUserName());
        Double qantity =0.0;
        if(criteria.getSendSumQuantity()!=null){
            qantity = criteria.getSendSumQuantity();
        }else{
            qantity = sendDetail.getSendQuantity()+ criteria.getSendQuantity();
        }
        sendDetail.setSendQuantity(qantity);
        if(qantity>=sendDetail.getOrderQuantity()){
            sendDetail.setState(2);
            soDetail.setState(6); // 完全发运
        }else{
            sendDetail.setState(1);
            soDetail.setState(7); // 部分发运
        }
        //更新发运详情
        sendDetailService.update(sendDetail);
        //更新详情状态
        soDetail.setDeliveryQuantity(qantity);
        soDetailService.update(soDetail);
        //主单状态更新
        Boolean isSendAll = true;
        List<SoDetail> list = soDetailService.findByBillNo(criteria.getSoBillNo());
        for(SoDetail detail:list){
            //只要有详情的状态不是全部发运，那这个主单不是全部发运
            if(detail.getState()!=6 && detail.getState()!=-1){
                isSendAll = false;
                break;
            }
        }
        //全部详情单均为全部发运 ，主单更新为全部发运，否咋为部分发运
        if(isSendAll) {
//            soMaster1.setState(8);
            soMaster1.setSendState(SendState.ALL);
            soMaster1.setOutTime(DateUtils.getNowDateTimeString());
            soMaster1.setOutUserId(currentUser.getUserId());
            soMaster1.setOutUserName(currentUser.getUserName());
            this.update(soMaster1);
            this.updateState(soMaster1);
            //主单为全部发运后再更新拣货任务
            List<PickTask> pickTasks = pickTaskService.findBySoMasterId(soMaster1.getSoMasterId());
            for (PickTask pickTask : pickTasks) {
                if (pickTask.getIsParent() == 0) {
                    Inventory inventory = inventoryService.findById(pickTask.getInventoryId());
                    if (inventory != null) {
                        inventory.setQuantity(inventory.getQuantity() - pickTask.getPickQuantity());
                        inventoryService.update(inventory);
                        pickTask.setState(5);
                        pickTaskService.update(pickTask);
                    }

                }

            }

        }else{
//            soMaster1.setState(12); //部分发运
            soMaster1.setSendState(SendState.PARTIAL);
            this.update(soMaster1);
        }

        return true;
    }




    /**
     * 将SoDetail状态尝试更新为已完成
     * 
     * 同步更新SO状态
     * 
     * 更新出库单详情和主单状态
     * 
     * 用于豆芽从PQ确认过此单已完成，尝试将本单的未分配的SKU直接刷成完成
     * 
     * @param billNo
     * @param sku
     * @param user
     * @return
     */
    @Override
    @Transactional
    public void updateMasterAndDetailState(String billNo, String sku, CurrentUser user) {
        // 查询状态是否是未分配或缺货
        SoDetailCriteria criteria = new SoDetailCriteria();
        criteria.setBillNo(billNo);
        criteria.setItemCode(sku);
        List<SoDetailDto> soDetailDtos = soDetailService.findList(criteria);
        if (soDetailDtos.size() < 1) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "没有可修改的记录！");
        }
        String stateErrorResult = "";
        boolean hasUpdated = false;
        for (SoDetailDto detailDto : soDetailDtos) {
            if (Np.i(detailDto).x(x -> x.getState()).notIn(0, 1)) {
                stateErrorResult += detailDto.getDetailNo() + "的" + sku + "状态码为：" + detailDto.getState() + ";";
            } else {
                hasUpdated = true;
                // 将详情状态改为已完成,分配数，拣货书，复核数，发货数均改为订单数
                SoDetail soDetail = new SoDetail();
                BeanUtils.copyProperties(detailDto, soDetail);
                soDetail.setState(6); // 已发运
                Double orderQuantity = soDetail.getOrderQuantity();
                soDetail.setCheckQuantity(orderQuantity);
                soDetail.setPickQuantity(orderQuantity);
                soDetail.setAllottedQuantity(orderQuantity);
                soDetail.setDeliveryQuantity(orderQuantity);
                soDetailService.update(soDetail);
            }
        }
        if (!hasUpdated) {
            stateErrorResult = "sku 状态不符合要求：" + stateErrorResult;
            throw new ServiceException(CommonCode.SERVICE_ERROR, stateErrorResult);
        }
        // 根据billNo 查询所有详情状态
        SoDetailCriteria criteria1 = new SoDetailCriteria();
        criteria1.setBillNo(billNo);
        List<SoDetailDto> soDetailDtos2 = soDetailService.findList(criteria1);
        SoMaster soMaster = this.findByBillNo(billNo);
        // SO : 0-初始化 1-审核通过 3-部分分配 4-分配完成 5-部分拣货 6-拣货完成 7-复核完成 8-完全发运 11-已合并
        // Detail : -1：取消 0：新建 1：部分分配 2：全部分配 3：部分拣货 4：全部拣货 5：复核完成 6：完全发运
        // 越靠后优先级越高，会冲掉前面的结论
        int soState = 8;
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 5) {
                soState = 7;
            }
        }
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 4) {
                soState = 6;
            }
        }
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 2) {
                soState = 4;
            }
        }
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 3) {
                soState = 5;
            }
        }
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 0) {
                soState = 1;
            }
        }
        for (SoDetailDto detail2 : soDetailDtos2) {
            if (detail2.getState() == 1) {
                soState = 3;
            }
        }
        // 只有当所有订单状态变为发运，主单才是发运，其他情况，主单状态不变
        if (soState != soMaster.getState()) {
            soMaster.setState(soState);
            if (soState == 8) {
                soMaster.setOutTime(DateUtils.getNowDateTimeString());
                soMaster.setOutUserId(user.getUserId());
                soMaster.setOutUserName(user.getUserName());
            }
            soMasterService.update(soMaster);
        }
    }
}
