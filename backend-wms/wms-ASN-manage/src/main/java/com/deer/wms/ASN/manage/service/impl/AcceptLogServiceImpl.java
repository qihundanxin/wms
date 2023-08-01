package com.deer.wms.ASN.manage.service.impl;

import com.deer.wms.ASN.manage.dao.AcceptLogMapper;
import com.deer.wms.ASN.manage.model.AcceptLog;
import com.deer.wms.ASN.manage.model.in.AcceptLogCriteria;
import com.deer.wms.ASN.manage.service.AcceptLogService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AcceptLogServiceImpl extends AbstractService<AcceptLog, Integer> implements AcceptLogService {

    @Resource
    private AcceptLogMapper acceptLogMapper;

    @Override
    public Date getMaxLogDate() {
        return acceptLogMapper.getLogMaxDate();
    }

    @Override
    public PageInfo findList(AcceptLogCriteria acceptLogCriteria) {
        PageHelper.startPage(acceptLogCriteria.getPageNum(), acceptLogCriteria.getPageSize());
        List<AcceptLog> list = acceptLogMapper.findList(acceptLogCriteria);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public void insertUnique(AcceptLog acceptLog) {
        acceptLogMapper.insertUnique(acceptLog);
    }

}
