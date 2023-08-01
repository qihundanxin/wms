package com.deer.wms.ASN.manage.web;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.ASN.manage.model.asn.AsnMaster;
import com.deer.wms.ASN.manage.model.buy.InsertData;
import com.deer.wms.ASN.manage.model.buy.RequestBuy;
import com.deer.wms.ASN.manage.model.buy.RequestBuyCriteria;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetail;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDetailDto;
import com.deer.wms.ASN.manage.model.buy.RequestBuyDto;
import com.deer.wms.ASN.manage.model.buy.RequestBuyLog;
import com.deer.wms.ASN.manage.model.buy.UpdateData;
import com.deer.wms.ASN.manage.model.file.FileData;
import com.deer.wms.ASN.manage.service.AsnMasterService;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.ASN.manage.service.RequestBuyLogService;
import com.deer.wms.ASN.manage.service.RequestBuyService;
import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 采购信息api接口
 * 
 * Created by guo on 2020/01/02.
 */
@RestController
@RequestMapping("/request/buys")
public class RequestBuyController {

	@Autowired
    private AsnMasterService asnMasterService;
	
    @Autowired
    private RequestBuyService requestBuyService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private RequestBuyDetailService requestBuyDetailService;

    @Autowired
    private BillRecordService billRecordService;

	@Autowired
	private RequestBuyLogService requestBuyLogService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;


