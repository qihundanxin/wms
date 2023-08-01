package com.deer.wms.ASN.manage.service.impl;

import java.util.List;

import com.deer.wms.inventory.model.Inventory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.dao.AcceptRecordMapper;
import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnDetailCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetailDto;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.buy.RequestBuy;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.ASN.manage.model.in.AcceptInsert;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.ASN.manage.model.in.AcceptWebHook;
import com.deer.wms.ASN.manage.service.AcceptExService;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.ASN.manage.service.RequestBuyService;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackMasterService;
import com.deer.wms.base.system.service.WareShopService;
import com.deer.wms.inventory.constant.InventoryConstant;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.InventoryCriteria;
import com.deer.wms.inventory.model.Inventory.InventoryDto;
import com.deer.wms.inventory.model.Inventory.InventoryTransactRunCriteria;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.InventoryTransactService;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.inventory.service.SerialNoService;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.system.manage.service.ValidatorService;
import com.deer.wms.ureport.service.ReportOperateService;
import com.deer.wms.webhook.WebHookSender;
import com.deer.wms.webhook.model.WebHookAbility;

import cn.hutool.core.util.StrUtil;

/**
 * Created by 郭靖勋 on 2019/12/18.
 */
@Service
@Transactional //事务回滚
public class AcceptRecordServiceImpl extends AbstractService<AcceptRecord, Integer> implements AcceptRecordService {
    @Autowired
    private AcceptRecordMapper acceptRecordMapper;

    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    private InventoryTransactService inventoryTransactService;

    @Autowired
    private AcceptRecordService acceptRecordService;

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    @Autowired
    private RequestBuyService requestBuyService;

    @Autowired
    private RequestBuyDetailService requestBuyDetailService;

    @Autowired
    private SerialNoService serialNoService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private ReportOperateService reportOperateService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PackMasterService packMasterService;

    @Autowired
    private WareShopService wareShopService;

    @Autowired
    private WebHookSender webHookSender;

    public String getNextBoxCode() {
        return validatorService.getNextIdString("box_info");
    }

    @Override
    public List<AcceptRecord> findList(AcceptRecordCriteria criteria) {
        return acceptRecordMapper.findList(criteria);
    }

//    `bill_type  '0-默认验收 1-采购入库 2-新建入库单 3-盲收 4-冻结',
    @Override
    @Synchronized(lockName = LockName.ACCEPT, value = "#acceptInsert.asnDetailNo")
    public Integer accept(AcceptInsert acceptInsert) {

        //垃圾货位不可进行验收、盲收操作
        Integer cellUseType = cellInfoService.getCellType(acceptInsert.getToCellCode(),acceptInsert.getWareId());
        if(Np.i(cellUseType).eq(-1)){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "验收货位无效");
        }
        ItemInfo itemInfo = itemInfoService.findByItemCode(acceptInsert.getItemCode());
        AcceptWebHook acceptWebHookData = new AcceptWebHook();// WebHook推出数据
        BeanUtils.copyBeanProp(acceptWebHookData, acceptInsert);
        acceptWebHookData.setSku(acceptInsert.getItemCode());
        AsnDetail asnDetail = null;
        if (acceptInsert.getBillType() == 2 || acceptInsert.getBillType() == 6) {// ASN验收
            if (StrUtil.isNotBlank(acceptInsert.getAsnDetailNo())) {
                asnDetail = asnDetailService.findBy("detailNo", acceptInsert.getAsnDetailNo());
            } else {
                throw new ServiceException(CommonCode.PARAMETER_ERROR, "缺少asnDetailNo");
            }
        }
        /*
         * 盲收
         */
        Integer acceptRecordId = null;
        if (acceptInsert.getBillType() == 3) {
            ItemBatch itemBatch = acceptInsert;
            itemBatch.setOrderType(2);
            itemBatch = itemBatchService.createItemBatch(itemBatch);

            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                    acceptInsert.getToCellCode(),
                    acceptInsert.getWareId());
            if(cellInfo==null){
                throw new ServiceException(CommonCode.SERVICE_ERROR,"货位号无效");
            }
            Integer wareId = acceptInsert.getWareId();
            String cellCode = cellInfo.getCellCode();
            String boxCode = acceptInsert.getToBoxCode();
            Integer packDetailId = acceptInsert.getToPackDetailId();
            Double transRatio = acceptInsert.getToTransRatio();
            Double quantity = acceptInsert.getToQuantity();
            String itemCode = acceptInsert.getItemCode();
            Integer batchId = itemBatch.getBatchId();
            String batchName = itemBatch.getBatchName();
            Inventory inventory = inventoryService.getSameInventory(wareId, cellCode, boxCode, packDetailId, itemCode, batchId, transRatio);
            if (inventory == null  ) {
                inventory = new Inventory();
                inventory.setOrganizationId(acceptInsert.getOrganizationId());
                inventory.setOrganizationName(acceptInsert.getOrganizationName());
                inventory.setWareId(wareId);
                inventory.setWareName(acceptInsert.getWareName());
                inventory.setCellCode(cellCode);
                inventory.setCellUseType(cellInfo.getCellUseType());
                inventory.setBoxCode(boxCode);
                inventory.setPackDetailId(packDetailId);
                inventory.setPackDescribe(acceptInsert.getToPackDescribe());
                inventory.setItemCode(itemCode);
                inventory.setItemName(itemInfo.getItemName());
                inventory.setImgUrl(acceptInsert.getImgUrl());
                inventory.setBatchId(batchId);
                inventory.setBatchName(batchName);
                inventory.setQuantity(quantity);
                inventory.setTransRatio(acceptInsert.getToTransRatio());
                inventory.setWareEntryTime(DateUtils.getNowDateTime());
                inventoryService.save(inventory);
            } else {
                inventory.setQuantity(inventory.getQuantity() + quantity);
                inventoryService.update(inventory);
            }
            acceptWebHookData.setInventoryId(inventory.getInventoryId());// WebHook
            inventory.setQuantity(acceptInsert.getToQuantity());
            InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
            runCriteria.setToInventory(inventory);
            runCriteria.setTransactType(InventoryConstant.TRANSACT_TYPE_BLIND);
            runCriteria.setCreateUserId(acceptInsert.getAcceptUserId());
            runCriteria.setCreateUserName(acceptInsert.getAcceptUserName());
            inventoryTransactService.run(runCriteria);
            AcceptRecord acceptRecord = new AcceptRecord();
            BeanUtils.copyProperties(acceptInsert, acceptRecord);

