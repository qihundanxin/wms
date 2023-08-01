package com.deer.wms.inventory.service;

import com.deer.wms.inventory.model.SerialNo;
import com.deer.wms.inventory.model.SerialNoCriteria;
import com.deer.wms.inventory.model.SerialNoDto;
import com.deer.wms.project.root.core.service.Service;

import java.util.List;

/**
 * Created by ll on 2020/02/15.
 */
public interface SerialNoService extends Service<SerialNo, Integer> {
    /**
     * 写入序列号
     *
     * @param serialNo
     */
    void serialNoRecored(List<String> serialNo, Integer createUserId, String createUserName, Integer acceptRecordId, Integer inventoryId, Integer type);
//    void serialCreate(List<String> serialNo,Integer createUserId,String createUserName,Integer acceptRecordId,Integer inventoryId,Integer type);

    /**
     * 根据条件查询
     */
    List<SerialNoDto> findList(SerialNoCriteria criteria);

    List<SerialNoDto> findInventoryList(SerialNoCriteria criteria);
}
