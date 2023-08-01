package com.deer.wms.pdaApi.service.impl;

import org.springframework.stereotype.Service;

import com.deer.wms.pdaApi.dao.PdaDemoMapper;
import com.deer.wms.pdaApi.model.PdaDemoEntity;
import com.deer.wms.pdaApi.service.PdaDemoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdaDemoServiceImpl implements PdaDemoService {

    private final PdaDemoMapper dao;
    @Override
    public PdaDemoEntity add(PdaDemoEntity entity) {
        dao.insert(entity);
        return entity;
    }

}
