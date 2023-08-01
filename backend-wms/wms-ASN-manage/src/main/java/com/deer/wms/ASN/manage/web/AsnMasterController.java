package com.deer.wms.ASN.manage.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.ASN.manage.model.asn.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.util.StrUtil;
import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptExCriteria;
import com.deer.wms.ASN.manage.model.AcceptExDto;
import com.deer.wms.ASN.manage.model.asn.*;
import com.deer.wms.ASN.manage.model.asn.CreateASN.AddInShipNo;
import com.deer.wms.ASN.manage.model.asn.CreateASN.CreateASN;
import com.deer.wms.ASN.manage.model.asn.CreateASN.Sku;
import com.deer.wms.ASN.manage.model.asn.CreateASN.UpdateSkuQuantity;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDto;
import com.deer.wms.ASN.manage.model.file.FileData;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.model.in.AcceptRecordCriteria;
import com.deer.wms.ASN.manage.service.*;
import com.deer.wms.base.system.model.Item.ItemInfo;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.SupplierManageService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.ExcelUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.model.carrier.Carrier;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.model.user.UserInfo;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.*;
import com.deer.wms.ware.task.model.Allot.AllotData;
import com.deer.wms.ware.task.model.Allot.AllotDetail;
import com.deer.wms.ware.task.model.Allot.AllotDetailCriteria;
import com.deer.wms.ware.task.model.Allot.AllotMaster;
import com.deer.wms.ware.task.service.AllotDetailService;
import com.deer.wms.ware.task.service.AllotMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ASN信息api接口
 * 
 * Created by guo on 2020/01/03.
 */
@RestController
@RequestMapping("/asn/masters")
public class AsnMasterController {

    @Autowired
    private AsnMasterService asnMasterService;

    @Autowired
    private AsnDetailService asnDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SupplierManageService supplierManageService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private PackDetailService packDetailService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private AcceptRecordService acceptRecordService;

    @Autowired
    private AsnShipDetailService asnShipDetailService;

    @Autowired
    private RequestBuyService requestBuyService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;

