package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.PageSetMapper;
import com.deer.wms.base.system.model.PageSet.PageSet;
import com.deer.wms.base.system.model.PageSet.PageSetCriteria;
import com.deer.wms.base.system.model.PageSet.PageSetDto;
import com.deer.wms.base.system.service.PageSetService;

import com.deer.wms.project.root.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/27.
 */
@Service
@Transactional
public class PageSetServiceImpl extends AbstractService<PageSet, Integer> implements PageSetService {

    @Autowired
    private PageSetMapper pageSetMapper;

    @Override
    public List<PageSetDto> findList(PageSetCriteria criteria) {
        return pageSetMapper.findList(criteria);
    }

}
