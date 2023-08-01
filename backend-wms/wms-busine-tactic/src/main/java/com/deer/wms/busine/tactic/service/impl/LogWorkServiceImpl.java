package com.deer.wms.busine.tactic.service.impl;

import org.springframework.stereotype.Service;

import com.deer.wms.busine.tactic.dao.LogWorkMapper;
import com.deer.wms.busine.tactic.model.logwork.LogWork;
import com.deer.wms.busine.tactic.service.LogWorkService;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LogWorkServiceImpl implements LogWorkService {
    private final LogWorkMapper mapper;

    @WorkLogger(false)
    @Override
    public LogWork post(LogWork entity) {
        mapper.insert(entity);
        return entity;
    }
}

///**
// * 监听消息队列，向数据库记录LogWork
// * 
// * @author xuesinuo
// *
// */
//@WorkLogger(false)
//@AllArgsConstructor
//@Service
//class LogWorkSaveTask {
//    private static Boolean saving = false;
//
//    private final QueueTool queueTool;
//    private final LogWorkService service;
//    private final LogWorkUnitService uservice;
//
//    @Async
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void saveLogWork() throws InterruptedException {
//        synchronized (saving) {
//            if (saving) {
//                return;
//            }
//            saving = true;
//        }
//        try {
//            while (saving) {
//                WorkLogBean logWorkBean = (WorkLogBean) queueTool.getBlockingQueue(QueueKey.WORKLOG_SAVE).take();
//                LogWork logWork = new LogWork();
//                Np.i(logWorkBean)
//                        .doo(x -> logWork.setBegin(x.getBegin()))
//                        .doo(x -> logWork.setEnd(x.getEnd()))
//                        .doo(x -> logWork.setFromInfo(JSON.toJSONString(x.getFromInfo())))
//                        .doo(x -> logWork.setIp(x.getIp()))
//                        .doo(x -> logWork.setSuccess(x.getSuccess()))
//                        .doo(x -> logWork.setTime(x.getTime()))
//                        .doo(x -> logWork.setTraceId(x.getTraceId()))
//                        .doo(x -> logWork.setUserId(x.getUserId()))
//                        .doo(x -> logWork.setWorkId(x.getWorkId()))
//                        .doo(x -> logWork.setWorkType(x.getWorkType()))
//                        .o();
//                LogWork logWorkRe = service.post(logWork);
//                boolean isFirstUnit = true;
//                for (WorkUnitLogBean workUnitLogBean : Np.i(logWorkBean).x(x -> x.getWorkList()).o(new ArrayList<>())) {
//                    LogWorkUnit workUnitLog = new LogWorkUnit();
//                    Np.i(workUnitLogBean)
//                            .doo(x -> x.setArgs(Np.i(workUnitLogBean).x(y -> y.getArgs()).ifNull(new String[0]).o()))
//                            .doo(x -> workUnitLog.setArgs(JSON.toJSONString(x.getArgs())))
//                            .doo(x -> workUnitLog.setBegin(x.getBegin()))
//                            .doo(x -> workUnitLog.setEnd(x.getEnd()))
//                            .doo(x -> workUnitLog.setMethod(x.getMethod()))
//                            .doo(x -> workUnitLog.setRemark(x.getRemark()))
//                            .doo(x -> workUnitLog.setResult(x.getResult()))
//                            .doo(x -> workUnitLog.setThrowable(x.getThrowable()))
//                            .doo(x -> workUnitLog.setTime(x.getTime()))
//                            .doo(x -> workUnitLog.setLogWorkId(logWorkRe.getId()))
//                            .o();
//                    if (isFirstUnit || StrUtil.isNotBlank(workUnitLog.getThrowable()) || workUnitLog.getTime() > 1000L) {
//                        uservice.post(workUnitLog);
//                    }
//                    isFirstUnit = false;
//                }
//            }
//        } finally {
//            saving = false;
//        }
//    }
//}