    /**
     * ERP--》WMS生成入库单
     */
    @PostMapping("/createASN")
    @Authority("asn_masters_createASN")
    public Result createASN(@RequestBody CreateASN createASN, @User CurrentUser currentUser) {

        String wareCode = createASN.getWareCode();
        String orgCode = createASN.getOrgCode();
        AsnMaster asnMaster = asnMasterService.findBy("billNo", createASN.getBillNo());
        if (asnMaster != null) {
            throw new ServiceException(CommonCode.BILL_NO);
        }
        if (wareCode == null) {
            throw new ServiceException(CommonCode.WARE_NULL);
        }
        if (orgCode == null) {
            throw new ServiceException(CommonCode.ORG_NULL);
        }

        asnMasterService.createASN(createASN, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/daochu")
    @Authority("asn_masters_daochu")
    public Result daochu (Integer asnMasterId, @User CurrentUser currentUser) throws BiffException, IOException, WriteException {

        AsnMaster asnMaster = asnMasterService.findById(asnMasterId);
        List<AsnDetail> list = asnDetailService.findByBillNo(asnMaster.getBillNo());



        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/asn" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/asn.xls";

            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(0, 3).getCellFormat();
            DecimalFormat two = new DecimalFormat("#.##");

            Label billNoLabel = new Label(1, 1, asnMaster.getBillNo());

            Label wareNameLabel = new Label(3, 1, asnMaster.getWareName());
            Label createTimeLabel = new Label(5, 1, asnMaster.getCreateTime());
            Label createUserName = new Label(7, 1, asnMaster.getCreateUserName());

            Label orgNamelabel = new Label(1, 2, asnMaster.getOrganizationName());
            Label supplierNameLabel = new Label(3, 2, asnMaster.getSupplierName());
            Label dcTimeLabel = new Label(5, 2, DateUtils.getNowDateTimeString());
            Label dcUserNameLabel = new Label(7, 2, currentUser.getUserName());

            wSheet.addCell(billNoLabel);
            wSheet.addCell(wareNameLabel);
            wSheet.addCell(createTimeLabel);
            wSheet.addCell(createUserName);
            wSheet.addCell(orgNamelabel);
            wSheet.addCell(supplierNameLabel);
            wSheet.addCell(dcTimeLabel);
            wSheet.addCell(dcUserNameLabel);

            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 4);

                AsnDetail asnDetail = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String sku = "-";
                if (asnDetail.getItemCode() != null) {
                    sku = asnDetail.getItemCode();
                }

                String itemName = "-";
                if (asnDetail.getItemName() != null) {
                    itemName = asnDetail.getItemName();
                }

                String pack = "-";
                if (asnDetail.getPackDescribe() != null) {
                    pack = asnDetail.getPackDescribe();
                }

                String acceptQuantity = "-";
                if (asnDetail.getAcceptQuantity() != null) {
                    acceptQuantity = asnDetail.getAcceptQuantity().toString();
                }

                String quantity = "-";
                if (asnDetail.getExpectQuantity() != null) {
                    quantity = asnDetail.getExpectQuantity().toString();
                }



                Label indexLabel = new Label(0, i + 4, indexStr);
                Label skuLabel = new Label(1, i + 4, sku);
                Label itemNameLabel = new Label(2, i + 4, itemName);
//                Label itemNameLabel2 = new Label(2, i + 4, "");
//                Label itemNameLabel3 = new Label(2, i + 4, "");

                Label packLabel = new Label(5, i + 4, pack);
                Label quantityLabel = new Label(6, i + 4, quantity);
                Label acceptQuantityLabel = new Label(7, i + 4, acceptQuantity);

                indexLabel.setCellFormat(format);
                skuLabel.setCellFormat(format);
                itemNameLabel.setCellFormat(format);
//                itemNameLabel2.setCellFormat(format);
//                itemNameLabel3.setCellFormat(format);

                packLabel.setCellFormat(format);
                quantityLabel.setCellFormat(format);
                acceptQuantityLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(skuLabel);

                wSheet.addCell(itemNameLabel);
//                wSheet.addCell(itemNameLabel2);
//                wSheet.addCell(itemNameLabel3);

                wSheet.addCell(packLabel);
                wSheet.addCell(quantityLabel);

                wSheet.addCell(acceptQuantityLabel);

                wSheet.mergeCells(2,i + 4, 2+2,i + 4);
            }


            copy.write();
            copy.close();
            workbook.close();

            String fullPath = fileS3Tool.save(FileUseType.EXPORT_ASN, "asn.xls", new java.io.File(staticPath + downloadPath));
            return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));

    }

    /**
     * ERP--》WMS推送入库快递单号
     */
    @PostMapping("/addInShipNo")
    @Authority("asn_masters_addInShipNo")
    public Result addInShipNo(@RequestBody AddInShipNo addInShipNo, @User CurrentUser currentUser) {


        AsnShipDetail asnShipDetail = asnShipDetailService.findBy("shipBillCode", addInShipNo.getShipBillNo());
        if (asnShipDetail == null) {
            asnShipDetail = new AsnShipDetail();
            asnShipDetail.setBillNo(addInShipNo.getBillNo());
            asnShipDetail.setShipBillCode(addInShipNo.getShipBillNo());
            asnShipDetail.setShipCode(addInShipNo.getShipCode());
            asnShipDetailService.save(asnShipDetail);
        } else {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "单号已存在:" + asnShipDetail.getShipBillCode());
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * ERP--》WMS修改数量
     */
    @PostMapping("/updateSkuQuantity")
    @Authority("asn_masters_updateSkuQuantity")
    public Result updateSkuQuantity(@RequestBody UpdateSkuQuantity updateSkuQuantity, @User CurrentUser currentUser) {


        AsnMaster asnMaster = asnMasterService.findSoByBillNo(updateSkuQuantity.getBillNo(), updateSkuQuantity.getWareCode());
        if (asnMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "单号无效！");
        }
        if (!asnMaster.getWareCode().equals(updateSkuQuantity.getWareCode())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库编码异常：" + asnMaster.getWareCode() + "||" + updateSkuQuantity.getWareCode());
        }
        if (updateSkuQuantity.getBillNo() == null || updateSkuQuantity.getBillNo().equals("")) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "没有传单号！");
        }
        List<Sku> skuList = updateSkuQuantity.getSkuList();
        for (Sku sku : skuList) {
            if (sku.getQuantity() == null || sku.getQuantity() < 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "数量必须大于0！");
            }
        }

        AcceptExCriteria acceptExCriteria = new AcceptExCriteria();
        acceptExCriteria.setBillNo(updateSkuQuantity.getBillNo());
        List<AcceptExDto> acceptExs = acceptExService.findList(acceptExCriteria);
        if (acceptExs.size() > 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该入库单已经有验收异常记录！");
        }
        AcceptRecordCriteria acceptRecordCriteria = new AcceptRecordCriteria();
        acceptRecordCriteria.setAsnBillNo(updateSkuQuantity.getBillNo());
        List<AcceptRecord> acceptRecords = acceptRecordService.findList(acceptRecordCriteria);
        if (acceptRecords.size() > 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "该订单已进行入库！");
        }
        if (!asnMaster.getWareCode().equals(updateSkuQuantity.getWareCode())) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库编码异常：" + asnMaster.getWareCode() + "||" + updateSkuQuantity.getWareCode());
        }
        for (Sku sku : skuList) {
            List<AsnDetail> list = asnDetailService.findByBillNo(updateSkuQuantity.getBillNo());
            for (AsnDetail asnDetail : list) {
                if (asnDetail.getItemCode().equals(sku.getSkuCode())) {
                    asnDetail.setExpectQuantity(sku.getQuantity());
                    asnDetailService.update(asnDetail);
                }
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * ERP--》WMS推送入库异常解决方案
     */
    @PostMapping("/addInExResult")
    @Authority("asn_masters_addInExResult")
    public Result addInExResult(@RequestBody InExResult inExResult, @User CurrentUser currentUser) {



        AcceptEx acceptEx = acceptExService.findById(Integer.parseInt(inExResult.getQcNo()));
        acceptEx.setFangan(inExResult.getExFangan());
        InExResult.ReturnInfo returnInfo = new InExResult.ReturnInfo();
        acceptEx.setReturnArea(returnInfo.getArea());
        acceptEx.setReturnCity(returnInfo.getCity());
        acceptEx.setReturnCountry(returnInfo.getCountry());
        acceptEx.setReturnDetailAddress(returnInfo.getDetailAddress());
        acceptEx.setReturnProvince(returnInfo.getProvince());
        acceptEx.setReturnLinkMan(returnInfo.getLinkMan());
        acceptEx.setReturnLinkPhone(returnInfo.getLinkPhone());
        acceptEx.setReturnExpressBillUrl(returnInfo.getExpressBillUrl());
        acceptEx.setReturnShipCode(returnInfo.getShipCode());
        acceptEx.setReturnShipBillCode(returnInfo.getShipBillCode());
        acceptEx.setState(2);
        acceptEx.setMemo(inExResult.getMemo());
        acceptExService.update(acceptEx);
        return ResultGenerator.genSuccessResult();

    }

    /**
     * 新增ASN信息
     */
    @PostMapping("/insert")
    @Authority("asn_masters_insert")
    public Result add(@RequestBody AsnData asnData, @User CurrentUser currentUser) {
        List<AsnDetail> asnDetails = asnData.getAsnDetails();
        for (AsnDetail asnDetail : asnDetails) {
            ItemInfo itemInfo = itemInfoService.selectItemWeightAndSpec(asnDetail.getItemCode(), asnData.getAsnMaster().getOrganizationId());
            if (itemInfo.getWeight() == null || itemInfo.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                throw  new ServiceException(CommonCode.SERVICE_ERROR, "此SKU:"+itemInfo.getItemCode()+"无重量信息，无法生成入库单",itemInfo);
            } else if (itemInfo.getItemLength() == null || itemInfo.getItemLength().compareTo(BigDecimal.ZERO) == 0 ||
                    itemInfo.getItemWidth() == null || itemInfo.getItemWidth().compareTo(BigDecimal.ZERO) == 0 ||
                    itemInfo.getItemHeight() == null || itemInfo.getItemHeight().compareTo(BigDecimal.ZERO) == 0) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "此SKU:"+itemInfo.getItemCode()+"无体积信息，无法生成入库单",itemInfo);
            }
        }
        RequestBuyDto requestBuy =requestBuyService.findByIdAndVersion(asnData.getRequestBuyId(),asnData.getRequestBuyVersion());
        if(asnData.getRequestBuyId()!=null && asnData.getRequestBuyVersion()!=null) {
            if (requestBuy == null) {
                throw new ServiceException(CommonCode.SERVICE_ERROR, "当前采购单状态已改变，请重新提交！");
            } else {
                //更新采购单
                requestBuy.setAcceptState(4);
                requestBuyService.update(requestBuy);
            }
        }
        asnMasterService.createAsnMaster(asnData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("asn_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        AsnMaster asnMaster = asnMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            asnMaster.setState(-1);
            asnMasterService.update(asnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 3);//ASN审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            asnMaster.setState(0);
            asnMasterService.update(asnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 3);//ASN审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            asnMaster.setState(2);
            asnMasterService.update(asnMaster);
        } else if (lastReviewResult == 1) {
            asnMaster.setState(1);
            asnMasterService.update(asnMaster);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 3);//ASN审核
            billRecord.setResult("通过");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("asn_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {

        AsnMaster asnMaster = asnMasterService.findBy("billNo", billNo);

        //初始化状态 2
        asnMaster.setState(2);
        asnMasterService.update(asnMaster);
        caseMasterService.createCase(billNo, 2, currentUser.getUserId(), asnMaster.getWareId(), asnMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 增删master、detail
     */
    @PostMapping("/update")
    @Authority("asn_masters_update")
    public Result update(@RequestBody AsnData asnData, @User CurrentUser currentUser) {

        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(asnData.getAsnMaster().getBillNo());
        billRecord.setRecordType(5);//ASN修改
        billRecord.setCreateTime(DateUtils.getNowDateTimeString());
        billRecord.setCreateUserId(currentUser.getUserId());
        billRecord.setCreateUserName(currentUser.getUserName());
        billRecordService.save(billRecord);
        AsnMaster asnMaster = asnData.getAsnMaster();
        asnMasterService.findByBillNo(asnMaster.getBillNo());

        AsnMasterDto queryAsnMaster = asnMasterService.findByBillNo(asnMaster.getBillNo());
        if (queryAsnMaster == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "asn单不存在");
        }
        asnMaster.setVersion(queryAsnMaster.getVersion());
        asnMasterService.update(asnMaster);
        List<AsnDetail> asnDetails = asnData.getAsnDetails();
        Map<Integer, AsnDetail> collect = asnDetailService.findByBillNo(asnMaster.getBillNo())
                .stream().collect(Collectors.toMap(AsnDetail::getAsnDetailId, Function.identity()));

        for (int i = 0; i < asnDetails.size(); i++) {
            AsnDetail asnDetail = asnDetails.get(i);
            AsnDetail queryAsnDetail = collect.get(asnDetail.getAsnDetailId());
            if (queryAsnDetail != null) {
                asnDetail.setVersion(queryAsnDetail.getVersion());
            }
            String detailNo = asnData.getAsnMaster().getBillNo() + "-" + (i + 1);
            asnDetail.setDetailNo(detailNo);
            asnDetail.setBillNo(asnData.getAsnMaster().getBillNo());
            asnDetail.setDetailNo(detailNo);

            if (asnData.getInsertType() == -2) {
                asnDetail.setState(2);
            } else if (asnData.getInsertType() == 0) {
                asnDetail.setState(0);
            }
            asnDetailService.update(asnDetail);
        }
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(asnData.getAsnMaster().getBillNo(), 5);//ASN修改
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecord1.setModifyUserId(currentUser.getUserId());
        billRecord1.setModifyUserName(currentUser.getUserName());
        billRecordService.update(billRecord1);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("asn_masters_detail")
    public Result detail(@PathVariable Integer id) {
        AsnMaster asnMaster = asnMasterService.findById(id);
        return ResultGenerator.genSuccessResult(asnMaster);
    }

    @GetMapping("/delete")
    @Authority("asn_masters_delete")
    public Result delete(Integer id) {
        asnMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/list")
    @Authority("asn_masters_list")
    public Result list(AsnMasterCriteria criteria) {
        if (criteria == null) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR);
        }
        // 2022-11-15 上线前的错误退货单均不再显示
        if (Np.i(criteria.getAsnType()).eq(44) && StrUtil.isBlank(criteria.getStartCreateTime())) {
            criteria.setStartCreateTime("2022-11-16");
            String endDateString = DateUtils.dateToStr(DateUtils.addDay(new Date(), 1), DateUtils.DEFAULT_DATE_FORMAT);
            criteria.setEndCreateTime(endDateString);
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AsnMasterDto> list = asnMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/pdaAccept")
    @Authority("asn_masters_pdaAccept")
    public Result pdaAccept(AsnMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize()*10);
        List<AsnMasterDto> list = asnMasterService.pdaAccept(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByBillNo")
    @Authority("asn_masters_findByBillNo")
    public Result findByBillNo(AsnMasterCriteria criteria) {
        AsnMasterDto asnMaster = asnMasterService.findByBillNo(criteria.getBillNo());
        return ResultGenerator.genSuccessResult(asnMaster);
    }

    /**
     * 归档ASN信息 传billNo
     */
    @PostMapping("/file")
    @Authority("asn_masters_file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {

        for (int i = 0; i < fileData.getBillNos().size(); i++) {
            String billNo = fileData.getBillNos().get(i);
            asnMasterService.createFile(billNo, currentUser);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * ASN单据导入
     */
    @Transactional
    @PostMapping("/importAsnBills")
    @Authority("asn_masters_importAsnBills")
    public Result importAsnBills(AsnImport excel, @User CurrentUser currentUser) {


        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";

        try {
            billWorkbook = jxl.Workbook.getWorkbook(excel.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            AsnMaster asnMaster = new AsnMaster();

            String tableName = "so_master";
            String nextId = validatorService.getNextIdString(tableName);
            String billNo = "ASN" + "-" + nextId;

            String wareName = ExcelUtils.getCellString(billSheet, 0, 2).trim();
            Integer wareId = null;
            WareInfo wareInfo = wareInfoService.findBy("wareName", wareName);
            if (wareInfo != null) {
                wareId = wareInfo.getWareId();

            } else {
                msg = "仓库名称错误！" + wareName;
                throw new Exception();
            }

            String organizationName = ExcelUtils.getCellString(billSheet, 1, 2).trim();
            Integer organizationId = null;
            Organization organization = organizationService.findBy("organizationName", organizationName);
            if (organization != null) {
                organizationId = organization.getOrganizationId();

            } else {
                msg = "货主名称错误！" + organizationName;
                throw new Exception();
            }
            String supplierName = ExcelUtils.getCellString(billSheet, 2, 2).trim();
            Integer supplierId = null;
            SupplierManage supplierManage = supplierManageService.findBy("supplierName", supplierName);
            if (supplierManage != null) {
                supplierId = supplierManage.getSupplierId();

            } else {
                msg = "供应商名称错误！" + supplierName;
                throw new Exception();
            }

            String platformCode = ExcelUtils.getCellString(billSheet, 3, 2).trim();
            String carrierName = ExcelUtils.getCellString(billSheet, 3, 2).trim();
            String carrierCode = null;
            Carrier carrier = carrierService.findBy("carrierName", carrierName);
            if (carrier != null) {
                carrierCode = carrier.getCarrierCode();

            } else {
                msg = "承运方名称错误！" + organizationName;
                throw new Exception();
            }

            String loadingPlace = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String deliveryPlace = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String trafficDescribe = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String asnUdfHs1 = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String asnUdfHs2 = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String asnUdfHs3 = ExcelUtils.getCellString(billSheet, 4, 2).trim();
            String expectTime = ExcelUtils.getCellString(billSheet, 4, 2).trim();

            String createUserName = ExcelUtils.getCellString(billSheet, 3, 2).trim();
            Integer userId = null;
            UserInfo userInfo = userInfoService.findBy("createUserName", createUserName);
            if (userInfo != null) {
                userId = userInfo.getUserId();

            } else {
                msg = "承运方名称错误！" + organizationName;
                throw new Exception();
            }

            String memo = ExcelUtils.getCellString(billSheet, 9, 2);
            asnMaster.setWareId(wareId);
            asnMaster.setWareName(wareName);
            asnMaster.setOrganizationId(organizationId);
            asnMaster.setOrganizationName(organizationName);
            asnMaster.setSupplierId(supplierId);
            asnMaster.setSupplierName(supplierName);
            asnMaster.setPlatformCode(platformCode);
            asnMaster.setCarrierCode(carrierCode);
            asnMaster.setLoadingPlace(loadingPlace);
            asnMaster.setDeliveryPlace(deliveryPlace);
            asnMaster.setTrafficDescribe(trafficDescribe);
            asnMaster.setAsnUdfHs1(asnUdfHs1);
            asnMaster.setAsnUdfHs2(asnUdfHs2);
            asnMaster.setAsnUdfHs3(asnUdfHs3);
            asnMaster.setState(0);
            String time = DateUtils.getNowDateTimeString();
            asnMaster.setCreateUserId(userId);
            asnMaster.setCreateUserName(createUserName);
            asnMaster.setCreateTime(time);
            asnMaster.setOrderNo(1);
            asnMaster.setExpectTime(expectTime);
            asnMaster.setBillSource(4); //导入
            asnMaster.setMemo(memo);

            Integer state = caseMasterService.createCase(billNo, 2, currentUser.getUserId(), wareId, organizationId);

            asnMaster.setState(state);

            asnMasterService.save(asnMaster);

            Integer rowCount = 0;
            for (int i = 6; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 1, i);
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }

            for (int i = 6; i < rowCount + 6; i++) {
                String itemName = ExcelUtils.getCellString(billSheet, 0, i).trim();
                if (itemName == null) {
                    msg = "缺少物料名称！" + itemName;
                    throw new Exception();
                }

                String packDescribe = ExcelUtils.getCellString(billSheet, 1, i);
                if (packDescribe == null) {
                    msg = "缺少单位！" + packDescribe;
                    throw new Exception();
                }

                Double expectQuantity = ExcelUtils.getCellDouble(billSheet, 2, i);
                if (expectQuantity == null) {
                    msg = "缺少预期到货数量！" + expectQuantity;
                    throw new Exception();
                }

                String memo1 = ExcelUtils.getCellString(billSheet, 3, i);
                String asnUdfDs1 = ExcelUtils.getCellString(billSheet, 4, i);
                String asnUdfDs2 = ExcelUtils.getCellString(billSheet, 5, i);
                String asnUdfDs3 = ExcelUtils.getCellString(billSheet, 6, i);
                ItemInfo itemInfo = itemInfoService.findBy("itemName", itemName);
                PackDetail packDetail = packDetailService.findBy("packDescribe", packDescribe);

                AsnDetail asnDetail = new AsnDetail();

                asnDetail.setBillNo(billNo);
                asnDetail.setDetailNo(billNo + "-" + (i - 6 + 1));
                asnDetail.setItemCode(itemInfo.getItemCode());
                asnDetail.setItemName(itemName);
                asnDetail.setPackDetailId(packDetail.getPackDetailId());
                asnDetail.setPackDescribe(packDescribe);
                asnDetail.setExpectQuantity(expectQuantity);
                asnDetail.setMemo(memo1);
                asnDetail.setAsnUdfDs1(asnUdfDs1);
                asnDetail.setAsnUdfDs2(asnUdfDs2);
                asnDetail.setAsnUdfDs3(asnUdfDs3);

                asnDetailService.save(asnDetail);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * ERP--》WMS入库单作废
     */
    @PostMapping("/disableASN")
    @Authority("asn_masters_disableASN")
    public Result disableASN(@RequestBody DisableASN disableASN, @User CurrentUser currentUser) {
        String wareCode = disableASN.getWareCode();
        if (StringUtils.isBlank(wareCode)) {
            throw new ServiceException(CommonCode.WARE_NULL);
        }
        WareInfo wareInfo = wareInfoService.findBy("wareCode", wareCode);
        if (wareInfo == null) {
            throw new ServiceException(CommonCode.WARE_NULL);
        }
        String orgCode = disableASN.getOrgCode();
        if (StringUtils.isBlank(orgCode)) {
            throw new ServiceException(CommonCode.ORG_NULL);
        }
        List<AsnDetail> asnDetails = asnDetailService.findByBillNo(disableASN.getBillNo());
        if (asnDetails.stream().filter(item -> !Objects.equals(item.getState(), 0)).count() > 0) {
            throw new ServiceException(CommonCode.CANCEL_ACCEPT_CODE, CommonCode.CANCEL_ACCEPT_CODE.getMessage());
        }
        AsnMaster asnMaster = asnMasterService.findSoByBillNo(disableASN.getBillNo(), disableASN.getWareCode());
        if (asnMaster == null) {
            throw new ServiceException(CommonCode.BILL_NO);
        }
        asnMaster.setIsDeleted("y");
        asnMasterService.update(asnMaster);
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private AllotMasterService allotMasterService;
    @Autowired
    private AllotDetailService allotDetailService;

    /**
     * 调拨生成ASN、SO
     */
    @PostMapping("/generate")
    @Authority("asn_masters_generate")
    public Result generate(@RequestBody AllotData allotData, @User CurrentUser currentUser) {

        AllotMaster allotMaster = allotMasterService.findBy("billNo",allotData.getAllotBillNo());
        AllotDetailCriteria criteria = new AllotDetailCriteria();
        criteria.setBillNo(allotData.getAllotBillNo());
        List<AllotDetail> list = allotDetailService.findList(criteria);
        allotData.setAllotMaster(allotMaster);
        allotData.setAllotDetails(list);
        asnMasterService.allot(allotData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

}
