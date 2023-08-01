package com.deer.wms.ware.task.service.impl;

import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.ware.task.dao.SendDetailMapper;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendDetailParam;
import com.deer.wms.ware.task.model.Send.SendOrderInfo;
import com.deer.wms.ware.task.service.SendDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/2/23
 */
@Service
public class SendDetailServiceImpl extends AbstractService<SendDetail, Integer> implements SendDetailService {


    @Autowired
    private SendDetailMapper mapper;

    @Autowired
    private ItemInfoService itemInfoService;

    /**
     * 根据出库单号与SKU 查询发运记录
     * @param soBillNo
     * @param itemCode
     * @return
     */
    @Override
    public SendDetail findByBillNoAndSku(String soBillNo, String itemCode) {
        return mapper.findByBillNoAndSku(soBillNo,itemCode);
    }

    @Override
    public List<SendDetail> findList(SendDetailParam criteria) {
        List<SendDetail> list =  mapper.findList(criteria);
        list.forEach(detail ->
                detail.setItemName(itemInfoService.getItemNameI18n(detail.getItemName())));
        return list;
    }

    @Override
    public List<SendOrderInfo> findOrderInfo(SendDetailParam criteria) {
        return mapper.findOrderInfo(criteria);
    }

}
