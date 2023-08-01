package com.deer.wms.system.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.util.WordUtils;
import com.deer.wms.system.manage.dao.ValidatorMapper;
import com.deer.wms.system.manage.service.ValidatorService;

/**
 * Created by guo on 2018/8/11.
 */
@Service
@Transactional
public class ValidatorServiceImpl implements ValidatorService {

    @Autowired
    private ValidatorMapper validatorMapper;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    private static int next = 100;

    public String getNextIdString(String tableName) {
        next++;
        if (next > 999) {
            next = 100;
        }
        return "" + next + validatorMapper.getNextId(tableName, WordUtils.getdbName(dbUrl));

    }

    public Integer getNextId(String tableName) {
        String nextIdString = this.getNextIdString(tableName);
        return Integer.valueOf(nextIdString);
    }

}
