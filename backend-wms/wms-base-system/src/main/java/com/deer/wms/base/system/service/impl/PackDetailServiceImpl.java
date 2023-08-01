package com.deer.wms.base.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.dao.PackDetailMapper;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Pack.PackDetail2;
import com.deer.wms.base.system.model.Pack.PackDetailCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.CacheTool;

/**
 * Created by guo on 2019/11/18.
 */
@Service
@Transactional
public class PackDetailServiceImpl extends AbstractService<PackDetail, Integer> implements PackDetailService {

    @Autowired
    private PackDetailMapper packDetailMapper;

    @Autowired
    private CacheTool cache;

    @Override
    public int update(PackDetail packDetail) {
        int update = super.update(packDetail);
        cache.del(CacheKey.PACK_CODE, packDetail.getPackCode());
        return update;
    }

    @Override
    public List<PackDetailDto> findList(PackDetailCriteria criteria) {
        return packDetailMapper.findList(criteria);
    }

    @Override
    public List<PackDetailDto> findByPackCode(String packCode) {
        return packDetailMapper.findByPackCode(packCode);
    }

    @Override
    public List<PackDetailDto> findByPackId(Integer id) {
        return packDetailMapper.findByPackId(id);
    }

    @Override
    public void deleteByCode(String packCode) {
        packDetailMapper.deleteByCode(packCode);
    }

    @Override
    public PackDetail findLevel(Integer packDetailId) {
        return packDetailMapper.findLevel(packDetailId);
    }

    @Override
    public List<PackDetail> findByCode(String packCode) {
        return packDetailMapper.findByCode(packCode);
    }

    @Override
    public List<PackDetail2> findByCode2(String packCode) {
        if (packCode == null) {
            packCode = "sys_pack";
        }
        List<PackDetail2> list = cache.getObject(CacheKey.PACK_CODE, packCode);
        if (list == null) {
            list = packDetailMapper.findByCode2(packCode);
            cache.setObject(CacheKey.PACK_CODE, packCode, list, CacheTime.PACK_CODE);
        }
        return list;
    }

}