            acceptRecord.setCellCode(cellCode);
            acceptRecord.setBoxCode(acceptInsert.getToBoxCode());
            acceptRecord.setQuantity(acceptInsert.getToQuantity());
            acceptRecord.setPackDetailId(acceptInsert.getToPackDetailId());
            acceptRecord.setPackDescribe(acceptInsert.getToPackDescribe());
            acceptRecord.setBatchName(batchName);
            acceptRecord.setBatchId(batchId);

            acceptRecordService.save(acceptRecord);
            acceptRecordId = acceptRecord.getAcceptRecordId();
        }
        /*
         * ASN单验收
         */
        if (acceptInsert.getBillType() == 2 || acceptInsert.getBillType() == 6) {
            ItemBatch itemBatch = acceptInsert;
            itemBatch.setOrderType(2);
            itemBatch = itemBatchService.createItemBatch(itemBatch);
            Integer batchId = itemBatch.getBatchId();
            String batchName = itemBatch.getBatchName();
           // inventoryTransact.setTransactType(InventoryConstant.TRANSACT_TYPE_ASN);

            String boxCode = acceptInsert.getToBoxCode();
            String cellCode = acceptInsert.getToCellCode();
            Integer wareId = acceptInsert.getWareId();
            String wareName = acceptInsert.getWareName();
            Integer orgId = acceptInsert.getOrganizationId();
            String orgName = acceptInsert.getOrganizationName();
            CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
            if(cellInfo==null){
                throw new ServiceException(CommonCode.SERVICE_ERROR,"货位号无效");
            }else{
                cellCode = cellInfo.getCellCode();
            }
//            CellInfo cellInfo =null;
//            if (cellCode == null || cellCode.trim().equals("")) {
//                cellInfo = cellInfoService.getSysAcceptCellInfo(  wareId);
//                cellCode =cellInfo.getCellCode();
//            }else{
//                cellInfo = cellInfoService.findByCodeAndWareId(  cellCode,  wareId);
//                if(cellInfo==null){
//                    cellInfo = cellInfoService.getSysAcceptCellInfo(acceptInsert.getWareId());
//                    cellCode =cellInfo.getCellCode();
//                }else{
//                    cellCode = cellInfo.getCellCode();
//                }
//            }

            Integer packDetailId = acceptInsert.getToPackDetailId();
            Double transRatio = acceptInsert.getToTransRatio();
            String packDescribe = acceptInsert.getToPackDescribe();
            Double quantity = acceptInsert.getToQuantity();
            String itemCode = acceptInsert.getItemCode();
            Double acceptQuantity = acceptInsert.getToQuantity();
            String now  = DateUtils.getNowDateTimeString();

            Inventory inventory = inventoryService.getSameInventory(wareId, cellCode, boxCode, packDetailId, itemCode, batchId, transRatio);
            if (inventory == null) {
                inventory = new Inventory();
                inventory.setOrganizationId(orgId);
                inventory.setOrganizationName(orgName);
                inventory.setWareId(wareId);
                inventory.setWareName(wareName);
                inventory.setCellCode(cellCode);
                inventory.setCellUseType(cellInfo.getCellUseType());
                inventory.setBoxCode(boxCode);
                inventory.setPackDetailId(packDetailId);
                inventory.setPackDescribe(packDescribe);
                inventory.setTransRatio(transRatio);
                inventory.setItemCode(itemCode);
                inventory.setItemName(itemInfo.getItemName());
                inventory.setSpec(acceptInsert.getSpec());
                inventory.setModel(acceptInsert.getModel());
                inventory.setImgUrl(acceptInsert.getImgUrl());
                inventory.setBatchId(batchId);
                inventory.setBatchName(batchName);
                inventory.setUpc(acceptInsert.getUpc());
                inventory.setQuantity(quantity);
                inventory.setBillNo(acceptInsert.getAsnBillNo());
                inventory.setWareEntryTime(DateUtils.getNowDateTime());
                inventoryService.save(inventory);
            } else {
                inventory.setQuantity(inventory.getQuantity() + quantity);
                inventoryService.update(inventory);
            }
            acceptWebHookData.setInventoryId(inventory.getInventoryId());// WebHook
            inventory.setQuantity(acceptQuantity);
            InventoryTransactRunCriteria runCriteria = new InventoryTransactRunCriteria();
            runCriteria.setToInventory(inventory);
            runCriteria.setTransactType(InventoryConstant.TRANSACT_TYPE_ASN);
            runCriteria.setDetailNo(acceptInsert.getAsnBillNo());
            runCriteria.setCreateUserId(acceptInsert.getAcceptUserId());
            runCriteria.setCreateUserName(acceptInsert.getAcceptUserName());
            inventoryTransactService.run(runCriteria);
            Integer inventoryId = inventory.getInventoryId();

            AcceptEx acceptEx = null;
            //异常入库
            if (acceptInsert.getBillType() == 6) {
                Integer acceptExId = acceptInsert.getAcceptExId();
                acceptEx = acceptExService.findById(acceptExId);
                acceptEx.setState(3);
                acceptExService.update(acceptEx);
                asnDetail.setExState(2);
            }
            AcceptRecord acceptRecord = new AcceptRecord();
            BeanUtils.copyProperties(acceptInsert, acceptRecord);
            acceptRecord.setInventoryId(inventoryId);
            acceptRecord.setAsnDetailNo(acceptInsert.getAsnDetailNo());
            acceptRecord.setBoxCode(boxCode);
            acceptRecord.setCellCode(cellCode);
            //验收数量
            acceptRecord.setQuantity(acceptQuantity);
            acceptRecord.setBatchId(batchId);
            acceptRecord.setBatchName(batchName);
            acceptRecord.setPackDetailId(packDetailId);
            acceptRecord.setPackDescribe(packDescribe);
            acceptRecord.setTransRatio(transRatio);
            acceptRecord.setAsnBillNo(asnDetail.getBillNo());
            if (acceptEx != null) {
                acceptRecord.setShipBillNo(acceptEx.getShipBillNo());
                acceptRecord.setAcceptExId(acceptEx.getAcceptExId());
            }
            acceptRecordService.save(acceptRecord);
            acceptRecordId = acceptRecord.getAcceptRecordId();
            //更新ASN单信息
            asnDetail.setAcceptQuantity(acceptQuantity + asnDetail.getAcceptQuantity());
            asnDetail.setAcceptTransRatio(transRatio);
            asnDetail.setAcceptPackDescribe(packDescribe);
            asnDetail.setAcceptPackDetailId(packDetailId);
            asnDetail.setAcceptTime(now);
            asnDetail.setVersion(Np.i(acceptInsert.getVersion()).o(asnDetail.getVersion()));

            if (asnDetail.getAcceptQuantity().equals(asnDetail.getExpectQuantity())) {
                asnDetail.setState(3);//全部收货

            } else if(asnDetail.getAcceptQuantity()<asnDetail.getExpectQuantity()){
                asnDetail.setState(2);//部分收货
            }
            else if (asnDetail.getAcceptQuantity() > asnDetail.getExpectQuantity()) {
                asnDetail.setState(5);//超额收货
            }
            asnDetail.setExState(0);
            asnDetailService.update(asnDetail);
            //跟新主表信息
            updateBill(asnDetail,acceptInsert.getToQuantity(),wareId,acceptInsert.getAcceptUserId());
        }

