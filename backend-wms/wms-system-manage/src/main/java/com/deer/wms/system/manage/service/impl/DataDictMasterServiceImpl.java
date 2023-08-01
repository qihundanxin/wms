package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.dao.DataDictMasterMapper;
import com.deer.wms.system.manage.model.DataDictMaster;
import com.deer.wms.system.manage.service.DataDictMasterService;

/**
 * Created by on 2019/11/25.
 */
@Service
@Transactional
public class DataDictMasterServiceImpl extends AbstractService<DataDictMaster, Integer> implements DataDictMasterService {

    @Autowired
    private DataDictMasterMapper dataDictMasterMapper;

    @Override
    public Result createDataDictMaster(DataDictMaster dataDictMaster) {
        List<DataDictMaster> list = this.findByCode(dataDictMaster.getDataDictCode());
        List<DataDictMaster> list1 = this.findByName(dataDictMaster.getDataDictName());

        if (list.size() == 0 && list1.size() == 0) {
            save(dataDictMaster);
            return ResultGenerator.genSuccessResult();
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.DATA_DICT_EXISTS, "此字典名称已存在");
        } else {
            throw new ServiceException(CommonCode.DATA_DICT_CODE_EXISTS, "此字典编码已存在");

        }
    }

    @Override
    public List<DataDictMaster> findByCode(String dataDictCode) {
        return dataDictMasterMapper.findByCode(dataDictCode);
    }

    @Override
    public List<DataDictMaster> findByName(String dataDictName) {
        return dataDictMasterMapper.findByName(dataDictName);
    }
}
