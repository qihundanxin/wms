package com.deer.wms.ware.task.service.impl;

import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ware.task.dao.SoLogMapper;
import com.deer.wms.ware.task.model.Return.SoLogDto;
import com.deer.wms.ware.task.model.Return.SoLogItemDto;
import com.deer.wms.ware.task.model.SO.SoLogCriteria;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.SoLog;
import com.deer.wms.ware.task.service.SoDetailService;
import com.deer.wms.ware.task.service.SoLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SoLogServiceImpl extends AbstractService<SoLog, Integer> implements SoLogService {

    @Resource
    private SoLogMapper soLogMapper;

    @Resource
    private SoDetailService soDetailService;

    @Override
    public Date getMaxLogDate() {
        return soLogMapper.getMaxLogDate();
    }

    @Override
    public PageInfo findList(SoLogCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        PageInfo<SoLogDto> pageInfo = new PageInfo(soLogMapper.findList(criteria));
        List<SoLogDto> list = pageInfo.getList();
        if (!CollectionUtils.isEmpty(list)) {
            // 查询item
            List<String> billNos = list.stream().map(SoLogDto::getBillNo).collect(Collectors.toList());
            List<SoLogItemDto> logItemsByBillNos = soDetailService.findLogItemsByBillNos(billNos);
            Map<String, List<SoLogItemDto>> collect = logItemsByBillNos.stream().collect(Collectors.groupingBy(SoLogItemDto::getBillNo));
            for (SoLogDto soLogDto : list) {
                soLogDto.setItems(collect.get(soLogDto.getBillNo()));
            }
        }
        return pageInfo;
    }

    @Override
    public void insertUnique(SoLog soLog) {
        soLogMapper.insertUnique(soLog);
    }

}
