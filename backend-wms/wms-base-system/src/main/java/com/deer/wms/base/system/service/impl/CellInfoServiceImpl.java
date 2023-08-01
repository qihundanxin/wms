package com.deer.wms.base.system.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.base.system.dao.CellInfoMapper;
import com.deer.wms.base.system.event.UpdateCellInfoEvent;
import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.base.system.model.Cell.CellExcelModel;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Cell.CellInfoCriteria;
import com.deer.wms.base.system.model.Cell.CellInfoDto;
import com.deer.wms.base.system.model.Cell.CellInfoImportParam;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ShelfInfoService;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BatchesInsertUtil;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.StringUtils;
import com.deer.wms.system.manage.model.ValidatorCellInfoModel;

import cn.hutool.core.util.StrUtil;

/**
 * Created by guo on 2019/11/27.
 */
@Service
@Transactional
public class CellInfoServiceImpl extends AbstractService<CellInfo, Integer> implements CellInfoService {

    @Autowired
    private CellInfoMapper cellInfoMapper;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private AreaInfoService areaInfoService;

    @Autowired
    private ShelfInfoService shelfInfoService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void deleteById(Integer id) {
        CellInfo cellInfo = this.findById(id);
        if (Np.i(cellInfo.getStatus()).ifNull(0).notEq(0)) {
            throw new ServiceException(CommonCode.CELL_ERROR, cellInfo.getCellCode() + "有库存，不允许删除");
        }
        cellInfoMapper.deleteByPrimaryKey(id);
    }


    @Override
    public void deleteByIds(String ids) {
        String[] idArray = ids.split(",");
        for (String idString : idArray) {
            Integer id = Integer.parseInt(idString);
            this.deleteById(id);
        }
    }

    @Override
    public String getSysAcceptCell(Integer wareId) {
        CellInfo cellInfo = cellInfoMapper.getSysCell(wareId, 0);
        if (cellInfo == null) {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.setWareId(wareId);
            areaInfo.setAreaName(wareId + "0");
            areaInfo.setAreaCode(wareId + "0");
            areaInfo.setIsSys(1);
            areaInfoService.save(areaInfo);

            ShelfInfo shelfInfo = new ShelfInfo();
            shelfInfo.setAreaId(areaInfo.getAreaId());
            shelfInfo.setState(1);
            shelfInfo.setShelfCode(areaInfo.getAreaCode() + "0");
            shelfInfo.setShelfName(areaInfo.getAreaCode() + "0");
            shelfInfo.setIsSys(1);
            shelfInfo.setType(0);
            shelfInfoService.save(shelfInfo);

            cellInfo = new CellInfo();
            cellInfo.setShelfInfoId(shelfInfo.getShelfInfoId());
            cellInfo.setCellUseType(0);
            cellInfo.setCellCode(shelfInfo.getShelfCode() + "0");
            cellInfo.setCellName("内置验收默认货位");
            cellInfo.setIsSys(1);
            cellInfo.setWareId(wareId);
            cellInfoService.save(cellInfo);

        }
        return cellInfo.getCellCode();
    }

    @Override
    public CellInfo getSysAcceptCellInfo(Integer wareId) {
        CellInfo cellInfo = cellInfoMapper.getSysCell(wareId, 0);
        if (cellInfo == null) {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.setWareId(wareId);
            areaInfo.setAreaName(wareId + "0");
            areaInfo.setAreaCode(wareId + "0");
            areaInfo.setIsSys(1);
            areaInfoService.save(areaInfo);

            ShelfInfo shelfInfo = new ShelfInfo();
            shelfInfo.setAreaId(areaInfo.getAreaId());
            shelfInfo.setState(1);
            shelfInfo.setShelfCode(areaInfo.getAreaCode() + "0");
            shelfInfo.setShelfName(areaInfo.getAreaCode() + "0");
            shelfInfo.setIsSys(1);
            shelfInfo.setType(0);
            shelfInfoService.save(shelfInfo);

            cellInfo = new CellInfo();
            cellInfo.setShelfInfoId(shelfInfo.getShelfInfoId());
            cellInfo.setCellUseType(0);
            cellInfo.setCellCode(shelfInfo.getShelfCode() + "0");
            cellInfo.setCellName("内置验收默认货位");
            cellInfo.setIsSys(1);
            cellInfo.setWareId(wareId);
            cellInfoService.save(cellInfo);

        }
        return cellInfo;
    }

