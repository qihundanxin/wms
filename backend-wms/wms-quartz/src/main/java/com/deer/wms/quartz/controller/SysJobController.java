package com.deer.wms.quartz.controller;

import java.util.Date;
import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.file.configurer.FileSetting;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.quartz.exception.TaskException;
import com.deer.wms.quartz.model.SysJob;
import com.deer.wms.quartz.model.SysJobCriteria;
import com.deer.wms.quartz.model.SysJobData;
import com.deer.wms.quartz.service.ISysJobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 调度任务信息操作处理
 *
 * @author deer
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController {

    @Autowired
    private ISysJobService jobService;

    @Autowired
    private FileSetting fileSetting;

    @Autowired
    private FileS3Tool fileS3Tool;
    /**
     * 任务调度list
     */
    @ResponseBody
    @GetMapping("/list")
    @Authority("monitor_job_list")
    public Result list(SysJobCriteria job) {
        PageHelper.startPage(job.getPageNum(), job.getPageSize());
        List<SysJob> list = jobService.selectJobList(job);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 批量删除任务调度
     */
    @GetMapping("/remove")
    @Authority("monitor_job_remove")
    public Result remove(SysJobData sysJobData) throws SchedulerException {
        jobService.deleteJobByIds(sysJobData);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 任务调度状态修改
     */
    @PostMapping("/changeStatus")
    @Authority("monitor_job_changeStatus")
    public Result changeStatus(@RequestBody SysJob job) throws SchedulerException {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        jobService.changeStatus(newJob);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 任务调度立即执行一次
     */
    @PostMapping("/run")
    @Authority("monitor_job_run")
    public Result run(@RequestBody SysJob job) {
        Long jobId = job.getJobId();
        SysJob sysJob = jobService.selectJobById(jobId);
        try {
            jobService.run(sysJob);
        } catch (Exception ec) {
            System.out.println(ec.getStackTrace());
        }

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 新增保存调度
     */
    @PostMapping("/add")
    @Authority("monitor_job_add")
    public Result addSave(@RequestBody SysJob job, @User CurrentUser currentUser) throws SchedulerException, TaskException {
        job.setCreateBy(currentUser.getUserName());
        jobService.insertJobCron(job);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 修改保存调度
     */
    @PostMapping("/edit")
    @Authority("monitor_job_edit")
    public Result editSave(@RequestBody SysJob job) throws SchedulerException, TaskException {
        jobService.updateJobCron(job);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/updateCron")
    @Authority("monitor_job_updateCron")
    public Result updateCron(@RequestBody SysJob job) throws SchedulerException, TaskException {
        SysJob sysJob = jobService.selectJobById(job.getJobId());
        sysJob.setCronExpression(job.getCronExpression());
        jobService.updateJobCron(sysJob);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 校验cron表达式是否有效
     */
    @PostMapping("/checkCronExpressionIsValid")
    @Authority("monitor_job_checkCronExpressionIsValid")
    public boolean checkCronExpressionIsValid(SysJob job) {
        return jobService.checkCronExpressionIsValid(job.getCronExpression());
    }

    /**
     * 任务调度单导出
     */
    @GetMapping("/sysJobExport")
    @Authority("monitor_job_sysJobExport")
    public Result sysJobExport(SysJob job) {

        List<SysJob> list = jobService.selectJobList(job);

        String staticPath = fileSetting.getStaticFileDir();
        String downloadPath = fileSetting.getDownloadPath() + "/sysJob" + DateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") + ".xls";
        String modelPath = fileSetting.getModelPath() + "/sysJobModel.xls";
        try {
            String modelFilePath = staticPath + modelPath;
            String filePath = staticPath + downloadPath;

            jxl.Workbook workbook = jxl.Workbook.getWorkbook(new java.io.File(modelFilePath));
            WritableWorkbook copy = jxl.Workbook.createWorkbook(new java.io.File(filePath), workbook);

            WritableSheet wSheet = copy.getSheet(0);
            CellFormat format = wSheet.getCell(1, 1).getCellFormat();

            for (int i = 0; i < list.size(); i++) {

                wSheet.insertRow(i + 2);

                SysJob sysJob = list.get(i);

                String indexStr = Integer.toString(i + 1);
                String jobName = "-";
                if (sysJob.getJobName() != null) {
                    jobName = sysJob.getJobName();
                }
                String jobGroup = "-";
                if (sysJob.getJobGroup() != null) {
                    jobGroup = sysJob.getJobGroup();
                }
                String methodName = "-";
                if (sysJob.getMethodName() != null) {
                    methodName = sysJob.getMethodName();
                }
                String methodParams = "-";
                if (sysJob.getMethodParams() != null) {
                    methodParams = sysJob.getMethodParams();
                }
                String cronExpression = "-";
                if (sysJob.getCronExpression() != null) {
                    cronExpression = sysJob.getCronExpression();
                }

                String misfirePolicy = "-";
                if (sysJob.getMisfirePolicy() != null) {
                    misfirePolicy = sysJob.getMisfirePolicy();
                }
                String status = "-";
                if (sysJob.getStatus() != null) {
                    status = sysJob.getStatus();
                }
                String createBy = "-";
                if (sysJob.getCreateBy() != null) {
                    createBy = sysJob.getCreateBy();
                }
                String createTime = "-";
                if (sysJob.getCreateTime() != null) {
                    createTime = sysJob.getCreateTime().toString();
                }
                String updateBy = "-";
                if (sysJob.getUpdateBy() != null) {
                    updateBy = sysJob.getUpdateBy();
                }
                String updateTime = "-";
                if (sysJob.getUpdateTime() != null) {
                    updateTime = sysJob.getUpdateTime().toString();
                }
                String remark = "-";
                if (sysJob.getRemark() != null) {
                    remark = sysJob.getRemark();
                }

                Label indexLabel = new Label(0, i + 2, indexStr);
                Label jobNameLabel = new Label(1, i + 2, jobName);
                Label jobGroupLabel = new Label(2, i + 2, jobGroup);
                Label methodNameLabel = new Label(3, i + 2, methodName);
                Label methodParamsLabel = new Label(4, i + 2, methodParams);
                Label cronExpressionLabel = new Label(5, i + 2, cronExpression);
                Label misfirePolicyLabel = new Label(6, i + 2, misfirePolicy);
                Label statusLabel = new Label(7, i + 2, status);
                Label createByLabel = new Label(8, i + 2, createBy);
                Label createTimeLabel = new Label(9, i + 2, createTime);
                Label updateByLabel = new Label(10, i + 2, updateBy);
                Label updateTimeLabel = new Label(11, i + 2, updateTime);
                Label remarkLabel = new Label(12, i + 2, remark);

                indexLabel.setCellFormat(format);
                jobNameLabel.setCellFormat(format);
                jobGroupLabel.setCellFormat(format);
                methodNameLabel.setCellFormat(format);
                methodParamsLabel.setCellFormat(format);
                cronExpressionLabel.setCellFormat(format);
                misfirePolicyLabel.setCellFormat(format);
                statusLabel.setCellFormat(format);
                createByLabel.setCellFormat(format);
                createTimeLabel.setCellFormat(format);
                updateByLabel.setCellFormat(format);
                updateTimeLabel.setCellFormat(format);
                remarkLabel.setCellFormat(format);

                wSheet.addCell(indexLabel);
                wSheet.addCell(jobNameLabel);
                wSheet.addCell(jobGroupLabel);
                wSheet.addCell(methodNameLabel);
                wSheet.addCell(methodParamsLabel);
                wSheet.addCell(cronExpressionLabel);
                wSheet.addCell(misfirePolicyLabel);
                wSheet.addCell(statusLabel);
                wSheet.addCell(createByLabel);
                wSheet.addCell(createTimeLabel);
                wSheet.addCell(updateByLabel);
                wSheet.addCell(updateTimeLabel);
                wSheet.addCell(remarkLabel);

            }

            copy.write();
            copy.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        }
        String fullPath = fileS3Tool.save(FileUseType.EXPORT_SYS_JOB, "sysJob.xls",
                new java.io.File(staticPath + downloadPath));
        return ResultGenerator.genSuccessResult(fileS3Tool.getUrl(fullPath));
    }
}
