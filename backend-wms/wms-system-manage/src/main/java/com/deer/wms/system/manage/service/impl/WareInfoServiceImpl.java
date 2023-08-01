package com.deer.wms.system.manage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.PinYinUtil;
import com.deer.wms.system.manage.dao.WareInfoMapper;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.model.ware.WareInfoCriteria;
import com.deer.wms.system.manage.model.ware.WareInfoDto;
import com.deer.wms.system.manage.model.ware.WareInfoDto2;
import com.deer.wms.system.manage.service.WareInfoService;

/**
 * Created by guo on 2019/11/06.
 */
@Service
@Transactional
public class WareInfoServiceImpl extends AbstractService<WareInfo, Integer> implements WareInfoService {

    @Autowired
    private WareInfoMapper wareInfoMapper;

    @Autowired
    private CacheTool cache;

    @Override
    public void updateStatus(String wareId, String status) {
        wareInfoMapper.updateStatus(wareId, status);
    }

    @Override
    public void deleteByCodeAndCom(WareInfoCriteria criteria) {
        wareInfoMapper.deleteByCodeAndCom(criteria);
    }

    @Override
    public List<WareInfoDto2> findByName(String wareName) {
        return wareInfoMapper.findByName(wareName);
    }

    @Override
    public List<WareInfoDto> findList(WareInfoCriteria criteria) {
        return wareInfoMapper.findList(criteria);
    }

    @Override
    public List<WareInfo> findAllByStatus() {
        return wareInfoMapper.findAllByStatus();
    }

    @Override
    public List<WareInfoDto2> findSome(WareInfoCriteria criteria) {
        return wareInfoMapper.findSome(criteria);
    }

    /***
     *
     * @param wareInfo
     * @return
     */
    @Override
    public WareInfo createWare(WareInfoDto wareInfo) {
        /**
         * 新建仓库
         */
        String nowDate = DateUtils.getNowDateTimeString();
        wareInfo.setAddTime(nowDate);
        String code = PinYinUtil.getPinYinHeadChar(wareInfo.getWareName());
        wareInfo.setWareCode(code);
        save(wareInfo);
        return wareInfo;
    }

    @Override
    public List<Integer> searchAllWareId() {
        return wareInfoMapper.searchAllWareId();
    }

    @Override
    public void save(WareInfo wareInfo) {
        super.save(wareInfo);
        cache.del(CacheKey.ALL_WARE_INFO);
    }

    @Override
    public void deleteById(Integer wareInfoId) {
        super.deleteById(wareInfoId);
        cache.del(CacheKey.ALL_WARE_INFO);
    }

    @Override
    public int update(WareInfo wareInfo) {
        int update = super.update(wareInfo);
        cache.del(CacheKey.ALL_WARE_INFO);
        return update;
    }

}
