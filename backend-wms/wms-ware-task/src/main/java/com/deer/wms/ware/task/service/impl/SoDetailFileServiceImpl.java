package com.deer.wms.ware.task.service.impl;

import com.deer.wms.ware.task.dao.SoDetailFileMapper;
import com.deer.wms.ware.task.model.SO.SoDetailFile;
import com.deer.wms.ware.task.model.SO.SoDetailFileCriteria;
import com.deer.wms.ware.task.service.SoDetailFileService;

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
public class SoDetailFileServiceImpl extends AbstractService<SoDetailFile, Integer> implements SoDetailFileService {

    @Autowired
    private SoDetailFileMapper soDetailFileMapper;

    @Override
    public List<SoDetailFile> findList(SoDetailFileCriteria criteria) {
        return soDetailFileMapper.findList(criteria);
    }
}
