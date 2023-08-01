package com.deer.wms.quartz.task;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptLog;
import com.deer.wms.ASN.manage.model.SjcUpperTask;
import com.deer.wms.ASN.manage.model.SjcUpperTaskCriteria;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.model.in.AcceptRecord;
import com.deer.wms.ASN.manage.service.*;
import com.deer.wms.inventory.model.Inventory.Inventory;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.SO.SoDetail;
import com.deer.wms.ware.task.service.SoDetailService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component("acceptLogTask")
public class AcceptLogTask {

    @Resource
    private AcceptLogService acceptLogService;

    @Resource
    private AcceptRecordService acceptRecordService;

    @Resource
    private SjcUpperTaskService sjcUpperTaskService;

    @Resource
    private AcceptExService acceptExService;

    @Resource
    private InventoryService inventoryService;

    @Resource
    private SoDetailService soDetailService;

    @Resource
    private AsnDetailService asnDetailService;

    public void createAcceptLog() {
        Date now = DateUtils.now();
        // 查询已上架的签收记录
        List<AcceptRecord> acceptRecordList = acceptRecordService.findUnUpList();
        if (CollectionUtils.isEmpty(acceptRecordList)) {
            return;
        }
        List<AcceptLog> acceptLogList = Lists.newArrayList();
        for (AcceptRecord acceptRecord : acceptRecordList) {
            AcceptLog acceptLog = new AcceptLog();
            acceptLog.setAcceptRecordId(acceptRecord.getAcceptRecordId());
            acceptLog.setWareId(acceptRecord.getWareId());
            acceptLog.setWareName(acceptRecord.getWareName());
            acceptLog.setItemCode(acceptRecord.getItemCode());
            acceptLog.setItemName(acceptRecord.getItemName());
            acceptLog.setItemOriginalName(null);
            acceptLog.setImgUrl(acceptRecord.getImgUrl());
            acceptLog.setSpec(acceptRecord.getSpec());
            acceptLog.setModel(acceptRecord.getModel());
            acceptLog.setBatchId(acceptRecord.getBatchId());
            acceptLog.setBatchName(acceptRecord.getBatchName());
            acceptLog.setCellCode(acceptRecord.getCellCode());
            acceptLog.setBillType(acceptRecord.getBillType());
            acceptLog.setAcceptType(1);
            acceptLog.setQcType(1);
            acceptLog.setAcceptState(acceptRecord.getState());
            acceptLog.setAcceptUserId(acceptRecord.getAcceptUserId());
            acceptLog.setAcceptUserName(acceptRecord.getAcceptUserName());
            acceptLog.setAcceptTime(DateUtils.strToDate(acceptRecord.getAcceptTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
            // 关联上架车信息
            if (Objects.nonNull(acceptRecord.getInventoryId())) {
                List<SjcUpperTask> sjcList = sjcUpperTaskService.findList(new SjcUpperTaskCriteria() {{
                    setAcceptInventoryId(acceptRecord.getInventoryId());
                }});
                if (!CollectionUtils.isEmpty(sjcList)) {
                    SjcUpperTask sjcUpperTask = sjcList.get(0);
                    acceptLog.setUpUserId(sjcUpperTask.getUpUserId());
                    acceptLog.setUpUserName(sjcUpperTask.getUpUserName());
                    acceptLog.setUpTime(DateUtils.strToDate(sjcUpperTask.getUpTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
                }
            } else {
                acceptLog.setAcceptType(2);
            }
            AsnDetail asnDetail = asnDetailService.findBy("detailNo", acceptRecord.getAsnDetailNo());
            acceptLog.setExpectQuantity(asnDetail.getExpectQuantity());
            acceptLog.setQuantity(acceptRecord.getQuantity());
            // 关联验收异常信息
            if (Objects.nonNull(acceptRecord.getAcceptExId())) {
                AcceptEx acceptEx = acceptExService.findById(acceptRecord.getAcceptExId());
                if (acceptEx != null) {
                    acceptLog.setExQuantity(acceptEx.getQuantity());
                }
            } else {
                acceptLog.setExQuantity(0.0d);
            }
            acceptLog.setBillNo(acceptRecord.getAsnBillNo());
            acceptLog.setCreateTime(now);
            Inventory inventory = inventoryService.findById(acceptRecord.getInventoryId());
            if (inventory == null) {
                acceptLog.setEndStatus(1);
            }
            acceptLogService.insertUnique(acceptLog);

//            acceptLogList.add(acceptLog);
        }
//        acceptLogService.save(acceptLogList);
    }

}
