package com.deer.wms.quartz.task;

import com.deer.wms.base.system.enums.SendState;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.SO.SoMasterCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SoLog;
import com.deer.wms.ware.task.model.pickTask.PickTask;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoLogService;
import com.deer.wms.ware.task.service.SoMasterService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component("soLogTask")
public class SoLogTask {

    @Resource
    private SoMasterService soMasterService;

    @Resource
    private SoLogService soLogService;

    @Resource
    private PickTaskService pickTaskService;

    public void createAcceptLog() {
        Date now = DateUtils.now();
//        Date maxLogDate = soLogService.getMaxLogDate();
//        SoMasterCriteria soMasterCriteria = new SoMasterCriteria();
//        soMasterCriteria.setState(8);
//        if (maxLogDate != null) {
//            soMasterCriteria.setStartCreateTime(DateUtils.dateToStr(maxLogDate, DateUtils.DEFAULT_DATETIME_FORMAT));
//            soMasterCriteria.setEndCreateTime("2099-01-01 00:00:00");
//        }
        List<SoMasterDto> soMasterDtoList = soMasterService.getUnCompleteLog();
        if (CollectionUtils.isEmpty(soMasterDtoList)) {
            return;
        }
        List<SoLog> soLogs = Lists.newArrayList();
        for (SoMasterDto soMasterDto : soMasterDtoList) {
            SoLog soLog = new SoLog();
            soLog.setSoMasterId(soMasterDto.getSoMasterId());
            soLog.setWareId(soMasterDto.getWareId());
            soLog.setWareName(soMasterDto.getWareName());
            soLog.setBillNo(soMasterDto.getBillNo());
            // 查询集货位
            List<PickTask> pickTaskLists = pickTaskService.findBySoMasterId(soMasterDto.getSoMasterId());
            if (!CollectionUtils.isEmpty(pickTaskLists)) {
                PickTask pickTask = pickTaskLists.get(0);
                soLog.setPickCellCode(pickTask.getToCellCode());
                soLog.setSortCellCode(pickTask.getToCellCode());
                soLog.setPickTime(DateUtils.strToDate(pickTask.getPickTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
                soLog.setPickUserId(pickTask.getPickUserId());
                soLog.setPickUserName(pickTask.getPickUserName());
            }
            soLog.setWeight(soMasterDto.getWeight());
            soLog.setShop(soMasterDto.getShop());
            soLog.setStatus(1);
            soLog.setSoStructure(soMasterDto.getSoStructure());
            soLog.setCreateTime(DateUtils.strToDate(soMasterDto.getCreateTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
            soLog.setCreateUserId(soMasterDto.getCreateUserId());
            soLog.setCreateUserName(soMasterDto.getCreateUserName());
            // 拣货人
            // 打包、发货人
            soLog.setOutTime(DateUtils.strToDate(soMasterDto.getOutTime(), DateUtils.DEFAULT_DATETIME_FORMAT));
            soLog.setOutUserId(soMasterDto.getOutUserId());
            soLog.setOutUserName(soMasterDto.getOutUserName());
            soLog.setShipCode(soMasterDto.getShipCode());
            soLog.setShipBillCode(soMasterDto.getShipBillCode());
            soLog.setLogCreateTime(now);
            if (soMasterDto.getSendState() == SendState.ALL) {
                soLog.setEndStatus(1);
            }
            soLogService.insertUnique(soLog);
//            soLogs.add(soLog);
        }
//        soLogService.save(soLogs);
    }

}
