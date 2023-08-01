package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.PrinterInfoMapper;
import com.deer.wms.base.system.model.PrinterInfo;
import com.deer.wms.base.system.model.PrinterInfoCriteria;
import com.deer.wms.base.system.service.PrinterInfoService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  on 2021/08/25.
 */
@Service
@Transactional
public class PrinterInfoServiceImpl extends AbstractService<PrinterInfo, Integer> implements PrinterInfoService {

    @Autowired
    private PrinterInfoMapper printerInfoMapper;

    @Override
    public List<PrinterInfo> findList(PrinterInfoCriteria criteria) {
        return printerInfoMapper.findList(criteria);
    }

    @Override
    public PrinterInfo findPdaDefault(Integer wareId) {
        return printerInfoMapper.findPdaDefault(wareId);
    }

    @Override
    public void changePda(PrinterInfo printerInfo) {
        String pda = printerInfo.getPdaDefault();
        String pda2=null;
        Integer id = printerInfo.getId();
        Integer wareId = printerInfo.getWareId();
        if(pda.endsWith("1")){
            pda2="0";
        }else if(pda.endsWith("0")){
            pda2="1";
        }
        printerInfoMapper.changePda(id,pda,wareId,pda2);
    }
}
