package com.deer.wms.project.root.log.work.config;

import java.io.Closeable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.log.work.annotation.WorkLogger;
import com.deer.wms.project.root.log.work.bean.WorkLogBean;
import com.deer.wms.project.root.log.work.bean.WorkUnitLogBean;
import com.deer.wms.project.root.util.AspectUtil;
import com.deer.wms.project.root.util.WorkLogTool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 请求日志监控
 * 
 * @author xuesinuo 2022-01-20
 *
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class WorkLoggerHandler implements WorkLogTool {
    private static final ThreadLocal<WorkLogBean> threadLocalWorkLog = new ThreadLocal<>();
//    private final QueueTool queueTool;

    /**
     * 初始化监控器<br>
     * 
     * 定义销毁方式<br>
     * 
     * 初始化时得到销毁器：1.创建者必须销毁 2.只有创建者有权销毁<br>
     * 
     * @param workLogParam 日志参数
     * @return 监控销毁器
     */
    private Closeable initWorkLog(WorkLogBean workLogParam) {
        // 初始化：开始收集日志
        workLogParam.setBegin(new Date());
        workLogParam.setWorkId(UUID.randomUUID().toString().replace("-", ""));
        workLogParam.setWorkList(new ArrayList<>());
        if (workLogParam.getWorkType() == null) {
            workLogParam.setWorkType(WorkLogBean.WorkType.UNKNOW);
        }
        threadLocalWorkLog.set(workLogParam);
        // 销毁：收集日志结束，开始记录日志
        return () -> {
            threadLocalWorkLog.get().setEnd(new Date());
            try {
                // 持久化
                WorkLogBean worklog = threadLocalWorkLog.get();
                String logValue = JSON.toJSONString(worklog);
                if (!worklog.getSuccess()) {
//                    boolean sendQueue = queueTool.getQueue(QueueKey.WORKLOG_SAVE).offer(worklog);
//                    if (sendQueue) {
//                        log.error("ERROR LOG WORK ID: " + worklog.getWorkId() + " @ " + worklog.getBegin());
//                        log.warn(worklogJson);
//                    } else {
//                        log.error("以下WorkLog未能发送到消息队列：");
//                        log.error(JSON.toJSONString(worklog));
//                    }
                    log.error("TraceId: " + worklog.getTraceId() + "|WorkId: " + worklog.getWorkId() + "请在trace.log中按照ID搜索详细内容。");
                }
                log.trace(logValue);
            } finally {
                threadLocalWorkLog.remove();
            }
        };
    }

    /**
     * 追加记录单元
     * 
     * @param workUnitLog 记录单元
     */
    private void addWorkUnitLog(WorkUnitLogBean workUnitLog) {
        WorkLogBean workLog = threadLocalWorkLog.get();
        if (workLog != null) {
            if (workUnitLog.getArgs() != null) {
                for (int i = 0; i < workUnitLog.getArgs().length; i++) {
                    if (workUnitLog.getArgs()[i] != null && workUnitLog.getArgs()[i].length() > 20000) {
                        workUnitLog.getArgs()[i] = workUnitLog.getArgs()[i].substring(0, 19900) + "...参数过长，不宜记录...";
                    }
                }
            }
            if (workUnitLog.getResult() != null && workUnitLog.getResult().length() > 10000) {
                if (workLog.getResultTooLong()) {
                    workUnitLog.setResult("...结果过长，不宜记录...");
                } else {
                    workLog.setResultTooLong(true);
                    workUnitLog.setResult(workUnitLog.getResult().substring(0, 9900) + "...结果过长，不宜记录...");
                }
            }
            workLog.getWorkList().add(workUnitLog);
        }
    }

    private boolean isSuccess() {
        WorkLogBean workLog = threadLocalWorkLog.get();
        if (workLog == null || workLog.getSuccess() == null) {
            return true;
        }
        return workLog.getSuccess();
    }

    private void unSuccess() {
        WorkLogBean workLog = threadLocalWorkLog.get();
        if (workLog != null) {
            workLog.setSuccess(false);
        }
    }

    /**
     * 非{@link WorkLogger}标注的切面执行<br>
     * 
     * 当检测到标注了{@link WorkLogger}会不记录日志<br>
     * 
     * 方法全称：saveWorkLogAndDoJoinPointWithoutWorkLoggerAnnotation<br>
     * 
     * @param joinPoint    切入点
     * @param workLogParam WorkLog信息：traceId、workType、fromInfo、ip、userId
     * @return 切点执行结果
     * @throws Throwable 日志记录异常
     */
    protected Object saveWlDoJpNoAnn(ProceedingJoinPoint joinPoint, WorkLogBean workLogParam) throws Throwable {
        Object target = joinPoint.getTarget();
        for (Annotation c : target.getClass().getAnnotations()) {
            if (c instanceof WorkLogger) {
                return joinPoint.proceed();
            }
        }
        Method targetMethod = AspectUtil.getMethod(joinPoint);
        for (Annotation m : targetMethod.getAnnotations()) {
            if (m instanceof WorkLogger) {
                return joinPoint.proceed();
            }
        }
        return this.saveWorkLogAndDoJoinPoint(joinPoint, workLogParam);
    }

    /**
     * 记录日志
     * 
     * @param joinPoint    切入点
     * @param workLogParam WorkLog信息：traceId、workType、fromInfo、ip、userId
     * @return 切点执行结果
     * @throws Throwable 日志记录异常
     */
    protected Object saveWorkLogAndDoJoinPoint(ProceedingJoinPoint joinPoint, WorkLogBean workLogParam) throws Throwable {
        // 记录WorkLog + WorkUnitLog
        if (threadLocalWorkLog.get() == null) {
            Closeable recorder = this.initWorkLog(workLogParam != null ? workLogParam : new WorkLogBean());
            try {
                return this.saveWorkUnitLogAndDoJoinPoint(joinPoint);
            } finally {
                recorder.close();
            }
        }
        // 只记录WorkUnitLog
        else {
            return this.saveWorkUnitLogAndDoJoinPoint(joinPoint);
        }
    }

    /**
     * 记录日志单元
     * 
     * @param joinPoint 切入点
     * @return 切点执行结果
     * @throws Throwable 日志记录异常
     */
    private Object saveWorkUnitLogAndDoJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        Method targetMethod = AspectUtil.getMethod(joinPoint);
        // 记录WorkUnitLog
        WorkUnitLogBean workUnitLog = new WorkUnitLogBean();
        workUnitLog.setBegin(new Date());
        workUnitLog.setMethod(target.getClass().getName() + "." + targetMethod.getName());
        String[] args = new String[joinPoint.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            args[i] = Optional.ofNullable(joinPoint.getArgs()[i]).map(arg -> arg.toString()).orElse(null);
        }
        workUnitLog.setArgs(args);
        //
        Object result = null;
        try {
            result = joinPoint.proceed();
            workUnitLog.setResult(Optional.ofNullable(result).map(x -> x.toString()).orElse(null));
            return result;
        } catch (Throwable t) {
            // 抛出的并非表达系统异常
            ServiceException serviceEx;
            if (t instanceof ServiceException && (serviceEx = (ServiceException) t).getCode() != CommonCode.SYSTEM_ERROR) {
                workUnitLog.setResult(Optional.ofNullable(serviceEx).map(x -> x.toString()).orElse(null));
            } else {
                if (this.isSuccess()) {
                    workUnitLog.putThrowable(t);
                }
                this.unSuccess();
            }
            throw t;
        } finally {
            workUnitLog.setEnd(new Date());
            this.addWorkUnitLog(workUnitLog);
        }
    }

    /**
     * 追加备注
     * 
     * @param remark 备注
     */
    @Override
    public void appendRemark(String remark) {
        WorkUnitLogBean workUnitLog = Optional.ofNullable(threadLocalWorkLog.get())
                .map(x -> x.getWorkList())
                .map(x -> x.get(x.size() - 1))
                .orElse(null);
        if (workUnitLog != null) {
            workUnitLog.setRemark((workUnitLog.getRemark() == null ? "" : workUnitLog.getRemark()) + remark + "\n");
        }
    }
}
