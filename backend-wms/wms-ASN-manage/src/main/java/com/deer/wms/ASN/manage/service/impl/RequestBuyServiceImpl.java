package com.deer.wms.ASN.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.ASN.manage.dao.RequestBuyMapper;
import com.deer.wms.ASN.manage.model.buy.*;
import com.deer.wms.ASN.manage.model.file.RequestBuyDetailFile;
import com.deer.wms.ASN.manage.model.file.RequestBuyFile;
import com.deer.wms.ASN.manage.service.RequestBuyDetailFileService;
import com.deer.wms.ASN.manage.service.RequestBuyDetailService;
import com.deer.wms.ASN.manage.service.RequestBuyFileService;
import com.deer.wms.ASN.manage.service.RequestBuyService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.review.manage.model.BillRecord;
import com.deer.wms.review.manage.service.BillRecordService;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.deer.wms.system.manage.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by guo on 2020/01/02.
 */
@Service
@Transactional
public class RequestBuyServiceImpl extends AbstractService<RequestBuy, Integer> implements RequestBuyService {

    @Autowired
    private RequestBuyMapper requestBuyMapper;

    @Autowired
    private RequestBuyService requestBuyService;

    @Autowired
    private RequestBuyDetailService requestBuyDetailService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private RequestBuyFileService requestBuyFileService;

    @Autowired
    private RequestBuyDetailFileService requestBuyDetailFileService;

    @Autowired
    private ValidatorService validatorService;

    @Autowired
    private BillRecordService billRecordService;

    @Override
    public List<RequestBuyDto> findList(RequestBuyCriteria criteria) {
        return requestBuyMapper.findList(criteria);
    }

    @Override
    public void deleteByBillNo(String billNo) {
        requestBuyMapper.deleteByBillNo(billNo);
    }

    @Override
    @Transactional
    public void createRequestBuy(InsertData insertData, CurrentUser currentUser) {

        // 状态 -2 保存为草稿 2-等待审核(初始化) 1-审核通过 -1 -审核退回
        Integer state = 2;
        RequestBuy requestBuy = insertData.getRequestBuy();
        String tableName = "request_buy";
        String nextId = validatorService.getNextIdString(tableName);

        String billNo = "CG" + "-" + nextId;
        Integer createUserId = currentUser.getUserId();
        String createUserName = currentUser.getUserName();
        String createTime = DateUtils.getNowDateTimeString();

        // 添加采购单跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(7);// 采购新增
        billRecord.setCreateTime(createTime);
        billRecord.setCreateUserId(createUserId);
        billRecord.setCreateUserName(createUserName);
        billRecordService.save(billRecord);
        /**
         * 草稿
         */
        if (insertData.getInsertType() == -2) {
            state = 2;
            BillRecord billRecord1 = new BillRecord();
            billRecord1.setBillNo(billNo);
            billRecord1.setRecordType(8);// 采购草稿
            billRecord1.setCreateTime(createTime);
            billRecord1.setCreateUserId(createUserId);
            billRecord1.setCreateUserName(createUserName);
            billRecordService.save(billRecord1);
        }
        /**
         * 提交
         */
        else if (insertData.getInsertType() == 0) {
            String billNo1 = insertData.getRequestBuy().getBillNo();
            // 通过草稿提交，则需要先把草稿删掉
            if (billNo1 != null) {
                requestBuyService.deleteByBillNo(billNo1);
                BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo1, 8);// 采购草稿删除
                billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
                billRecord1.setModifyUserId(currentUser.getUserId());
                billRecord1.setModifyUserName(currentUser.getUserName());
                billRecordService.update(billRecord1);
            }
            /**
             * 添加审核流
             */
//        -1不通过  0-流转中 1-已通过
            state = caseMasterService.createCase(billNo, 1, currentUser.getUserId(), requestBuy.getWareId(), requestBuy.getOrganizationId());

        }
        requestBuy.setCreateUserId(createUserId);
        requestBuy.setCreateUserName(createUserName);
        requestBuy.setCreateTime(createTime);
        requestBuy.setBillNo(billNo);
        requestBuy.setState(state);
        requestBuy.setInsertType(insertData.getInsertType());
        requestBuy.setBillSource(insertData.getBillSource());
        requestBuyService.save(requestBuy);