    @Override
    public String getSysJhCell(Integer wareId) {
        CellInfo cellInfo = cellInfoMapper.getSysCell(wareId, 3);
        if (cellInfo == null) {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.setWareId(wareId);
            areaInfo.setAreaName(wareId + "3");
            areaInfo.setAreaCode(wareId + "3");
            areaInfo.setIsSys(1);
            areaInfoService.save(areaInfo);

            ShelfInfo shelfInfo = new ShelfInfo();
            shelfInfo.setAreaId(areaInfo.getAreaId());
            shelfInfo.setState(1);
            shelfInfo.setShelfCode(areaInfo.getAreaCode() + "3");
            shelfInfo.setShelfName(areaInfo.getAreaCode() + "3");
            shelfInfo.setIsSys(1);
            shelfInfo.setType(0);
            shelfInfoService.save(shelfInfo);

            cellInfo = new CellInfo();
            cellInfo.setShelfInfoId(shelfInfo.getShelfInfoId());
            cellInfo.setCellUseType(3);
            cellInfo.setCellCode(shelfInfo.getShelfCode() + "3");
            cellInfo.setCellName("内置集货默认货位");
            cellInfo.setIsSys(1);
            cellInfo.setWareId(wareId);
            cellInfoService.save(cellInfo);

        }
        return cellInfo.getCellCode();
    }

    @Override
    public List<CellInfoDto> findList(CellInfoCriteria criteria) {
        return cellInfoMapper.findList(criteria);
    }

    @Override
    public List<CellInfo> findByShelfInfoId(Integer shelfInfoId) {
        return cellInfoMapper.findByShelfInfoId(shelfInfoId);
    }

    @Override
    public void deleteByShelfId(Integer shelfInfoId) {
        List<CellInfo> cellInfoList = cellInfoMapper.findByShelfInfoId(shelfInfoId);
        String errorCodeCodes = cellInfoList.stream().filter(x -> Np.i(x.getStatus()).ifNull(0).notEq(0)).map(x -> x.getCellCode()).collect(Collectors.joining(","));
        if (StrUtil.isNotBlank(errorCodeCodes)) {
            throw new ServiceException(CommonCode.CELL_ERROR, errorCodeCodes + "有库存，不允许删除");
        }
        cellInfoMapper.deleteByShelfId(shelfInfoId);
    }

    @Override
    public CellInfoDto findByCodeAndWareId(String itemCode, Integer wareId) {
        return cellInfoMapper.findByCodeAndWareId(itemCode, wareId);
    }

