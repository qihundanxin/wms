package com.deer.wms.inventory.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.inventory.dao.SerialNoMapper;
import com.deer.wms.inventory.model.SerialNo;
import com.deer.wms.inventory.model.SerialNoCriteria;
import com.deer.wms.inventory.model.SerialNoDto;
import com.deer.wms.inventory.service.SerialNoService;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.DateUtils;

/**
 * Created by ll on 2020/02/15.
 */
@Service
@Transactional
public class SerialNoServiceImpl extends AbstractService<SerialNo, Integer> implements SerialNoService {

    @Autowired
    private SerialNoMapper serialNoMapper;

    @Override
    public void serialNoRecored(@Param("serialNo") List<String> serialNos, @Param("createUserId") Integer createUserId,
                                @Param("createUserName") String createUserName, @Param("acceptRecordId") Integer acceptRecordId,
                                @Param("inventoryId") Integer inventoryId, @Param("type") Integer type) {
        for (int i = 0; i < serialNos.size(); i++) {
            SerialNo serialNo = new SerialNo();
            serialNo.setSerialNo(serialNos.get(i));
            serialNo.setType(type);
            serialNo.setTaskId(acceptRecordId);
            serialNo.setInventoryId(inventoryId);
            serialNo.setCreateTime(DateUtils.getNowDateTimeString());
            serialNo.setCreateUserId(createUserId);
            serialNo.setCreateUserName(createUserName);
            save(serialNo);
        }
    }
    /*@Override
    public void serialCreate(@Param("serialNo") List<String> serialNos,@Param("createUserId") Integer createUserId,
                                @Param("createUserName") String createUserName,@Param("acceptRecordId") Integer acceptRecordId,
                                @Param("inventoryId") Integer inventoryId,@Param("type") Integer type){
        for(int i=0;i<serialNos.size();i++) {
            SerialNo serialNo = new SerialNo(serialNos.get(i),type,acceptRecordId, inventoryId,DateUtils.getNowDateTimeString(),
                    createUserId,createUserName);
            save(serialNo);
        }
    }*/

    @Override
    public List<SerialNoDto> findList(SerialNoCriteria criteria) {
        return serialNoMapper.findList(criteria);
    }

    @Override
    public List<SerialNoDto> findInventoryList(SerialNoCriteria criteria) {
        return serialNoMapper.findInventoryList(criteria);
    }

}
