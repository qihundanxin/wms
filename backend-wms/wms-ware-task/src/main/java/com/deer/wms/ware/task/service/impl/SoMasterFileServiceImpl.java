package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.SoMasterFileMapper;
import com.deer.wms.ware.task.model.SO.SoMasterFile;
import com.deer.wms.ware.task.model.SO.SoMasterFileCriteria;
import com.deer.wms.ware.task.service.SoMasterFileService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ll on 2020/02/03.
 */
@Service
@Transactional
public class SoMasterFileServiceImpl extends AbstractService<SoMasterFile, Integer> implements SoMasterFileService {

    @Autowired
    private SoMasterFileMapper soMasterFileMapper;

    @Override
    public List<SoMasterFile> findList(SoMasterFileCriteria criteria) {
        return soMasterFileMapper.findList(criteria);
    }
}
