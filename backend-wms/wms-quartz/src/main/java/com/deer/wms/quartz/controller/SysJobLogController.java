package com.deer.wms.quartz.controller;

import java.util.Date;
import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.quartz.model.SysJobLog;
import com.deer.wms.quartz.model.SysJobLogCriteria;
import com.deer.wms.quartz.service.ISysJobLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 调度日志操作处理
 *
 * @author deer
 */
@Controller
@RequestMapping("/monitor/jobLog")
public class SysJobLogController {
    private String prefix = "monitor/job";

    @Autowired
    private ISysJobLogService jobLogService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;
    /**
     * 任务调度日志list
     */
    @GetMapping("/list")
    @ResponseBody
    @Authority("monitor_jobLog_list")
    public Result list(SysJobLogCriteria jobLog) {
        PageHelper.startPage(jobLog.getPageNum(), jobLog.getPageSize());
        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 批量删除任务调度日志
     */
    @GetMapping("/remove")
    @ResponseBody
    @Authority("monitor_jobLog_remove")
    public Result remove(String ids) {
        jobLogService.deleteJobLogByIds(ids);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail/{jobLogId}")
    @Authority("monitor_jobLog_detail")
    public String detail(@PathVariable("jobLogId") Long jobLogId, ModelMap mmap) {
        mmap.put("name", "jobLog");
        mmap.put("jobLog", jobLogService.selectJobLogById(jobLogId));
        return prefix + "/detail";
    }

    /**
     * 清空任务调度日志
     */
    @PostMapping("/clean")
    @ResponseBody
    @Authority("monitor_jobLog_clean")
    public Result clean() {
        jobLogService.cleanJobLog();
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 任务调度日志导出
     */
    @GetMapping("/sysJobLogExport")
    @Authority("monitor_jobLog_sysJobLogExport")
    public Result sysJobLogExport(SysJobLog jobLog) {

        List<SysJobLog> list = jobLogService.selectJobLogList(jobLog);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/sysJobLog" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/sysJobLogModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();

            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                SysJobLog sysJobLog = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String jobName = "-";
                if (sysJobLog.getJobName() != null) {
                    jobName = sysJobLog.getJobName();
                }
                String jobGroup = "-";
                if (sysJobLog.getJobGroup() != null) {
                    jobGroup = sysJobLog.getJobGroup();
                }
                String methodName = "-";
                if (sysJobLog.getMethodName() != null) {
                    methodName = sysJobLog.getMethodName();
                }
                String methodParams = "-";
                if (sysJobLog.getMethodParams() != null) {
                    methodParams = sysJobLog.getMethodParams();
                }
                String jobMessage = "-";
                if (sysJobLog.getJobMessage() != null) {
                    jobMessage = sysJobLog.getJobMessage();
                }

                String status = "-";
                if (sysJobLog.getStatus() != null) {
                    status = sysJobLog.getStatus();
                }
                String exceptionInfo = "-";
                if (sysJobLog.getExceptionInfo() != null) {
                    exceptionInfo = sysJobLog.getExceptionInfo();
                }
                String createTime = "-";
                if (sysJobLog.getCreateTime() != null) {
                    createTime = sysJobLog.getCreateTime().toString();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label jobNameLabel = new Label(1, i + 2, jobName);
                Label jobGroupLabel = new Label(2, i + 2, jobGroup);
                Label methodNameLabel = new Label(3, i + 2, methodName);
                Label methodParamsLabel = new Label(4, i + 2, methodParams);
                Label jobMessageLabel = new Label(5, i + 2, jobMessage);
                Label statusLabel = new Label(6, i + 2, status);
                Label exceptionInfoLabel = new Label(7, i + 2, exceptionInfo);
                Label createTimeLabel = new Label(8, i + 2, createTime);

                indexLabel.setCellFormat(format);
                jobNameLabel.setCellFormat(format);
                jobGroupLabel.setCellFormat(format);
                methodNameLabel.setCellFormat(format);
                methodParamsLabel.setCellFormat(format);
                jobMessageLabel.setCellFormat(format);
                statusLabel.setCellFormat(format);
                exceptionInfoLabel.setCellFormat(format);
                createTimeLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(jobNameLabel);
                wSheet.addCell(jobGroupLabel);
                wSheet.addCell(methodNameLabel);
                wSheet.addCell(methodParamsLabel);
                wSheet.addCell(jobMessageLabel);
                wSheet.addCell(statusLabel);
                wSheet.addCell(exceptionInfoLabel);
                wSheet.addCell(createTimeLabel);
            }

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SYS_JOB_LOG, "sysJobLog.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }
}
