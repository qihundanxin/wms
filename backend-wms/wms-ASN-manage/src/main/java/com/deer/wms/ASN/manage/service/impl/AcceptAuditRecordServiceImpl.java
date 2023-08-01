package com.deer.wms.ASN.manage.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.ASN.manage.dao.AcceptAuditRecordMapper;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecord;
import com.deer.wms.ASN.manage.model.in.AcceptAuditRecordCriteria;
import com.deer.wms.ASN.manage.model.in.AcceptInsert;
import com.deer.wms.ASN.manage.service.AcceptAuditRecordService;
import com.deer.wms.ASN.manage.service.AcceptRecordService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.SqlUtil;

import lombok.AllArgsConstructor;

/**
 * @author lixuehe
 * @date 2023/1/11
 */
@Service
@AllArgsConstructor
public class AcceptAuditRecordServiceImpl extends AbstractService<AcceptAuditRecord, Integer>  implements AcceptAuditRecordService {

    private AcceptAuditRecordMapper mapper;

    private AcceptRecordService acceptRecordService;

    /**
     * 查询验收审批列表
     * @param criteria
     * @return
     */
    @Override
    public List<AcceptAuditRecord> findList(AcceptAuditRecordCriteria criteria) {
        criteria.setItemCodeLike(SqlUtil.likeLR(criteria.getItemCode()));
        criteria.setItemNameLike(SqlUtil.likeLR(criteria.getItemName()));
        criteria.setCellCodeLike(SqlUtil.likeLR(criteria.getCellCode()));
        criteria.setAcceptUserNameLike(SqlUtil.likeLR(criteria.getAcceptUserName()));
        return mapper.findList(criteria);
    }

    @Override
    public int updateById(AcceptAuditRecord record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @Transactional
    public Boolean approved(Integer id, CurrentUser currentUser) {
        //已经审核的不可在审核
        AcceptAuditRecord record1 = findById(id);
        if(record1.getAuditState()!=0){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "订单已审核！");
        }
        AcceptAuditRecord record = new AcceptAuditRecord();
        record.setAcceptAuditRecordId(id);
        record.setAuditState(1);
        record.setAuditUserId(currentUser.getUserId());
        record.setAuditUserName(currentUser.getUserName());
        record.setAuditTime(new Date());
        int result = mapper.updateByPrimaryKeySelective(record);
        if(result>0){
            AcceptInsert acceptInsert = new AcceptInsert();
            BeanUtils.copyProperties(record1,acceptInsert);
            acceptInsert.setAcceptTime(DateUtils.dateToStr(record1.getAcceptTime(),"yyyy-MM-dd HH:mm:ss"));
            acceptInsert.setToCellCode(record1.getCellCode());
            acceptInsert.setToBoxCode(record1.getBoxCode());
            acceptInsert.setToPackDetailId(record1.getPackDetailId());
            acceptInsert.setToPackDescribe(record1.getPackDescribe());
            acceptInsert.setToTransRatio(record1.getTransRatio());
            acceptInsert.setToQuantity(record1.getQuantity());
            acceptRecordService.accept(acceptInsert);
        }
        return true;
    }

    @Override
    public Boolean notApproved(Integer id, CurrentUser currentUser) {
        //已经审核的不可在审核
        AcceptAuditRecord record1 = findById(id);
        if(record1.getAuditState()!=0){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "订单已审核");
        }
        AcceptAuditRecord record = new AcceptAuditRecord();
        record.setAcceptAuditRecordId(id);
        record.setAuditState(-1);
        record.setAuditUserId(currentUser.getUserId());
        record.setAuditUserName(currentUser.getUserName());
        record.setAuditTime(new Date());
        mapper.updateByPrimaryKeySelective(record);
        return true;
    }




}
