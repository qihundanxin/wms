package com.deer.wms.quartz.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.deer.wms.busine.tactic.model.WaveTacticDianshangCriteria;
import com.deer.wms.busine.tactic.model.WaveTacticDianshangDto;
import com.deer.wms.busine.tactic.service.WaveTacticDianshangService;
import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.service.WaveMasterService;

/**
 * 定时任务调度测试
 *
 * @author deer
 */
@Component("waveTask")
public class WaveTask {
    @Autowired
    private WaveMasterService waveMasterService;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private WaveTaskExtension that;

    @WorkLogger
    public void generateWaves() {
        List<WareInfo> wareInfos = wareInfoService.findAll();
        for (WareInfo wareInfo : wareInfos) {
            that.yibugenerateWaves(wareInfo.getWareId());
        }
    }

    @WorkLogger
    public void waveState() {
        waveMasterService.waveState(2);
//        waveMasterService.waveState(3);
//        waveMasterService.waveState(4);
//        waveMasterService.waveState(11);
    }
}

@Component
class WaveTaskExtension {
    @Autowired
    private WaveTacticDianshangService waveTacticDianshangService;
    @Autowired
    private WaveMasterService waveMasterService;

    @Async
    @Synchronized(lockName = LockName.GENERATE_WAVES_TASK, value = "#wareId")
    public void yibugenerateWaves(Integer wareId) {
        WaveTacticDianshangCriteria criteria = new WaveTacticDianshangCriteria();
        criteria.setState(1);
        criteria.setWareId(wareId);
        List<WaveTacticDianshangDto> tactics = waveTacticDianshangService.findList(criteria);
        for (WaveTacticDianshangDto tactic : tactics) {
            try {
                waveMasterService.generateWaves(tactic);
            } catch (Exception e) {
                continue;
            }
        }
    }
}