    @Override
    public void modifyCellState(String cellCode, Integer wareId, String sate, CurrentUser currentUser) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode, wareId);
        cellInfo.setState(sate);
        cellInfoService.update(cellInfo);
    }

    @Override
    public List<CellInfoDto> findByCellUseTypes(CellInfoCriteria criteria) {
        return cellInfoMapper.findByCellUseTypes(criteria);
    }

    @Override
    public String getNewInCell(Integer wareId) {
        String cellCode = cellInfoMapper.getNewInCell(wareId);

        return cellCode;
    }

    @Override
    public Integer getCellType(String cellCode, Integer wareId) {
        CellInfo cellInfo = cellInfoMapper.findByCodeAndWareId(cellCode, wareId);
        return Np.i(cellInfo).x(x -> x.getCellUseType()).o();
    }

    @Override
    public void updateCellInfo(CellInfo cellInfo, Integer wareId) {
        CellInfoDto byCodeAndWareId = cellInfoMapper.findByCodeAndWareId(cellInfo.getCellCode(), wareId);
        if (byCodeAndWareId == null) {
            return;
        }
        if (Np.i(byCodeAndWareId.getStatus()).notEq(cellInfo.getStatus())){
            cellInfo.setStatus(Np.i(cellInfo.getStatus()).o(0));
            cellInfo.setCellId(byCodeAndWareId.getCellId());
            cellInfo.setCellCode(null);
            cellInfoMapper.updateByPrimaryKeySelective(cellInfo);
        }
//        eventPublisher.publishEvent(new UpdateCellInfoEvent(byCodeAndWareId, cellInfo));
    }

    @Override
    public int update(CellInfo cellInfo) {
        CellInfo source = this.findById(cellInfo.getCellId());
        int result = cellInfoMapper.updateByPrimaryKeySelective(cellInfo);
        eventPublisher.publishEvent(new UpdateCellInfoEvent(source, cellInfo));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExcelMode(CellInfoImportParam cellInfoImportParam) {
        List<CellExcelModel> list = cellInfoImportParam.getList();
        //货区
        List<AreaInfo> areaList = new ArrayList<>();
        //货位组
        List<ShelfInfo> shelfList = new ArrayList<>();
        //货位
        List<CellInfo> cellList = new ArrayList<>();
        String dataTime = DateUtils.getNowDateTimeString();
        for (CellExcelModel area: list) {
            AreaInfo areaInfo = new AreaInfo();
            areaInfo.setWareId(cellInfoImportParam.getWareId());
            areaInfo.setAreaCode(area.getAreaCode());
            areaInfo.setAreaName(area.getAreaName());
            areaInfo.setPriorityLevel(0);
            areaInfo.setAddTime(dataTime);
            areaList.add(areaInfo);
        }
        //根据货区编码去重
        areaList = areaList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<AreaInfo>(Comparator.comparing(a -> String.valueOf(a.getAreaCode())))), ArrayList::new));
        //已经存在的货区
        List<AreaInfo> existAreaList = new ArrayList<>();
        Iterator<AreaInfo> it = areaList.iterator();
        while(it.hasNext()){
            AreaInfo areaInfo = it.next();
            List<AreaInfoDto> alist = areaInfoService.findByCode(areaInfo.getAreaCode());
            if (alist != null && !alist.isEmpty()){
                if (cellInfoImportParam.getWareId().equals(alist.get(0).getWareId())){
                    areaInfo.setAreaId(alist.get(0).getAreaId());
                    existAreaList.add(areaInfo);
                    it.remove();
                }else {
                    throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + areaInfo.getAreaCode() + "的货区已存在但不属于当前仓库，请核对数据！");
                }
            }
        }
        if (areaList != null && !areaList.isEmpty()){
            // 进行新增
            Consumer<List<AreaInfo>> consumer = o -> areaInfoService.save(o);
            BatchesInsertUtil.batchSplitInsert(areaList, consumer);
        }
        areaList.addAll(existAreaList);
        for (CellExcelModel shelf: list){
            ShelfInfo shelfInfo = new ShelfInfo();
            for (AreaInfo area : areaList){
                if (shelf.getAreaCode().equals(area.getAreaCode())){
                    if (shelf.getType() == null){
                        throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + shelf.getShelfCode() + "的货位组类型数据错误，请核对数据！");
                    }
                    List<ShelfInfoDto> slist = shelfInfoService.findByCode(shelf.getShelfCode());
                    if (slist != null && !slist.isEmpty()){
                        int m = 0 , n = 0;
                        for (ShelfInfoDto sDto : slist){
                            //货位组已存在且属于当前仓库
                            if (cellInfoImportParam.getWareId().equals(sDto.getWareId())){
                                m++;
                                //货位组和货区信息匹配
                                if (area.getAreaCode().equals(sDto.getAreaCode())){
                                    n++;
                                    if (!shelf.getType().equals(sDto.getType())){
                                        throw new ServiceException(CommonCode.SERVICE_ERROR, "编码为" + shelf.getShelfCode() + "的货位组已存在,但是货位组类型数据错误!");
                                    }
                                }
                            }
                        }
                        if (m == 1){
                            if (n == 1){
                                shelfInfo.setAreaId(area.getAreaId());
                            }else {
                                throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + shelf.getShelfCode() + "的货位组已存在和货区不匹配，请核对数据！");
                            }
                        }else if (m > 1){
                            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + shelf.getShelfCode() + "的货位组数据不唯一，请核对数据！");
                        }else {
                            shelfInfo.setAreaId(area.getAreaId());
                        }
                    }else {
                        shelfInfo.setAreaId(area.getAreaId());
                    }
                }
            }
            shelfInfo.setShelfCode(shelf.getShelfCode());
            shelfInfo.setShelfName(shelf.getShelfName());
            shelfInfo.setShelfDescribe(shelf.getShelfName());
            shelfInfo.setType(shelf.getType());
            shelfList.add(shelfInfo);
        }
