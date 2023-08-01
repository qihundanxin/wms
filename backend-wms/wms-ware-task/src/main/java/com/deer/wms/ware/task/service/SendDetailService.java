package com.deer.wms.ware.task.service;

import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.ware.task.model.SO.SoMasterDto;
import com.deer.wms.ware.task.model.Send.SendDetail;
import com.deer.wms.ware.task.model.Send.SendDetailParam;
import com.deer.wms.ware.task.model.Send.SendOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lixuehe
 * @date 2023/2/23
 */
public interface SendDetailService  extends Service<SendDetail, Integer> {

    SendDetail findByBillNoAndSku(@Param("soBillNo") String soBillNo, @Param("itemCode") String itemCode);

    List<SendDetail> findList(SendDetailParam criteria);

    /**
     * 查询发运记录 订单维度信息
     * @param criteria
     * @return
     */
    List<SendOrderInfo> findOrderInfo(SendDetailParam criteria);
}