//        reportOperateService.report(
//                acceptInsert.getWareId(),
//                acceptInsert.getAcceptUserId(),
//                1,
//                1,
//                0,
//                acceptInsert.getAsnBillNo(),
//                acceptInsert.getItemCode(),
//                acceptInsert.getItemName(),
//                acceptInsert.getImgUrl()
//                );

        // 发送WebHook
        if (asnDetail != null && StrUtil.isNotBlank(asnDetail.getRequestDetailDetailNo())) {
            RequestBuyDetail rbDetail = requestBuyDetailService.findByDetailNo(asnDetail.getRequestDetailDetailNo());
            if (rbDetail != null) {
                acceptWebHookData.setRequestBuyBillNo(rbDetail.getBillNo());
                acceptWebHookData.setRequestBuyDetailNo(rbDetail.getDetailNo());
                acceptWebHookData.setRequestBuyDetail(rbDetail);
            }
        }
        wareShopService.findAll().forEach(wareShop -> {
            if (wareShop.getWareId() == acceptWebHookData.getWareId()) {
                acceptWebHookData.setShopId(wareShop.getShopId());
            }
        });
        // 计算此SKU目前的可用库存总数
        {
            InventoryCriteria invParam = new InventoryCriteria();
            invParam.setItemCode(acceptWebHookData.getSku());
            invParam.setWareId(acceptWebHookData.getWareId());
            invParam.setCellUseType(1);// 拣货位
            List<InventoryDto> cellInv = inventoryService.findList(invParam);
            Double availableQuantity = 0.0;
            for (InventoryDto inv : cellInv) {
                availableQuantity = availableQuantity
                        + Np.i(inv).x(x -> x.getQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getFreezeQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getAllotQuantity()).o(0.0);
            }
            acceptWebHookData.setAvailableQuantityPickCell(availableQuantity);
        }
        {
            InventoryCriteria invParam = new InventoryCriteria();
            invParam.setItemCode(acceptWebHookData.getSku());
            invParam.setWareId(acceptWebHookData.getWareId());
            invParam.setCellUseType(2);// 仓储位
            List<InventoryDto> cellInv = inventoryService.findListMoreInfo(invParam);
            Double availableQuantity = 0.0;
            for (InventoryDto inv : cellInv) {
                availableQuantity = availableQuantity
                        + Np.i(inv).x(x -> x.getQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getFreezeQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getAllotQuantity()).o(0.0);
            }
            acceptWebHookData.setAvailableQuantityStorageCell(availableQuantity);
        }
        {
            InventoryCriteria invParam = new InventoryCriteria();
            invParam.setItemCode(acceptWebHookData.getSku());
            invParam.setWareId(acceptWebHookData.getWareId());
            invParam.setCellUseType(0);// 验收位
            List<InventoryDto> cellInv = inventoryService.findListMoreInfo(invParam);
            Double availableQuantity = 0.0;
            for (InventoryDto inv : cellInv) {
                availableQuantity = availableQuantity
                        + Np.i(inv).x(x -> x.getQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getFreezeQuantity()).o(0.0)
                        - Np.i(inv).x(x -> x.getAllotQuantity()).o(0.0);
            }
            acceptWebHookData.setAvailableQuantityAcceptanceCell(availableQuantity);
        }

        webHookSender.send(WebHookAbility.CONFIRM_STORAGE_IN, acceptWebHookData, acceptWebHookData.getAsnBillNo());

        return acceptRecordId;
    }

    private void updateBill(AsnDetail asnDetail,  Double acceptQuantity,Integer wareId,Integer userId){
        //根据detail的收货状态判断 master的asn收货状态
        AsnDetailCriteria criteria = new AsnDetailCriteria();
        criteria.setBillNo(asnDetail.getBillNo());
        List<AsnDetailDto> asnDetails = asnDetailService.findList(criteria);
        Boolean asnAccept = true;
        AsnMaster asnMaster = asnMasterService.findBy("billNo", asnDetail.getBillNo());
        for (AsnDetail asnDetail1 : asnDetails) {
            if (asnDetail1.getState() != 3 && asnDetail1.getState() != 5) {
                asnAccept = false;
                break;
            }
        }
        if (asnAccept) {
            reportOperateService.report(wareId,userId,1,1,1,asnDetail.getBillNo(),asnDetail.getItemCode(),asnDetail.getItemName(),asnDetail.getImgUrl());

            Boolean isChaoE = false;
            for (AsnDetail asnDetail1 : asnDetails) {
                if ( asnDetail1.getState()== 5) {
                    isChaoE = true;
                    break;
                }
            }
            if(isChaoE){
                asnMaster.setAsnState(5);
                asnMasterService.update(asnMaster);
            }else {
                asnMaster.setAsnState(3);
                asnMasterService.update(asnMaster);
            }

        } else {
            asnMaster.setAsnState(2);
            asnMasterService.update(asnMaster);
            reportOperateService.report(wareId,userId,1,1,0,asnDetail.getBillNo(),asnDetail.getItemCode(),asnDetail.getItemName(),asnDetail.getImgUrl());

        }

        //更新采购单信息
        if (asnDetail.getRequestDetailDetailNo() != null) {
            RequestBuyDetail requestBuyDetail = requestBuyDetailService.findByDetailNo(asnDetail.getRequestDetailDetailNo());
            if(requestBuyDetail!=null){
                requestBuyDetail.setAcceptQuantity(acceptQuantity + requestBuyDetail.getAcceptQuantity());
                requestBuyDetail.setAcceptTime(DateUtils.getNowDateTimeString());
                if (requestBuyDetail.getAcceptQuantity().equals(requestBuyDetail.getExpectQuantity())) {
                    requestBuyDetail.setState(3);//全部收货

                } else {
                    requestBuyDetail.setState(2);//部分收货
                }
                if (requestBuyDetail.getAcceptQuantity() > (requestBuyDetail.getExpectQuantity())) {
                    requestBuyDetail.setState(5);// 超额收货
                }
                requestBuyDetailService.update(requestBuyDetail);

                //根据detail的收货状态判断 master的采购收货状态

                RequestBuyDetailCriteria criteria1 = new RequestBuyDetailCriteria();
                criteria1.setBillNo(requestBuyDetail.getBillNo());
                List<RequestBuyDetailDto> requestBuyDetails = requestBuyDetailService.findList(criteria1);
                //1、详情都是全部收货，主单全部收货；2、详情中部分收货，部分未收货的；或者详情均是部分收货，则主单是部分收货；3、详情中全部收货，且包含超额验收的，主单超额验收；4、
                Boolean requestAccept = true;
                Boolean isExcess = true;
                RequestBuy requestBuy = requestBuyService.findBy("billNo", requestBuyDetail.getBillNo());
                for (RequestBuyDetail requestBuyDetail1 : requestBuyDetails) {
                    if (requestBuyDetail1.getState() != 3) {
                        requestAccept = false;
                    }
                    if (requestBuyDetail1.getAcceptQuantity() < (requestBuyDetail1.getExpectQuantity())) {
                        requestBuy.setAcceptState(2);//部分收货
                        isExcess = false;
                    }
                }
                if (requestAccept) {
                    requestBuy.setAcceptState(3);
                    isExcess = false;
                }
                //只要全部详情订单的验收数量>=期望验收数量，主单为超额验收
                if(isExcess){
                    requestBuy.setAcceptState(5);
                }
                requestBuyService.update(requestBuy);
            }

        }
    }

    @Override
    public List<AcceptRecord> findDayList(AcceptRecordCriteria criteria) {
        return acceptRecordMapper.findDayList(criteria);
    }

    @Override
    public List<AcceptRecord> findCanTongbu() {
        return acceptRecordMapper.findCanTongbu();
    }

    @Override
    public List<AcceptRecord> findUnUpList() {
        return acceptRecordMapper.findUnUpList();
    }

}

