package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.AreaInfoMapper;
import com.deer.wms.base.system.dao.ShelfInfoMapper;
import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Area.AreaInfoCriteria;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.service.AreaInfoService;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.system.manage.model.ValidatorAreaInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/21.
 */
@Service
@Transactional
public class AreaInfoServiceImpl extends AbstractService<AreaInfo, Integer> implements AreaInfoService {

    @Autowired
    private AreaInfoMapper areaInfoMapper;
    @Autowired
    private ShelfInfoMapper shelfInfoMapper;

    @Override
    public List<AreaInfoDto> findList(AreaInfoCriteria criteria) {
        return areaInfoMapper.findList(criteria);
    }

    @Override
    public List<AreaInfoDto> findByCode(String areaCode) {
        return areaInfoMapper.findByCode(areaCode);
    }

    @Override
    public List<AreaInfoDto> findByName(String areaName) {
        return areaInfoMapper.findByName(areaName);
    }

    @Override
    public int validatorChongfu(ValidatorAreaInfoModel model) {
        return areaInfoMapper.validatorChongfu(model);
    }

    @Override
    public void saveAreaInfo(AreaInfo areaInfo) {
        if (StringUtils.isEmpty(areaInfo.getAreaCode())){
            throw new ServiceException(CommonCode.CODE_NULL);
        }
        ValidatorAreaInfoModel model = new ValidatorAreaInfoModel();
        model.setWareId(areaInfo.getWareId());
        model.setAreaCode(areaInfo.getAreaCode());
        int count = areaInfoMapper.validatorChongfu(model);
        if (count > 0){
            throw new ServiceException(CommonCode.AREA_CODE_EXISTS, "此货区编码已存在");
        }
        String nowDate = DateUtils.getNowDateTimeString();
        areaInfo.setAddTime(nowDate);
        this.save(areaInfo);
    }

    @Override
    public void updateAreaInfo(AreaInfo areaInfo) {
        if (StringUtils.isEmpty(areaInfo.getAreaCode())){
            throw new ServiceException(CommonCode.CODE_NULL);
        }
        List<AreaInfoDto> list = areaInfoMapper.findByWareIdAndCode(areaInfo.getWareId(), areaInfo.getAreaCode());
        if (list.size() == 0) {
            String nowDate = DateUtils.getNowDateTimeString();
            areaInfo.setAddTime(nowDate);
            this.update(areaInfo);
        } else if (list.size() == 1) {
            if (list.get(0).getAreaId().equals(areaInfo.getAreaId())) {
                String nowDate = DateUtils.getNowDateTimeString();
                areaInfo.setAddTime(nowDate);
                this.update(areaInfo);
            } else {
                throw new ServiceException(CommonCode.AREA_CODE_EXISTS, "此货区编码已存在");
            }
        }else {
            throw new ServiceException(CommonCode.AREA_CODE_EXISTS, "此货区编码已存在");
        }
    }

    @Override
    public void deleteByAreaId(Integer id) {
        List<ShelfInfo> list = shelfInfoMapper.findByAreaId(id);
        if (list != null && list.size() > 0){
            throw new ServiceException(CommonCode.NOT_DELETE_AREA);
        }else{
            this.deleteById(id);
        }
    }
}
