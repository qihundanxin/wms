package com.deer.wms.quartz.task;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.deer.wms.project.root.constant.LockName;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.sync.annotation.Synchronized;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.ware.task.dao.PickTaskMapper;
import com.deer.wms.ware.task.model.SO.SoDetailDto;
import com.deer.wms.ware.task.service.PickTaskService;
import com.deer.wms.ware.task.service.SoDetailService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务调度测试
 *
 * @author deer
 */
@Component("ryTask")
@Slf4j
public class RyTask {

    @Autowired
    private PickTaskService pickTaskService;

    @Autowired
    private SoDetailService soDetailService;

    @Autowired
    private PickTaskMapper pickTaskMapper;

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private RyTaskExtension that;

    /**
     * 定时任务 -- 拣货任务合并
     */
    @WorkLogger
    public void autoCreatePickTaskV2() {
        List<WareInfo> wareInfos = wareInfoService.findAll();
        for (WareInfo wareInfo : wareInfos) {
            Integer wareId = wareInfo.getWareId();
            that.createPickTaskByWareId(wareId);
        }
    }

    @WorkLogger
    public void autoCreatePickTask() {
        List<WareInfo> wareInfos = wareInfoService.findAllByStatus();
        for (WareInfo wareInfo : wareInfos) {
            that.yibuPickTask(wareInfo.getWareId());
        }
    }

    /**
     * 这里执行从未被执行过的
     */
    @WorkLogger
    public void autoCreatePickTask2() {
        List<WareInfo> wareInfos = wareInfoService.findAllByStatus();
        for (WareInfo wareInfo : wareInfos) {
            that.yibuPickTask2(wareInfo.getWareId());
        }
    }

    /**
     * 这里执行订单数量增加的
     */
    @WorkLogger
    @Deprecated
    // FIXME 适时销毁
    public void autoCreatePickTask3() {
        List<WareInfo> wareInfos = wareInfoService.findAllByStatus();
        for (WareInfo wareInfo : wareInfos) {
            that.autoCreatePickTask3(wareInfo.getWareId());
        }
    }
}

@Component
@Slf4j
class RyTaskExtension {

    @Autowired
    private PickTaskService pickTaskService;
    @Autowired
    private SoDetailService soDetailService;
    @Autowired
    private CacheTool cache;

    @Async
    @Synchronized(lockName = LockName.RY_TASK, value = "#wareId")
    public void createPickTaskByWareId(Integer wareId) {
        pickTaskService.createBatchExceptionPickTask();
        List<SoDetailDto> soDetailDtoList = soDetailService.findForUpdatePickTask(wareId, LocalDateTime.now().minusMonths(1));
        log.info(wareId + "分配订单的拣货任务，本次查询到待分配：" + soDetailDtoList.size());
        for (SoDetailDto soDetailDto : soDetailDtoList) {
            Boolean cached = cache.getObject("DoNotCreateTackAt10Minus", soDetailDto.getSoDetailId().toString());
            if (cached != null && cached) {
                break;
            } else {
                cache.setObject("DoNotCreateTackAt10Minus", soDetailDto.getSoDetailId().toString(), true, 10 * 60);
            }
            try {
                Double orderQuantity = soDetailDto.getOrderQuantity();
                Double allottedQuantity = soDetailDto.getAllottedQuantity();
                if (orderQuantity == 0 && allottedQuantity == 0) {
                    // 退货
                    soDetailService.cancelSoDetail(soDetailDto);
                } else if (orderQuantity > allottedQuantity) {
                    pickTaskService.createPickTask(soDetailDto);
                } else if (orderQuantity < allottedQuantity) {
                    // 数量减少
                    soDetailService.reduceSkuQuantity(soDetailDto);
                }
            } catch (ServiceException ex) {
                if (ex.getCode() != null && ex.getCode() == CommonCode.SYSTEM_ERROR) {
                    log.error(wareId + "拣货任务更新失败，so单号：{}", soDetailDto.getBillNo(), ex);
                } else {
                    log.info("service exception : {}", ex.getMsg());
                }
            } catch (Exception e) {
                log.error(wareId + "拣货任务更新失败，so单号：{}", soDetailDto.getBillNo(), e);
            }
        }
    }

    /**
     * 这里执行缺货状态的
     */
    @Async
    @Synchronized(lockName = LockName.RY_TASK, value = "#wareId")
    public void yibuPickTask(Integer wareId) {
        List<SoDetailDto> soDetailDtos = soDetailService.findUndistributedSoDetail(wareId);
        for (SoDetailDto soDetailDto : soDetailDtos) {
            try {
                pickTaskService.createPickTask(soDetailDto);
            } catch (Exception e) {
                log.error(wareId + "缺货出货单创建拣货任务失败，so单号：{}", soDetailDto.getBillNo(), e);
                continue;
            }
        }
        pickTaskService.mergeTask();
    }

    /**
     * 这里执行从未被执行过的
     */
    @Async
    @Synchronized(lockName = LockName.RY_TASK, value = "#wareId")
    public void yibuPickTask2(Integer wareId) {
        List<SoDetailDto> soDetailDtos = soDetailService.findUndistributedSoDetail2(wareId);
        for (SoDetailDto soDetailDto : soDetailDtos) {
            try {
                pickTaskService.createPickTask(soDetailDto);
            } catch (Exception e) {
                log.error("新出货单创建拣货任务失败，so单号：{}", soDetailDto.getBillNo(), e);
                continue;
            }
        }
        pickTaskService.mergeTask();
    }

    /**
     * 这里执行订单数量增加的
     */
    @Async
    @Synchronized(lockName = LockName.RY_TASK, value = "#wareId")
    public void autoCreatePickTask3(Integer wareId) {
        List<SoDetailDto> soDetailDtos = soDetailService.findUndistributedSoDetail3(wareId);
        for (SoDetailDto soDetailDto : soDetailDtos) {
            try {
                pickTaskService.createPickTask3(soDetailDto);
            } catch (Exception e) {
                log.error("autoCreatePickTask3（） 出货单（数量增加）创建拣货任务失败，so单号：{}", soDetailDto.getBillNo(), e);
                continue;
            }
        }
        List<SoDetailDto> soDetailDtos4 = soDetailService.findUndistributedSoDetail4(wareId);
        for (SoDetailDto soDetailDto : soDetailDtos4) {
            try {
                soDetailService.reduceSkuQuantity(soDetailDto);
            } catch (Exception e) {
                log.error("autoCreatePickTask3（） 出货单（数量减少）创建拣货任务失败，so单号：{}", soDetailDto.getBillNo(), e);
                continue;
            }
        }
        // berwin process q = 0 and aq = 0
        List<SoDetailDto> soDetailDtos5 = soDetailService.findUndistributedSoDetail5(wareId);
        for (SoDetailDto soDetailDto : soDetailDtos5) {
            try {
                soDetailService.reduceSkuQuantityFor0And0(soDetailDto);
            } catch (Exception e) {
                log.error("autoCreatePickTask3（） 出货单（数量为0）更新拣货任务失败，so单号：{}", soDetailDto.getBillNo(), e);
                continue;
            }
        }
        pickTaskService.mergeTask();
    }
}