    /**
     * 新增采购信息
     */
    @PostMapping("/insert")
    @Authority("request_buys_insert")
    public Result add(@RequestBody InsertData insertData, @User CurrentUser currentUser) {

        if (insertData == null || insertData.getRequestBuy() == null || StrUtil.isBlank(insertData.getRequestBuy().getExpectTime())) {
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "预期到货日期不可为空");
        }
        // 新增采购单的时候,采购总价不能为空
        if (insertData.getRequestBuy().getBuyTotalPrices()==null){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "采购总价不能为空");
        }
        if (insertData.getRequestBuy().getListInvoiceFilePath() != null) {
            insertData.getRequestBuy().setInvoiceFilePath(JSON.toJSONString(insertData.getRequestBuy().getListInvoiceFilePath()));
        }

        requestBuyService.createRequestBuy(insertData, currentUser);
               return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/daochu")
    @Authority("request_buys_daochu")
	public Result daochu(Integer requestBuyId, @User CurrentUser currentUser)
			throws BiffException, IOException, WriteException {

        RequestBuy requestBuy = requestBuyService.findById(requestBuyId);
        List<RequestBuyDetail> list = requestBuyDetailService.findByBillNo(requestBuy.getBillNo());



        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/po" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/po.xls";

        String modelFilePath = staticPath + modelPath;
        String filePath = staticPath + downloadPath;

        jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
        WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

        WritableSheet wSheet = copy.getSheet(0);
        CellFormat format = wSheet.getCell(0, 3).getCellFormat();
        DecimalFormat two = new DecimalFormat("#.##");

        Label billNoLabel = new Label(1, 1, requestBuy.getBillNo());

        Label wareNameLabel = new Label(3, 1, requestBuy.getWareName());
        Label createTimeLabel = new Label(5, 1, requestBuy.getCreateTime());
        Label createUserName = new Label(7, 1, requestBuy.getCreateUserName());

        Label orgNamelabel = new Label(1, 2, requestBuy.getOrganizationName());
        Label supplierNameLabel = new Label(3, 2, requestBuy.getSupplierName());
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

            RequestBuyDetail asnDetail = list.get(i);

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

            String userDF1 = "-";
            if (asnDetail.getPoUdfDs1() != null) {
            	userDF1 = asnDetail.getPoUdfDs1().toString();
            }
            String userDF2 = "-";
            if (asnDetail.getPoUdfDs2() != null) {
            	userDF2 = asnDetail.getPoUdfDs2().toString();
            }
            String userDF3 = "-";
            if (asnDetail.getPoUdfDs3() != null) {
            	userDF3 = asnDetail.getPoUdfDs3().toString();
            }


            Label indexLabel = new Label(0, i + 4, indexStr);
            Label skuLabel = new Label(1, i + 4, sku);
            Label itemNameLabel = new Label(2, i + 4, itemName);
//                Label itemNameLabel2 = new Label(2, i + 4, "");
//                Label itemNameLabel3 = new Label(2, i + 4, "");

            Label packLabel = new Label(5, i + 4, pack);
            Label quantityLabel = new Label(6, i + 4, quantity);
            Label acceptQuantityLabel = new Label(7, i + 4, acceptQuantity);

            Label userDF1Label = new Label(8, i + 4, userDF1);
            Label userDF2Label = new Label(9, i + 4, userDF2);
            Label userDF3Label = new Label(10, i + 4, userDF3);

            indexLabel.setCellFormat(format);
            skuLabel.setCellFormat(format);
            itemNameLabel.setCellFormat(format);
//                itemNameLabel2.setCellFormat(format);
//                itemNameLabel3.setCellFormat(format);

            packLabel.setCellFormat(format);
            quantityLabel.setCellFormat(format);
            acceptQuantityLabel.setCellFormat(format);
            
            userDF1Label.setCellFormat(format);
            userDF2Label.setCellFormat(format);
            userDF3Label.setCellFormat(format);

            wSheet.addCell(indexLabel);
            wSheet.addCell(skuLabel);

            wSheet.addCell(itemNameLabel);
//                wSheet.addCell(itemNameLabel2);
//                wSheet.addCell(itemNameLabel3);

            wSheet.addCell(packLabel);
            wSheet.addCell(quantityLabel);
            wSheet.addCell(acceptQuantityLabel);
            wSheet.addCell(userDF1Label);
            wSheet.addCell(userDF2Label);
            wSheet.addCell(userDF3Label);

            wSheet.mergeCells(2,i + 4, 2+2,i + 4);
        }


        copy.write();
        copy.close();
        workbook.close();

        String fullPath = fileS3Tool.save(FileUseType.EXPORT_PO, "po.xls", new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("request_buys_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        RequestBuy requestBuy = requestBuyService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            requestBuy.setState(-1);
            requestBuyService.update(requestBuy);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 9);//采购审核
            billRecord.setResult("退回");
            billRecord.setModifyTime(DateUtils.getNowDateTimeString());
            billRecord.setModifyUserId(currentUser.getUserId());
            billRecord.setModifyUserName(currentUser.getUserName());
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 0) {
            requestBuy.setState(0);
            requestBuyService.update(requestBuy);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 9);//采购审核
            billRecord.setResult("流转");
            billRecordService.update(billRecord);
        } else if (lastReviewResult == 2) {
            requestBuy.setState(2);
            requestBuyService.update(requestBuy);
        } else if (lastReviewResult == 1) {
            requestBuy.setState(1);
            requestBuyService.update(requestBuy);
            BillRecord billRecord = billRecordService.findByBillNoAndType(billNo, 9);//采购审核
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
    @Authority("request_buys_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {

        RequestBuy requestBuy = requestBuyService.findBy("billNo", billNo);

        //初始化状态 2
        requestBuy.setState(2);
        requestBuyService.update(requestBuy);
        caseMasterService.createCase(billNo, 1, currentUser.getUserId(), requestBuy.getWareId(), requestBuy.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 采购单强制完成
     */
    @GetMapping("/forcecompletion")
    @Authority("request_buys_forcecompletion")
    public Result forcecompletion (Integer id,String reason, @User CurrentUser currentUser) throws BiffException, IOException, WriteException {
        //2022 berwin 仓库老板需求：
    	//采购单强制完成-不改数-可以填写原因   
    	if (currentUser == null) {
            throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER);
        }
    	//process RequestBuy
    	RequestBuy requestBuy = requestBuyService.findById(id);
    	requestBuy.setAcceptState(6);//berwin 采购强制完成
    	requestBuy.setForceCompletionReason(reason);
        requestBuy.setFcUserId(currentUser.getUserId());
        requestBuy.setFcUserName(currentUser.getUserName());
    	requestBuyService.update(requestBuy);
    	
    	String billNO = requestBuy.getBillNo();
    	List<RequestBuyDetail> requestBuyDetails = requestBuyDetailService.findByBillNo(billNO);//insertData.getRequestBuyDetails();

	    for (RequestBuyDetail requestBuyDetail : requestBuyDetails) {
	        requestBuyDetail.setState(3);
	        requestBuyDetailService.update(requestBuyDetail);
	    }
	    
	    asnMasterService.forceCompletion(billNO);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("request_buys_delete")
    public Result delete(Integer id) {
        RequestBuy requestBuy = requestBuyService.findById(id);
        //根据采购单查询入库单，只要有入库单就不可以删除
        List<AsnMaster> asnMasters = asnMasterService.findByRequestBuyBillNO(requestBuy.getBillNo());
        if(asnMasters!=null && asnMasters.size()!=0){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "此采购单已生成入库单，无法删除!");
        }
        requestBuyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 修改master、detail
     */
    @PostMapping("/update")
    @Authority("request_buys_update")
    public Result update(@RequestBody UpdateData updateData, @User CurrentUser currentUser) {
        RequestBuy oldRequestBuy = requestBuyService.findById(updateData.getRequestBuy().getRequestBuyId());
		List<RequestBuyDetail> oldRequestBuyDetailList = requestBuyDetailService
				.findByBillNo(updateData.getRequestBuy().getBillNo());
        RequestBuy requestBuy = updateData.getRequestBuy();
        // 不更新发票
        requestBuy.setInvoiceFilePath(null);
        //判断原数据有没有采购总价，如果有，采购总价必填，如果没有 可以为空。
        if (oldRequestBuy.getBuyTotalPrices() != null){
            if (requestBuy.getBuyTotalPrices()==null){
                throw new ServiceException(CommonCode.PARAMETER_ERROR, "采购总价不能为空");
            }
        }
		// 重置审核状态
		caseMasterService.deleteByBillNo(requestBuy.getBillNo());
		int state = caseMasterService.createCase(requestBuy.getBillNo(), 1, currentUser.getUserId(),
				requestBuy.getWareId(), requestBuy.getOrganizationId());
		requestBuy.setState(state);
		// 计算主表状态
		boolean acceptStateHas0 = false;
		boolean acceptStateHas2 = false;
		boolean acceptStateHas3 = false;
		boolean acceptStateHas5 = false;
		// 更新从表
		List<RequestBuyDetailDto> paramDetailList = updateData.getRequestBuyDetails();

		Map<String, RequestBuyDetailDto> paramDetailMap = paramDetailList.stream()
				.collect(Collectors.toMap(x -> x.getItemCode(), x -> x));
		List<RequestBuyDetail> oDetailList = requestBuyDetailService.findByBillNo(requestBuy.getBillNo());
		for (RequestBuyDetail oDetail : oDetailList) {

            RequestBuyDetailDto paramDetail = paramDetailMap.get(oDetail.getItemCode());
            RequestBuyDetailDto updater = new RequestBuyDetailDto();
            BeanUtils.copyProperties(oDetail, updater);
            //如果详情没有被删除
            if (paramDetail != null) {// 修改明细
                if (paramDetail.getExpectQuantity() != null) {
                    // 注意：这里是允许修改的明细！
                    updater.setExpectQuantity(paramDetail.getExpectQuantity());// 数量
                    updater.setPriceIn(paramDetail.getPriceIn());// 单价
                    updater.setPriceInSetNull(paramDetail.getPriceInSetNull());// 单价（置空）
                    updater.setTaxRateIn(paramDetail.getTaxRateIn());// 税率
                    updater.setTaxRateInSetNull(paramDetail.getTaxRateInSetNull());// 税率（置空）
                    updater.setPoUdfDs1(paramDetail.getPoUdfDs1());// 自定义
                    updater.setPoUdfDs2(paramDetail.getPoUdfDs2());
                    updater.setPoUdfDs3(paramDetail.getPoUdfDs3());
                }
				paramDetailMap.remove(oDetail.getItemCode());
			} else {// 删除明细,只修改入库单数量为0,不删除此详情信息
                updater.setExpectQuantity(0D);
			}
			if (oDetail.getAcceptQuantity().equals(0D) && !oDetail.getExpectQuantity().equals(0D)) {
                updater.setState(0);// 初始化
				acceptStateHas0 = true;
			} else if (oDetail.getAcceptQuantity() < oDetail.getExpectQuantity()) {
                updater.setState(2);// 部分收货
				acceptStateHas2 = true;
			} else if (oDetail.getAcceptQuantity() > oDetail.getExpectQuantity()) {
                updater.setState(5);// 超额收货
				acceptStateHas5 = true;
			} else {
                updater.setState(3);// 全部收货
				acceptStateHas3 = true;
			}
            requestBuyDetailService.updateDetail(updater);
		}
		int detailIndex = oDetailList.size();
        //如果修改-后续新增详情的情况
		for (Map.Entry<String, RequestBuyDetailDto> entry : paramDetailMap.entrySet()) {
			RequestBuyDetail paramDetail = entry.getValue();
			paramDetail.setState(0);
			acceptStateHas0 = true;
			paramDetail.setBillNo(requestBuy.getBillNo());
			paramDetail.setDetailNo(requestBuy.getBillNo() + "-" + (++detailIndex));
			requestBuyDetailService.save(paramDetail);
		}
		// 更新主表
		if (acceptStateHas0 && !acceptStateHas2 && !acceptStateHas3 && !acceptStateHas5) {
			requestBuy.setAcceptState(0);
		} else if (acceptStateHas0 || acceptStateHas2) {
			requestBuy.setAcceptState(2);
		} else if (!acceptStateHas5) {
			requestBuy.setAcceptState(3);
		} else {
			requestBuy.setAcceptState(5);
		}
		//更换这个方法 修改服务费和其他费用可以为NULL
        RequestBuyDto requestBuyDto = new RequestBuyDto();
        BeanUtils.copyProperties(requestBuy,requestBuyDto);
        requestBuyDto.setServiceChargeSetNull(updateData.getRequestBuy().getServiceChargeSetNull());
        requestBuyDto.setOtherExpensesSetNull(updateData.getRequestBuy().getOtherExpensesSetNull());
        requestBuyService.update(requestBuyDto);

		// 日志表记录
		RequestBuy newRequestBuy = requestBuyService.findById(updateData.getRequestBuy().getRequestBuyId());
		List<RequestBuyDetail> newRequestBuyDetailList = requestBuyDetailService
				.findByBillNo(updateData.getRequestBuy().getBillNo());
		RequestBuyLog log = new RequestBuyLog();
		log.setCreateUserId(currentUser.getUserId());
		log.setCreateTime(new Date());
		log.setCreateUserName(currentUser.getUserName());
		log.setBillNo(updateData.getRequestBuy().getBillNo());
		log.setOperationType(2);
		Map<String, Object> oldData = new HashMap<>();
		oldData.put("requestBy", oldRequestBuy);
		oldData.put("requestByDetail", oldRequestBuyDetailList);
		Map<String, Object> newData = new HashMap<>();
		newData.put("requestBy", newRequestBuy);
		newData.put("requestByDetail", newRequestBuyDetailList);
		log.setBeforeData(JSON.toJSONString(oldData));
		log.setData(JSON.toJSONString(newData));
		requestBuyLogService.save(log);
		// 单据记录
		BillRecord billRecord = billRecordService.findByBillNoAndType(updateData.getRequestBuy().getBillNo(), 14);// 采购修改
		if (billRecord.getBillRecordId() == null) {
			billRecord.setBillNo(updateData.getRequestBuy().getBillNo());
			billRecord.setRecordType(14);
			billRecord.setCreateTime(DateUtils.getNowDateTimeString());
			billRecord.setCreateUserId(currentUser.getUserId());
			billRecord.setCreateUserName(currentUser.getUserName());
			billRecordService.save(billRecord);
		} else {
			billRecord.setModifyTime(DateUtils.getNowDateTimeString());
			billRecord.setModifyUserId(currentUser.getUserId());
			billRecord.setModifyUserName(currentUser.getUserName());
			billRecordService.update(billRecord);
		}
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("request_buys_detail")
    public Result detail(Integer id) {
        RequestBuy requestBuy = requestBuyService.findById(id);
        return ResultGenerator.genSuccessResult(requestBuy);
    }

    /**
     * 采购信息
     */
    @GetMapping("/list")
    @Authority("request_buys_list")
    public Result list(RequestBuyCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<RequestBuyDto> list = requestBuyService.findList(criteria);
        list.forEach(dto -> {
            if (StrUtil.isNotBlank(dto.getInvoiceFilePath())) {
                dto.setListInvoiceFilePath(JSON.parseArray(dto.getInvoiceFilePath(), String.class));
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 归档采购信息 传billNo
     */
    @PostMapping("/file")
    @Authority("request_buys_file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {

        for (int i = 0; i < fileData.getBillNos().size(); i++) {
            String billNo = fileData.getBillNos().get(i);
            requestBuyService.createFile(billNo, currentUser);
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 采购Invoice上传
     */
    @PostMapping("/uploadInvoice")
    @Authority("request_buys_uploadInvoice")
    public Result uploadInvoice(@RequestParam("file") MultipartFile multipartFile,String billNo,@User CurrentUser currentUser) throws IOException {
        if (multipartFile.getSize() > 10 * 1024 * 1024) {
            throw new ServiceException(CommonCode.UPLOAD_FILE_ERROR,"单一文件大小不超过10MB");
        }
        //获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //检查文件拓展名
        String extName = FileNameUtil.extName(fileName);
        if (!extName.equals("png")&&!extName.equals("jpg")&&!extName.equals("jpeg")&&!extName.equals("pdf")){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "传文件格式错误，请上传PNG、JPG、JPEG、PDF格式");
        }
        String fileTempPath = fileSetting.getStaticFileDir() + fileSetting.getTempPath() + "/" + fileName;
        File file = new File(fileTempPath);
        if (!FileUtil.isDirectory(fileSetting.getStaticFileDir() + fileSetting.getTempPath())) {
            FileUtil.mkdir(fileSetting.getStaticFileDir() + fileSetting.getTempPath());
        }
        multipartFile.transferTo(file);
        String fullPath = fileS3Tool.save(FileUseType.UPLOAD_CG_INVOICE, fileName, file);
        //将Url返回回去
        return ResultGenerator.genSuccessResult(SimpleMap.init().append("fullPath", fullPath));
    }

    /**
     * 采购Invoice预览
     */
    @GetMapping("/selectInvoice")
    @Authority("request_buys_selectInvoice")
    public Result selectInvoice(@RequestParam("fullPath") String fullPath, @User CurrentUser currentUser) throws IOException {
        String url = fileS3Tool.getUrl(fullPath, 1 * 24 * 60 * 60, null);
        //直接返回前端用户可打开的URL
        return ResultGenerator.genSuccessResult(SimpleMap.init().append("url",url));
    }

    /**
     * 修改页面上传invoice附件
     */
    @PostMapping("/detailUploadInvoice")
    @Authority("request_buys_detailUploadInvoice")
    public Result detailUploadInvoice(@RequestBody RequestBuyDto dto, @User CurrentUser currentUser) throws IOException {
        List<String> listInvoiceFilePath = dto.getListInvoiceFilePath();
        if (listInvoiceFilePath == null || listInvoiceFilePath.size() <= 0) {
            listInvoiceFilePath = new ArrayList<>();
        }
        dto.setInvoiceFilePath(JSON.toJSONString(listInvoiceFilePath));

        requestBuyService.updateInvoice(dto, currentUser);
        return ResultGenerator.genSuccessResult();
    }

}
