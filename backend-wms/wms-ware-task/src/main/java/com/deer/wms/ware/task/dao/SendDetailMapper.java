package com.deer.wms.ware.task.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendDetailParam;
import com.deer.wms.ware.task.model.Send.SendOrderInfo;
import com.deer.wms.ware.task.model.Send.SendOutCriteria;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/2/23
 */
public interface SendDetailMapper  extends Mapper<SendDetail> {

    SendDetail findByBillNoAndSku(String soBillNo,String itemCode);

    int update(SendOutCriteria criteria);

    List<SendDetail> findList(SendDetailParam criteria);

    List<SendOrderInfo> findOrderInfo(SendDetailParam criteria);
}
