package com.deer.wms.ASN.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.file.FileData;
import com.deer.wms.ASN.manage.model.qc.QcRecord;
import com.deer.wms.ASN.manage.model.qc.QcRecordCriteria;
import com.deer.wms.ASN.manage.model.qc.QcRecordDto;
import com.deer.wms.ASN.manage.service.QcRecordService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 质检记录api接口
 * 
 * Created by guo on 2020/01/15.
 */
@RestController
@RequestMapping("/qc/records")
public class QcRecordController {

    @Autowired
    private QcRecordService qcRecordService;

    @PostMapping("/insert")
    @Authority("qc_records_insert")
    public Result add(@RequestBody QcRecord qcRecord) {
        qcRecordService.save(qcRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("qc_records_delete")
    public Result delete(Integer id) {
        qcRecordService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("qc_records_update")
    public Result update(@RequestBody QcRecord qcRecord) {
        qcRecordService.update(qcRecord);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("qc_records_detail")
    public Result detail(@PathVariable Integer id) {
        QcRecord qcRecord = qcRecordService.findById(id);
        return ResultGenerator.genSuccessResult(qcRecord);
    }

    @GetMapping("/list")
    @Authority("qc_records_list")
    public Result list(QcRecordCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<QcRecordDto> list = qcRecordService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 归档质检记录 传billNo
     */
    @PostMapping("/file")
    @Authority("qc_records_file")
    public Result file(@RequestBody FileData fileData, @User CurrentUser currentUser) {

        for (int i = 0; i < fileData.getDetailNos().size(); i++) {
            String detailNo = fileData.getDetailNos().get(i);
            qcRecordService.createFile(detailNo, currentUser);
        }

        return ResultGenerator.genSuccessResult();
    }

}
