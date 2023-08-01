package com.deer.wms.base.system.service.impl;

import com.deer.wms.base.system.dao.ShelfInfoMapper;
import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Shelf.ShelfCell;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfoCriteria;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ShelfInfoService;

import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.system.manage.model.ValidatorShelfInfoModel;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by guo on 2019/11/27.
 */
@Service
@Transactional
public class ShelfInfoServiceImpl extends AbstractService<ShelfInfo, Integer> implements ShelfInfoService {

    @Autowired
    private ShelfInfoMapper shelfInfoMapper;
    @Autowired
    private CellInfoService cellInfoService;
    @Autowired
    private AreaInfoService areaInfoService;
    @Autowired
    private WareInfoService wareInfoService;

    @Override
    public List<ShelfInfoDto> findList(ShelfInfoCriteria criteria) {
        return shelfInfoMapper.findList(criteria);
    }

    @Override
    public ShelfInfoDto findByCodeAndWareId(Integer wareId, String shelfCode) {
        return shelfInfoMapper.findByCodeAndWareId(wareId, shelfCode);
    }

    @Override
    public List<ShelfInfoDto> findByCode(String shelfCode) {
        return shelfInfoMapper.findByCode(shelfCode);
    }

    @Override
    public int validatorChongfu(ValidatorShelfInfoModel model) {
        return shelfInfoMapper.validatorChongfu(model);
    }

    @Override
    public void saveShelfInfo(ShelfCell shelfCell) {
        if (StringUtils.isEmpty(shelfCell.getShelfCode())){
            throw new ServiceException(CommonCode.CODE_NULL);
        }
        ValidatorShelfInfoModel model = new ValidatorShelfInfoModel();
        model.setShelfCode(shelfCell.getShelfCode());
        model.setWareId(shelfCell.getWareId());
        int count = validatorChongfu(model);
        if (count > 0){
            throw new ServiceException(CommonCode.CODE_EXISTS);
        }
        ShelfInfo shelfInfo = new ShelfInfo();
        BeanUtils.copyProperties(shelfCell, shelfInfo);

        AreaInfo areaInfo = areaInfoService.findById(shelfCell.getAreaId());
        WareInfo wareInfo = wareInfoService.findById(areaInfo.getWareId());

        String wareFeel = shelfCell.getShelfCode();
        String areaFeel = areaInfo.getAreaCode();

        this.save(shelfInfo);
        Integer shelfRow = shelfInfo.getShelfRow();
        Integer shelfColumn = shelfInfo.getShelfColumn();
        for (int i = 1; i <= shelfRow; i++) {
            for (int j = 1; j <= shelfColumn; j++) {

                String one = j <= 9 ? "0" + j : "" + j;
                String two = i <= 9 ? "0" + i : "" + i;
                String cellCode = areaFeel + "-" + wareFeel + "-" + two + "-" + one;
                CellInfo cellInfo = new CellInfo();
                BeanUtils.copyProperties(shelfCell, cellInfo);
                cellInfo.setCellCode(cellCode);
                cellInfo.setCellName(cellCode);
                cellInfo.setShelfInfoId(shelfInfo.getShelfInfoId());
                cellInfo.setSColumn(j);
                cellInfo.setSRow(i);
                cellInfo.setState("enable");
                cellInfo.setYCoordinate(i);
                cellInfo.setXCoordinate(j);
                cellInfo.setWareId(wareInfo.getWareId());
                cellInfo.setWareName(wareInfo.getWareName());
                cellInfoService.save(cellInfo);
            }
        }
    }

    @Override
    public void updateShelfInfo(ShelfCell shelfCell) {
        if (StringUtils.isEmpty(shelfCell.getShelfCode())){
            throw new ServiceException(CommonCode.CODE_NULL);
        }
        ValidatorShelfInfoModel model = new ValidatorShelfInfoModel();
        model.setShelfInfoId(shelfCell.getShelfInfoId());
        model.setShelfCode(shelfCell.getShelfCode());
        model.setWareId(shelfCell.getWareId());
        int count = validatorChongfu(model);
        if (count > 0){
            throw new ServiceException(CommonCode.CODE_EXISTS);
        }
        ShelfInfo shelfInfo = new ShelfInfo();
        BeanUtils.copyProperties(shelfCell, shelfInfo);
        this.update(shelfInfo);
    }

    @Override
    public void deleteByshelfInfoId(Integer shelfInfoId) {
        List<CellInfo> list = cellInfoService.findByShelfInfoId(shelfInfoId);
        if (list != null && list.size() > 0){
            throw new ServiceException(CommonCode.NOT_DELETE_SHELF);
        }else{
            this.deleteById(shelfInfoId);
        }
    }

}
