package com.deer.wms.ASN.manage.web;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.ASN.manage.model.in.AcceptAuditRecord;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecordCriteria;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecordData;
import com.deer.wms.ASN.manage.service.AcceptAuditRecordService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;

/**
 * 验收审批接口
 * @author lixuehe
 * @date 2023/1/11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/accept/audit/records")
public class AcceptAuditRecordController {

    private AcceptAuditRecordService service;


    /**
     * 验收审批记录查询
     * @param criteria
     * @return
     */
    @PostMapping("/list")
    @Authority("accept_audit_records_list")
    public Result findList(@RequestBody AcceptAuditRecordCriteria criteria){
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptAuditRecord> list = service.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 通过审批
     * @param data
     * @return
     */
    @PostMapping("/approved")
    @Authority("accept_audit_records_approved")
    public Result approved(@RequestBody AcceptAuditRecordData data, @User CurrentUser currentUser){

        service.approved(data.getId(),currentUser);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 不通过审批
     * @param data
     * @return
     */
    @PostMapping("/notApproved")
    @Authority("accept_audit_records_notApproved")
    public Result notApproved(@RequestBody AcceptAuditRecordData data,@User CurrentUser currentUser){

        service.notApproved(data.getId(),currentUser);
        return ResultGenerator.genSuccessResult();
    }



}