        List<RequestBuyDetail> requestBuyDetailList = insertData.getRequestBuyDetails();
        for (int i = 0; i < requestBuyDetailList.size(); i++) {
            String detailNo = billNo + "-" + (i + 1);
            RequestBuyDetail requestBuyDetail = requestBuyDetailList.get(i);
            requestBuyDetail.setBillNo(billNo);
            requestBuyDetail.setDetailNo(detailNo);
            requestBuyDetailService.save(requestBuyDetail);
        }
        // 新增采购单新增节点信息
        if (insertData.getRequestBuy() != null
                && insertData.getRequestBuy().getListInvoiceFilePath() != null
                && insertData.getRequestBuy().getListInvoiceFilePath().size() > 0) {
            BillRecord invoiceBillRecord = new BillRecord();
            invoiceBillRecord.setBillNo(billNo);
            invoiceBillRecord.setRecordType(60);    // 60:创建invoice信息
            invoiceBillRecord.setMemo("上传附件");
            billRecordService.createBillRecord(invoiceBillRecord, currentUser);
        }
    }

    /**
     * 归档
     *
     * @param billNo
     * @param currentUser
     */
    @Override
    public void createFile(String billNo, CurrentUser currentUser) {
        RequestBuy requestBuy = requestBuyService.findBy("billNo", billNo);
        // 状态为 ASN完成状态
        if (requestBuy.getAcceptState() != 2) {
            throw new ServiceException(CommonCode.REQUEST_FILE_ERROR);
        }
        Integer fileUserId = currentUser.getUserId();
        String fileUserName = currentUser.getUserName();
        String fileTime = DateUtils.getNowDateTimeString();

        RequestBuyFile requestBuyFile = new RequestBuyFile();
        BeanUtils.copyProperties(requestBuy, requestBuyFile);

        requestBuyFile.setFileUserId(fileUserId);
        requestBuyFile.setFileUserName(fileUserName);
        requestBuyFile.setFileTime(fileTime);
        requestBuyFileService.save(requestBuyFile);

        // 添加采购单归档跟踪信息
        BillRecord billRecord = new BillRecord();
        billRecord.setBillNo(billNo);
        billRecord.setRecordType(11);// 采购归档
        billRecord.setCreateTime(fileTime);
        billRecord.setCreateUserId(fileUserId);
        billRecord.setCreateUserName(fileUserName);
        billRecordService.save(billRecord);

        List<RequestBuyDetail> requestBuyDetails = requestBuyDetailService.findByBillNo(billNo);

//        BeanUtils.copyProperties(requestBuyDetailList, requestBuyDetailFile);
        for (int i = 0; i < requestBuyDetails.size(); i++) {
            RequestBuyDetailFile requestBuyDetailFile = new RequestBuyDetailFile();
            requestBuyDetailFile.setBillNo(requestBuyDetails.get(i).getBillNo());
            requestBuyDetailFile.setDetailNo(requestBuyDetails.get(i).getDetailNo());
            requestBuyDetailFile.setItemCode(requestBuyDetails.get(i).getItemCode());
            requestBuyDetailFile.setItemName(requestBuyDetails.get(i).getItemName());
            requestBuyDetailFile.setPackDetailId(requestBuyDetails.get(i).getPackDetailId());
            requestBuyDetailFile.setPackDescribe(requestBuyDetails.get(i).getPackDescribe());
            requestBuyDetailFile.setAcceptQuantity(requestBuyDetails.get(i).getAcceptQuantity());
            requestBuyDetailFile.setExpectQuantity(requestBuyDetails.get(i).getExpectQuantity());
            requestBuyDetailFile.setAsnQuantity(requestBuyDetails.get(i).getAsnQuantity());
            requestBuyDetailFile.setBatchTacticCode(requestBuyDetails.get(i).getBatchTacticCode());
            requestBuyDetailFile.setMemo(requestBuyDetails.get(i).getMemo());
            requestBuyDetailFile.setAcceptTime(requestBuyDetails.get(i).getAcceptTime());
            requestBuyDetailFile.setState(requestBuyDetails.get(i).getState());
            requestBuyDetailFile.setPoUdfDs1(requestBuyDetails.get(i).getPoUdfDs1());
            requestBuyDetailFile.setPoUdfDs2(requestBuyDetails.get(i).getPoUdfDs2());
            requestBuyDetailFile.setPoUdfDs3(requestBuyDetails.get(i).getPoUdfDs3());

            requestBuyDetailFileService.save(requestBuyDetailFile);

        }
        requestBuyService.deleteByBillNo(billNo);
        BillRecord billRecord1 = billRecordService.findByBillNoAndType(billNo, 11);// 采购归档
        billRecord1.setModifyUserId(fileUserId);
        billRecord1.setModifyUserName(fileUserName);
        billRecord1.setModifyTime(DateUtils.getNowDateTimeString());
        billRecordService.update(billRecord1);
    }

    @Override
    public RequestBuyDto findByIdAndVersion(Integer requestBuyId, Integer version) {
        return requestBuyMapper.findByIdAndVersion(requestBuyId, version);
    }

    @Override
    public int update(RequestBuyDto requestBuyDto) {
        int rowCount = requestBuyMapper.update(requestBuyDto);
        if (rowCount == 0) {
            throw new ServiceException(CommonCode.ASN_VERSION, "update requestBuy更新了0条数据：" + requestBuyDto.getRequestBuyId() + "，" + requestBuyDto.getVersion());
        }
        return rowCount;
    }

    @Override
    public void updateInvoice(RequestBuyDto dto, CurrentUser currentUser) {
        RequestBuy old = this.findById(dto.getRequestBuyId());
        List<String> oldInvoiceList = JSON.parseArray(Np.i(old.getInvoiceFilePath()).o("[]"), String.class);
        BillRecord billRecord = new BillRecord();
        if (oldInvoiceList.isEmpty() && dto.getListInvoiceFilePath().isEmpty()) {
            return;// 没变化
        }
        if (!oldInvoiceList.isEmpty() && !dto.getListInvoiceFilePath().isEmpty()) {
            if (oldInvoiceList.stream().filter(oldInv -> !dto.getListInvoiceFilePath().contains(oldInv)).collect(Collectors.toList()).size() == 0
                    && oldInvoiceList.size() == dto.getListInvoiceFilePath().size()) {
                return;// 没变化
            }
            // 修改
            billRecord.setRecordType(62);   // 62:更新invoice信息
            billRecord.setMemo("更新附件");
        }
        if (oldInvoiceList.isEmpty() && !dto.getListInvoiceFilePath().isEmpty()) {
            // 新增
            billRecord.setRecordType(60);   // 60:创建invoice信息
            billRecord.setMemo("上传附件");
        }
        if (!oldInvoiceList.isEmpty() && dto.getListInvoiceFilePath().isEmpty()) {
            // 删除
            billRecord.setRecordType(61);   // 61:删除invoice信息
            billRecord.setMemo("删除附件");
        }
        this.update(dto);
        billRecord.setBillNo(old.getBillNo());
        billRecordService.createBillRecord(billRecord, currentUser);
    }

}
