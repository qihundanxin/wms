package com.deer.wms.ASN.manage.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.PrintData;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecord;
import com.deer.wms.ASN.manage.model.in.AcceptData;
import com.deer.wms.ASN.manage.model.in.AcceptInsert;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.ASN.manage.service.AcceptAuditRecordService;
import com.deer.wms.ASN.manage.service.AcceptExService;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.SupplierManageService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.model.Inventory.ItemBatch;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.ItemBatchService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 验收api接口
 *
 * Created by 郭靖勋 on 2019/12/18.
 */
@RestController
@RequestMapping("/accept/records")
public class AcceptRecordController {

    @Autowired
    private AcceptRecordService acceptRecordService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PrintTempInfoService printTempInfoService;

    @Autowired
    private ItemBatchService itemBatchService;

    @Autowired
    private SupplierManageService supplierManageService;

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private AcceptAuditRecordService acceptAuditRecordService;

    @PostMapping("/insert")
    @Authority("accept_records_insert")
    public Result add(@RequestBody AcceptRecord acceptRecord) {
        acceptRecordService.save(acceptRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("accept_records_delete")
    public Result delete(Integer id) {
        acceptRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("accept_records_update")
    public Result update(@RequestBody AcceptRecord acceptRecord) {
        acceptRecordService.update(acceptRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("accept_records_detail")
    public Result detail(Integer id) {
        AcceptRecord acceptRecord = acceptRecordService.findById(id);
        return ResultGenerator.genSuccessResult(acceptRecord);
    }

    @GetMapping("/list")
    @Authority("accept_records_list")
    public Result list(AcceptRecordCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptRecord> list = acceptRecordService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private AsnDetailService asnDetailService;

    @Resource
    private AsnMasterService asnMasterService;

    /**
     * 验收:异常验收
     */
    @Transactional
    @PostMapping("/accept")
    @Authority("accept_records_accept")
    public Result accept(@RequestBody AcceptInsert acceptInsert, @User CurrentUser currentUser) {
        if(acceptInsert.getToQuantity()<=0){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "验收数量至少为1！");
        }
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(acceptInsert.getWareId(), "验收标签");
        if (acceptInsert.getAutoPrint() != null && acceptInsert.getAutoPrint() && printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到验收标签模板");
        }
        String cellCode = acceptInsert.getToCellCode();
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, acceptInsert.getWareId());
        if (cellInfo == null || cellInfo.getCellUseType()!=0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "验收货位无效！");
        }
        String detailNo = acceptInsert.getAsnDetailNo();
        AsnDetail asnDetail;
        if(acceptInsert.getBillType()==2 && acceptInsert.getVersion()!=null){
            asnDetail = asnDetailService.findByDetailNoAndVersion(detailNo,acceptInsert.getVersion());
        }else{
            asnDetail = asnDetailService.findBy("detailNo", detailNo);
        }
        if(asnDetail ==null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "当前入库单状态已改变，请重新提交！");
        }
        AsnMaster asnMaster = asnMasterService.findBy("billNo", asnDetail.getBillNo());
        if (asnMaster == null || !Objects.equals(asnMaster.getIsDeleted(), "n")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "入库单不存在或已经删除！");
        }
//        if(asnDetail.getExpectQuantity()<=asnDetail.getAcceptQuantity()){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"该订单已经验收！");
//        }
//        if(asnDetail.getExpectQuantity()<asnDetail.getAcceptQuantity()+acceptInsert.getToQuantity()){
//            throw  new ServiceException(CommonCode.SERVICE_ERROR,"验收数量大于采购数量！");
//        }
        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String acceptTime = DateUtils.getNowDateTimeString();

        acceptInsert.setAcceptUserId(userId);
        acceptInsert.setAcceptUserName(userName);
        acceptInsert.setAcceptTime(acceptTime);

        //添加ASN单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(acceptInsert.getAsnBillNo());
        billRecord.setRecordType(4);//ASN验收
        billRecord.setCreateTime(acceptInsert.getAcceptTime());
        billRecord.setCreateUserId(acceptInsert.getAcceptUserId());
        billRecord.setCreateUserName(acceptInsert.getAcceptUserName());

        Integer acceptExId = acceptInsert.getAcceptExId();
        if (acceptExId != null) {
            AcceptEx acceptEx = acceptExService.findById(acceptExId);
            if(acceptEx.getState()==3){
                throw new ServiceException(CommonCode.SERVICE_ERROR, "当前入库异常已经处理！");
            }
            acceptEx.setInQuantity(acceptEx.getInQuantity() + acceptInsert.getToQuantity());
            acceptExService.update(acceptEx);
        }
        acceptInsert.setOrganizationName(asnMaster.getOrganizationName());
        acceptInsert.setOrganizationId(asnMaster.getOrganizationId());
        // 验收操作
        Integer acceptRecordId = acceptRecordService.accept(acceptInsert);
        //如果爆款判断
        if (acceptInsert.getBaoKuan() != null && acceptInsert.getBaoKuan()) {
            baoKuan(acceptRecordId);
        }

        if (acceptInsert.getAutoPrint() != null && acceptInsert.getAutoPrint()) {// TODO xuesinuo 异常验收时自动打印，根本没在用
            return ResultGenerator.genSuccessResult(acceptPrintList(acceptRecordId, currentUser));
        } else {
            return ResultGenerator.genSuccessResult();
        }

    }

    /**
     * 打印验收标签
     */
    @GetMapping("/acceptPrint")
    @Authority("accept_records_acceptPrint")
    public Result acceptPrint(Integer acceptRecordId, @User CurrentUser currentUser) {

        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String acceptTime = DateUtils.getNowDateTimeString();
        return ResultGenerator.genSuccessResult(acceptPrint1(acceptRecordId, currentUser));
    }

    public List<PrintData> acceptPrintList(Integer acceptRecordId, CurrentUser currentUser) {// TODO xuesinuo 异常验收时自动打印，根本没在用
        List<PrintData> list = new ArrayList<PrintData>();
        PrintData printData1 = acceptPrint2(acceptRecordId, false, currentUser);
        list.add(printData1);
        Inventory inventory = inventoryService.findBy("baoKuan", acceptRecordId);
        PrintData printData2 = null;
        if (inventory != null) {
            printData2 = acceptPrint2(acceptRecordId, true, currentUser);
            list.add(printData2);
        }
        return list;
    }

    public String acceptPrint1(Integer acceptRecordId, CurrentUser currentUser) {
        AcceptRecord acceptRecord = acceptRecordService.findById(acceptRecordId);

        Integer batchId = acceptRecord.getBatchId();
        ItemBatch itemBatch = itemBatchService.findById(batchId);
        String supplierCode = Np.i(itemBatch).x(x->x.getSupplierCode()).o("");
        SupplierManage supplierManage = null;
        if (supplierCode != null) {
            supplierManage = supplierManageService.findBy("supplierCode", supplierCode);
        }
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(acceptRecord.getWareId(), "验收标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到验收标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();
//         if(inventory==null){
//            doc.select(".baokuan").remove();
//        } else if(!acceptRecord.getCellCode().equals(inventory.getCellCode())){
//             doc.select(".baokuan").remove();
//         }else  if(inventory.getBaoKuan()==0 &&  inventory.getAllotQuantity()<0.01 ){
//             doc.select(".baokuan").remove();
//         }
        doc.select(".baokuan").remove();

        PrintTempUtils.setTempData("WARE_CODE", acceptRecord.getWareName(), doc, "text");
        PrintTempUtils.setTempData("ORG_CODE", acceptRecord.getOrganizationId(), doc, "text");
        if (supplierManage != null) {
            PrintTempUtils.setTempData("SUPPLIER_CODE", supplierManage.getSupplierCode(), doc, "text");
            PrintTempUtils.setTempData("SUPPLIER_NAME", supplierManage.getSupplierName(), doc, "text");
        }

        PrintTempUtils.setTempData("WARE_NAME", acceptRecord.getWareName(), doc, "text");
        PrintTempUtils.setTempData("ORG_NAME", acceptRecord.getOrganizationName(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE", acceptRecord.getCellCode(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE_QR_CODE", acceptRecord.getCellCode(), doc, "qrCode");// TODO xuesinuo 入库->验收记录打印，根本没打出个有用的东西
        PrintTempUtils.setTempData("CELL_CODE_BAR_CODE", acceptRecord.getCellCode(), doc, "barCode");

        PrintTempUtils.setTempData("ITEM_NAME", acceptRecord.getItemName(), doc, "text");
        PrintTempUtils.setTempData("INVEN_ID", acceptRecord.getInventoryId(), doc, "text");
        PrintTempUtils.setTempData("INVEN_ID_BAR_CODE", acceptRecord.getInventoryId(), doc, "barCode");
        PrintTempUtils.setTempData("INVEN_ID_QR_CODE", acceptRecord.getInventoryId(), doc, "qrCode");// TODO xuesinuo 入库->验收记录打印，根本没打出个有用的东西

        PrintTempUtils.setTempData("ITEM_CODE", acceptRecord.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE_BAR_CODE", acceptRecord.getItemCode(), doc, "barCode");
        PrintTempUtils.setTempData("ITEM_CODE_QR_CODE", acceptRecord.getItemCode(), doc, "qrCode");// TODO xuesinuo 入库->验收记录打印，根本没打出个有用的东西

        PrintTempUtils.setTempData("QUANTITY", acceptRecord.getQuantity().toString(), doc, "text");

        PrintTempUtils.setTempData("ACCEPT_TIME", acceptRecord.getAcceptTime(), doc, "text");
        PrintTempUtils.setTempData("ACCEPT_USER_NAME", acceptRecord.getAcceptUserName(), doc, "text");

        PrintTempUtils.setTempData("PD_DATE",Np.i(itemBatch).x(x->x.getProductDate()).o(""), doc, "text");
        PrintTempUtils.setTempData("IN_DATE", Np.i(itemBatch).x(x->x.getInDate()).o(""), doc, "text");
        PrintTempUtils.setTempData("EX_DATE", Np.i(itemBatch).x(x->x.getExDate()).o(""), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_CODE", Np.i(itemBatch).x(x->x.getSupplierCode()).o(""), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_BATCH", Np.i(itemBatch).x(x->x.getSupplierBatch()).o(""), doc, "text");
        //printTempInfoService.setTempData("QC_STATE",Np.i(itemBatch.getQcState(),doc,"text");
        PrintTempUtils.setTempData("COXT_PRICE", Np.i(itemBatch).x(x->x.getCostPrice()).o(), doc, "text");
        PrintTempUtils.setTempData("SALE_PRICE", Np.i(itemBatch).x(x->x.getSalePrice()).o(), doc, "text");
        PrintTempUtils.setTempData("DETAIL_NO", Np.i(itemBatch).x(x->x.getDetailNo()).o(""), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_1", Np.i(itemBatch).x(x->x.getMemo1()).o(""), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_2", Np.i(itemBatch).x(x->x.getMemo2()).o(""), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_3", Np.i(itemBatch).x(x->x.getMemo3()).o(""), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_1", Np.i(itemBatch).x(x->x.getBatchAttribute1()).o(""), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_2", Np.i(itemBatch).x(x->x.getBatchAttribute2()).o(""), doc, "text");

        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        if (doc.getElementById("FULL_ITEMS_DETAIL_TABLE") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = tbody.getElementById("ITEM_LIST_DETAIL");
            Integer colspan = 0;
            for (int i = 0; i < 5; i++) {
                Element newEle = ITEM_LIST_DETAIL.clone();

                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PICTURE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_ORIGINAL_SKU", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU_PLATFORM", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU_COMBO", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_EN", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_WAREHOUSE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_GRID_CODE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PACKAGE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_WEIGHT", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PROPERTY", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PRODUCT_UNIT", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SALESPRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_TOTAL_AMOUNT_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_TOTAL_SALES_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_ITEM_ID", currentUser.getUserName(), newEle, "text");

                newEle.appendTo(tbody);
            }
            ITEM_LIST_DETAIL.remove();
        }

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return tempHtml;
    }

    public PrintData acceptPrint2(Integer acceptRecordId, Boolean isBaokuan, CurrentUser currentUser) {
        AcceptRecord acceptRecord = acceptRecordService.findById(acceptRecordId);
        Inventory inventory = null;
        if (isBaokuan) {
            inventory = inventoryService.findBy("baoKuan", acceptRecordId);
        } else {
            inventory = inventoryService.findById(acceptRecord.getInventoryId());
        }
        if (inventory == null) {
            PrintData printData = new PrintData();
            printData.setQuantity(0);
            return printData;
        }
        Integer batchId = acceptRecord.getBatchId();
        ItemBatch itemBatch = itemBatchService.findById(batchId);
        String supplierCode = itemBatch.getSupplierCode();
        SupplierManage supplierManage = null;
        if (supplierCode != null) {
            supplierManage = supplierManageService.findBy("supplierCode", supplierCode);
        }
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(acceptRecord.getWareId(), "验收标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到验收标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();
//         if(inventory==null){
//            doc.select(".baokuan").remove();
//        } else if(!acceptRecord.getCellCode().equals(inventory.getCellCode())){
//             doc.select(".baokuan").remove();
//         }else  if(inventory.getBaoKuan()==0 &&  inventory.getAllotQuantity()<0.01 ){
//             doc.select(".baokuan").remove();
//         }
        if (!isBaokuan) {
            doc.select(".baokuan").remove();
        }

        PrintTempUtils.setTempData("WARE_CODE", acceptRecord.getWareName(), doc, "text");
        PrintTempUtils.setTempData("ORG_CODE", acceptRecord.getOrganizationId(), doc, "text");
        if (supplierManage != null) {
            PrintTempUtils.setTempData("SUPPLIER_CODE", supplierManage.getSupplierCode(), doc, "text");
            PrintTempUtils.setTempData("SUPPLIER_NAME", supplierManage.getSupplierName(), doc, "text");
        }

        PrintTempUtils.setTempData("WARE_NAME", acceptRecord.getWareName(), doc, "text");
        PrintTempUtils.setTempData("ORG_NAME", acceptRecord.getOrganizationName(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE", acceptRecord.getCellCode(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE_QR_CODE", acceptRecord.getCellCode(), doc, "qrCode");// TODO xuesinuo 异常验收时自动打印，根本没在用
        PrintTempUtils.setTempData("CELL_CODE_BAR_CODE", acceptRecord.getCellCode(), doc, "barCode");

        PrintTempUtils.setTempData("ITEM_NAME", acceptRecord.getItemName(), doc, "text");
        PrintTempUtils.setTempData("INVEN_ID", inventory.getInventoryId(), doc, "text");
        PrintTempUtils.setTempData("INVEN_ID_BAR_CODE", inventory.getInventoryId(), doc, "barCode");
        PrintTempUtils.setTempData("INVEN_ID_QR_CODE", inventory.getInventoryId(), doc, "qrCode");// TODO xuesinuo 异常验收时自动打印，根本没在用

        PrintTempUtils.setTempData("ITEM_CODE", acceptRecord.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE_BAR_CODE", acceptRecord.getItemCode(), doc, "barCode");
        PrintTempUtils.setTempData("ITEM_CODE_QR_CODE", acceptRecord.getItemCode(), doc, "qrCode");// TODO xuesinuo 异常验收时自动打印，根本没在用

        PrintTempUtils.setTempData("QUANTITY", acceptRecord.getQuantity().toString(), doc, "text");

        PrintTempUtils.setTempData("ACCEPT_TIME", acceptRecord.getAcceptTime(), doc, "text");
        PrintTempUtils.setTempData("ACCEPT_USER_NAME", acceptRecord.getAcceptUserName(), doc, "text");

        PrintTempUtils.setTempData("PD_DATE", itemBatch.getProductDate(), doc, "text");
        PrintTempUtils.setTempData("IN_DATE", itemBatch.getInDate(), doc, "text");
        PrintTempUtils.setTempData("EX_DATE", itemBatch.getExDate(), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_CODE", itemBatch.getSupplierCode(), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_BATCH", itemBatch.getSupplierBatch(), doc, "text");
        //printTempInfoService.setTempData("QC_STATE",itemBatch.getQcState(),doc,"text");
        PrintTempUtils.setTempData("COXT_PRICE", itemBatch.getCostPrice(), doc, "text");
        PrintTempUtils.setTempData("SALE_PRICE", itemBatch.getSalePrice(), doc, "text");
        PrintTempUtils.setTempData("DETAIL_NO", itemBatch.getDetailNo(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_1", itemBatch.getMemo1(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_2", itemBatch.getMemo2(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_3", itemBatch.getMemo3(), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_1", itemBatch.getBatchAttribute1(), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_2", itemBatch.getBatchAttribute2(), doc, "text");

        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        if (doc.getElementById("FULL_ITEMS_DETAIL_TABLE") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = tbody.getElementById("ITEM_LIST_DETAIL");
            Integer colspan = 0;
            for (int i = 0; i < 5; i++) {
                Element newEle = ITEM_LIST_DETAIL.clone();

                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PICTURE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_ORIGINAL_SKU", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU_PLATFORM", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SKU_COMBO", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_CN", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_NAME_EN", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_WAREHOUSE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_GRID_CODE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PACKAGE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_QUANTITY", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_WEIGHT", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PROPERTY", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PRODUCT_UNIT", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_SALESPRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_TOTAL_AMOUNT_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_TOTAL_SALES_PRICE", currentUser.getUserName(), newEle, "text");
                colspan += PrintTempUtils.setTempData("ITEM_LIST_DETAIL_ITEM_ID", currentUser.getUserName(), newEle, "text");

                newEle.appendTo(tbody);
            }
            ITEM_LIST_DETAIL.remove();
        }

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        PrintData printData = new PrintData();
        printData.setData(tempHtml);
        printData.setQuantity(inventory.getQuantity().intValue());
        return printData;
    }

    @Autowired
    private SoDetailService soDetailService;
    @Autowired
    private PickTaskService pickTaskService;

    @Autowired
    private SoMasterService soMasterService;

    public void baoKuan(Integer acceptRecordId) {
        AcceptRecord acceptRecord = acceptRecordService.findById(acceptRecordId);
        Integer inventoryId = acceptRecord.getInventoryId();
        Inventory inventory = inventoryService.findById(inventoryId);
        List<SoDetailDto> soDetailDtos = soDetailService.findBaoKuanByItemCode(inventory.getItemCode(), inventory.getWareId(), inventory.getOrganizationId());
        if (soDetailDtos.size() > 0) {
            pickTaskService.createBaoKuanPickTask(inventory, acceptRecordId);

        }
    }

    public String itemPrint2(Integer batchId, CurrentUser currentUser) {

        ItemBatch itemBatch = itemBatchService.findById(batchId);
        String supplierCode = itemBatch.getSupplierCode();
        ItemInfo itemInfo = itemInfoService.findBy("itemCode", itemBatch.getItemCode());
        PrintTempInfo printTempInfo = printTempInfoService.findById(7);
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();

        PrintTempUtils.setTempData("ITEM_NAME", itemInfo.getItemName(), doc, "text");
        PrintTempUtils.setTempData("SPEC", itemInfo.getSpec(), doc, "text");
        PrintTempUtils.setTempData("MODEL", itemInfo.getModel(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE", itemBatch.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE_BAR_CODE", itemBatch.getItemCode(), doc, "barCode");
        PrintTempUtils.setTempData("ITEM_CODE_QR_CODE", itemBatch.getItemCode(), doc, "qrCode");// TODO xuesinuo 此方法根本没在用

        PrintTempUtils.setTempData("PD_DATE", itemBatch.getProductDate(), doc, "text");
        PrintTempUtils.setTempData("IN_DATE", itemBatch.getInDate(), doc, "text");
        PrintTempUtils.setTempData("EX_DATE", itemBatch.getExDate(), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_CODE", itemBatch.getSupplierCode(), doc, "text");
        PrintTempUtils.setTempData("SUPPLIER_BATCH", itemBatch.getSupplierBatch(), doc, "text");
        PrintTempUtils.setTempData("QC_STATE", itemBatch.getQcState(), doc, "text");
        PrintTempUtils.setTempData("COXT_PRICE", itemBatch.getCostPrice(), doc, "text");
        PrintTempUtils.setTempData("SALE_PRICE", itemBatch.getSalePrice(), doc, "text");
        PrintTempUtils.setTempData("DETAIL_NO", itemBatch.getDetailNo(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_1", itemBatch.getMemo1(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_2", itemBatch.getMemo2(), doc, "text");
        PrintTempUtils.setTempData("BILL_MEMO_3", itemBatch.getMemo3(), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_1", itemBatch.getBatchAttribute1(), doc, "text");
        PrintTempUtils.setTempData("BATCH_ATTRIBUTE_2", itemBatch.getBatchAttribute2(), doc, "text");

        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString().substring(0, 17), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString().substring(0, 9), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        return tempHtml;
    }

    /**
     * 批量验收,pda 盲收，asn明细验收
     */
    @PostMapping("/acceptList")
    @Authority("accept_records_acceptList")
    public Result acceptList(@RequestBody AcceptData acceptData, @User CurrentUser currentUser) {

        Integer userId = currentUser.getUserId();
        String userName = currentUser.getUserName();
        String acceptTime = DateUtils.getNowDateTimeString();

        for (int i = 0; i < acceptData.getAcceptInserts().size(); i++) {
            AcceptInsert acceptInsert = acceptData.getAcceptInserts().get(i);
            acceptInsert.setAcceptUserId(userId);
            acceptInsert.setAcceptUserName(userName);
            acceptInsert.setAcceptTime(acceptTime);

//            //货主开关不做限制的时候不用校验  货主做限制的时候把这个放开
//            ItemInfo itemInfo = itemInfoService.findBy("itemCode",acceptInsert.getItemCode());
//            if(!itemInfo.getOrganizationId().equals(acceptInsert.getOrganizationId())){
//                throw new ServiceException(CommonCode.ITEM_ORGANIZATION_ERROR);
//            }
//            CellInfoCriteria criteria = new CellInfoCriteria();
//            criteria.setCellCode(acceptInsert.getToCellCode());
//            criteria.setCellUseTypes(Arrays.asList(-1,0));
//            List<CellInfoDto> list= cellInfoService.findList(criteria);
//            if(!list.get(0).getWareId().equals(acceptInsert.getWareId())){
//                throw new ServiceException(CommonCode.WARE_CELL_ERROR);
//            }

//            如果进行的是盲收操作，且验收数量大于10,则先形成盲收审批单
            if(acceptInsert.getBillType()==3 && acceptInsert.getToQuantity()>=10){
                CellInfo cellInfo = cellInfoService.findByCodeAndWareId(
                        acceptInsert.getToCellCode(),
                        acceptInsert.getWareId());
                if (cellInfo == null) {
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "货位号无效");
                }
                AcceptAuditRecord acceptAuditRecord = new AcceptAuditRecord();
                BeanUtils.copyProperties(acceptInsert,acceptAuditRecord);
                acceptAuditRecord.setAcceptTime(new Date());
                acceptAuditRecord.setCellCode( cellInfo.getCellCode());
                acceptAuditRecord.setItemCode( acceptInsert.getItemCode());
                acceptAuditRecord.setItemName( acceptInsert.getItemName());
                acceptAuditRecord.setBoxCode(acceptInsert.getToBoxCode());
                acceptAuditRecord.setPackDetailId(acceptInsert.getToPackDetailId());
                acceptAuditRecord.setPackDescribe(acceptInsert.getToPackDescribe());
                acceptAuditRecord.setTransRatio(acceptInsert.getToTransRatio());
                acceptAuditRecord.setQuantity(acceptInsert.getToQuantity());
                acceptAuditRecordService.save(acceptAuditRecord);
            }else{
                if(acceptInsert.getToQuantity()<=0){
                    throw new ServiceException(CommonCode.SERVICE_ERROR, "验收数量至少为1！");
                }
                acceptRecordService.accept(acceptInsert);
            }


            //修改订单记录
           /* BillRecord billRecord1 =billRecordService.findByBillNoAndType(acceptData.getAcceptInserts().get(0).getAsnBillNo(),4);//ASN验收
            billRecordService.updateBillRecord(billRecord1,currentUser);*/

        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 入库日报表
     */
    @GetMapping("/findDayList")
    @Authority("accept_records_findDayList")
    public Result findDayList(AcceptRecordCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptRecord> list = acceptRecordService.findDayList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 入库日报表导出
     */
    @GetMapping("/exportDayList")
    @Authority("accept_records_exportDayList")
    public Result exportDayList(AcceptRecordCriteria criteria, @User CurrentUser currentUser) {

        criteria.setPageNum(1);
        criteria.setPageSize(10000);

        List<AcceptRecord> list = acceptRecordService.findDayList(criteria);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/asnDayList" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/asnDayListModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();
            DecimalFormat two = new DecimalFormat("#.#####");

            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                AcceptRecord acceptRecord = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String wareName = "-";
                if (acceptRecord.getWareName() != null) {
                    wareName = acceptRecord.getWareName();
                }
                String organizationName = "-";
                if (acceptRecord.getOrganizationName() != null) {
                    organizationName = acceptRecord.getOrganizationName();
                }
                String itemName = "-";
                if (acceptRecord.getItemName() != null) {
                    itemName = acceptRecord.getItemName();
                }
                String itemCode = "-";
                if (acceptRecord.getItemCode() != null) {
                    itemCode = acceptRecord.getItemCode();
                }

                String packDescribe = "-";
                if (acceptRecord.getPackDescribe() != null) {
                    packDescribe = acceptRecord.getPackDescribe();
                }

                String quantity = "-";
                if (acceptRecord.getQuantity() != null) {
                    quantity = two.format(acceptRecord.getQuantity());
                    sum += acceptRecord.getQuantity();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label wareNameLabel = new Label(1, i + 2, wareName);
                Label organizationNameLabel = new Label(2, i + 2, organizationName);
                Label itemNameLabel = new Label(3, i + 2, itemName);
                Label itemCodeLabel = new Label(4, i + 2, itemCode);
                Label packDescribeLabel = new Label(5, i + 2, packDescribe);
                Label quantityLabel = new Label(6, i + 2, quantity);

                indexLabel.setCellFormat(format);
                wareNameLabel.setCellFormat(format);
                organizationNameLabel.setCellFormat(format);
                itemNameLabel.setCellFormat(format);
                itemCodeLabel.setCellFormat(format);
                packDescribeLabel.setCellFormat(format);
                quantityLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(wareNameLabel);
                wSheet.addCell(organizationNameLabel);
                wSheet.addCell(itemNameLabel);
                wSheet.addCell(itemCodeLabel);
                wSheet.addCell(packDescribeLabel);
                wSheet.addCell(quantityLabel);

            }

            String sumStr = two.format(sum);
            Label sumLabel = new Label(6, list.size() + 3, sumStr);
            sumLabel.setCellFormat(format);
            wSheet.addCell(sumLabel);

            String nowStr = "打印日期：" + DateUtils.getNowDateString();
            Label dateLabel = new Label(0, list.size() + 4, nowStr);
            dateLabel.setCellFormat(format);
            wSheet.addCell(dateLabel);

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }

        String fullPath = fileS3Tool.save(FileUseType.EXPORT_ASN_DAY_LIST, "asnDayList.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }


    /*@ApiOperation(value = "归档采购信息 传billNo", notes = "归档采购信息")
    @PostMapping("/file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {
        if(currentUser == null){
            throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER, null);
        }
        for(int i=0;i<fileData.getBillNos().size();i++){
            String billNo = fileData.getBillNos().get(i);
            requestBuyService.createFile(billNo,currentUser);
        }

        return ResultGenerator.genSuccessResult();
    }*/
}