//        shelfList = shelfList.stream().distinct().collect(Collectors.toList());
        //根据货位组编码去重
        shelfList = shelfList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<ShelfInfo>(Comparator.comparing(s -> String.valueOf(s.getShelfCode())))), ArrayList::new));
        //已经存在的货位组
        List<ShelfInfo> existShelfList = new ArrayList<>();
        Iterator<ShelfInfo> sit = shelfList.iterator();
        while(sit.hasNext()){
            ShelfInfo shelfInfo = sit.next();
            ShelfInfoDto shelfInfoDto = shelfInfoService.findByCodeAndWareId(cellInfoImportParam.getWareId(),shelfInfo.getShelfCode());
            if (shelfInfoDto != null){
                shelfInfo.setShelfInfoId(shelfInfoDto.getShelfInfoId());
                existShelfList.add(shelfInfo);
                sit.remove();
            }
        }
        if (shelfList != null && !shelfList.isEmpty()){
            // 进行新增
            Consumer<List<ShelfInfo>> shelfConsumer = o -> shelfInfoService.save(o);
            BatchesInsertUtil.batchSplitInsert(shelfList, shelfConsumer);
        }
        shelfList.addAll(existShelfList);
        for (CellExcelModel cell: list){
            CellInfo cellInfo = new CellInfo();
            cellInfo.setWareId(cellInfoImportParam.getWareId());
            cellInfo.setWareName(cellInfoImportParam.getWareName());
            for (ShelfInfo shelf: shelfList){
                if (cell.getShelfCode().equals(shelf.getShelfCode())){
                    cellInfo.setShelfInfoId(shelf.getShelfInfoId());
                }
            }
            cellInfo.setCellCode(cell.getCellCode());
            cellInfo.setCellName(cell.getCellName());
            cellInfo.setCellUseType(cell.getCellUseType());
            cellInfo.setSkuLimit(cell.getSkuLimit());
            cellInfo.setXCoordinate(cell.getXCoordinate());
            cellInfo.setYCoordinate(cell.getYCoordinate());
            cellList.add(cellInfo);
        }
//        cellList = cellList.stream().distinct().collect(Collectors.toList());
        cellList = cellList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<CellInfo>(Comparator.comparing(c -> String.valueOf(c.getCellCode())))), ArrayList::new));
        //已经存在的货位
        List<CellInfo> existCellList = new ArrayList<>();
        Iterator<CellInfo> cit = cellList.iterator();
        while(cit.hasNext()){
            CellInfo cellInfo = cit.next();
            if(cellInfo.getCellUseType() == null){
                throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + cellInfo.getCellCode() + "的货位使用类型数据不正确，请核对数据！");
            }else{
                ValidatorCellInfoModel cellModel = new ValidatorCellInfoModel();
                cellModel.setCellCode(cellInfo.getCellCode());
                int cellCount = cellInfoMapper.validatorChongfu(cellModel);
                if (cellCount > 0) {
                    throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, "编码为" + cellInfo.getCellCode() + "的货位已存在，请核对数据！");
                }
            }
        }
        if (cellList != null && !cellList.isEmpty()){
            // 进行新增
            Consumer<List<CellInfo>> cellConsumer = o -> cellInfoService.save(o);
            BatchesInsertUtil.batchSplitInsert(cellList, cellConsumer);
        }
    }

    @Override
    public List<CellInfo> findByCode(String cellCode) {
        return cellInfoMapper.findByCode(cellCode);
    }

    @Override
    public CellInfo findCellInfo(CellInfo cellInfo){
        CellInfo cell = cellInfoMapper.findCellInfo(cellInfo);
        return cell;
    }
    public int validatorChongfu(ValidatorCellInfoModel model) {
        return cellInfoMapper.validatorChongfu(model);
    }

    @Override
    public void saveCellInfo(CellInfo cellInfo) {
        if (StringUtils.isEmpty(cellInfo.getCellCode())){
            throw new ServiceException(CommonCode.CODE_NULL);
        }
        ValidatorCellInfoModel model = new ValidatorCellInfoModel();
        model.setCellCode(cellInfo.getCellCode());
        int count = validatorChongfu(model);
        if (count > 0) {
            throw new ServiceException(CommonCode.CODE_EXISTS);
        }
        cellInfo.setStatus(Np.i(cellInfo.getStatus()).o(0));
        this.save(cellInfo);
    }

}
