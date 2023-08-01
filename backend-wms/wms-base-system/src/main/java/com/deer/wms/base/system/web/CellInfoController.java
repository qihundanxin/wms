package com.deer.wms.base.system.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.CellData;
import com.deer.wms.base.system.listener.CellListener;
import com.deer.wms.base.system.model.Cell.*;
import com.deer.wms.system.manage.model.ValidatorCellInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.Constants;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.ExcelUtils;
import com.deer.wms.project.root.util.Np;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.project.root.util.SimpleMap;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 货位信息api接口
 * 
 * Created by guo on 2019/11/27.
 */
@RestController
@RequestMapping("/cell/infos")
public class CellInfoController {

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private WareInfoService wareInfoService;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 新增货位信息
     */
    @PostMapping("/insert")
    @Authority("cell_infos_insert")
    public Result add(@RequestBody CellInfo cellInfo, @User CurrentUser currentUser) {
        Integer wareId = cellInfo.getWareId();
        WareInfo wareInfo =wareInfoService.findById(wareId);
        if(wareInfo==null){
            throw new ServiceException(CommonCode.SERVICE_ERROR, "仓库ID无效");
        }
        cellInfoService.saveCellInfo(cellInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("cell_infos_delete")
    public Result delete(Integer id) {
        // 查询货位下是否有库存余量  没有则不允许删除货位
        cellInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/deleteByIds")
    @Authority("cell_infos_deleteByIds")
    public Result delete(String ids) {
        cellInfoService.deleteByIds(ids);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/getCellByCellCode")
    @Authority("cell_infos_getCellByCellCode")
    public Result getCellByCellCode(String cellCode,Integer wareId) {
        CellInfo cellInfo = cellInfoService.findByCodeAndWareId(cellCode,wareId);
        return ResultGenerator.genSuccessResult(cellInfo);
    }

    @GetMapping("/byCellCodeOnly")
    @Authority("cell_infos_byCellCodeOnly")
    public Result getByCellCodeOnly(String cellCode) {
        List<CellInfo> cellInfoList = cellInfoService.findByCode(cellCode);
        return ResultGenerator.genSuccessResult(SimpleMap.init("cellInfoList", cellInfoList));
    }

    @Transactional // FIXME 临时方案：允许特定人员修改货位使用类型
    @PostMapping("/update")
    @Authority("cell_infos_update")
    public Result update(@RequestBody CellInfo cellInfo, @User CurrentUser currentUser) {
        CellInfo old = cellInfoService.findById(cellInfo.getCellId());
        if (!cellInfo.getCellCode().equals(old.getCellCode())){
            throw new ServiceException(CommonCode.PARAMETER_ERROR, "货位编码不可修改");
        }
        if (cellInfo.getCellUseType() == null) {
            cellInfo.setCellUseType(old.getCellUseType());
        } else if (!cellInfo.getCellUseType().equals(old.getCellUseType())) {
            boolean isAuth;
            if(currentUser.getIsAdmin()!=1){
                List<String> list = currentUser.getAuthMenus();
                if (list == null || list.size() == 0){
                    isAuth = false;
                }else {
                    isAuth = list.contains("cell_use_type_update");
                }
                if (!isAuth){
                    throw new ServiceException(CommonCode.NO_AUTH, "您没有修改货位使用类型权限！");
                }
            }

//            if (Np.i(currentUser.getAccount()).in("zhouzhi", "Yiwang", "JiaoXueMing", "admin", "lixuehe")) {// 临时方案
//                // 允许修改
//            } else {
//                throw new ServiceException(CommonCode.PARAMETER_ERROR, "货位使用类型不可修改");
//            }
        }
        cellInfoService.update(cellInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("cell_infos_detail")
    public Result detail(Integer id) {
        CellInfo cellInfo = cellInfoService.findById(id);
        return ResultGenerator.genSuccessResult(cellInfo);
    }

    @GetMapping("/list")
    @Authority("cell_infos_list")
    public Result list(CellInfoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CellInfoDto> list = cellInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findByShelfInfoId")
    @Authority("cell_infos_findByShelfInfoId")
    public Result findByShelfInfoId(Integer shelfInfoId) {
        List<CellInfo> list = cellInfoService.findByShelfInfoId(shelfInfoId);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/findByCellUserTypes")
    @Authority("cell_infos_findByCellUserTypes")
    public Result findByCellUserTypes(CellInfoCriteria criteria) {
        List<CellInfoDto> list = cellInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 启用货位
     */
    @GetMapping("/enable")
    @Authority("cell_infos_enable")
    public Result enable(String cellCode, Integer wareId, @User CurrentUser currentUser) {

        cellInfoService.modifyCellState(cellCode, wareId, Constants.CELL_ENABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 停用货位
     */
    @GetMapping("/disable")
    @Authority("cell_infos_disable")
    public Result disable(String cellCode, Integer wareId, @User CurrentUser currentUser) {

        cellInfoService.modifyCellState(cellCode, wareId, Constants.CELL_DISABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private PrintTempInfoService printTempInfoService;

    /**
     * 打印
     */
    @GetMapping("/print")
    @Authority("cell_infos_print")
    public Result print(String cellCode, Integer wareId, @User CurrentUser currentUser) {

        CellInfoDto cellInfoDto = cellInfoService.findByCodeAndWareId(cellCode, wareId);

        if(cellInfoDto==null){
            throw  new ServiceException(CommonCode.SERVICE_ERROR,"货位编码无效");
        }

        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "货位标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到货位标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();

        PrintTempUtils.setTempData("WARE_CODE", cellInfoDto.getWareCode(), doc, "text");
        PrintTempUtils.setTempData("WARE_NAME", cellInfoDto.getWareName(), doc, "text");

        PrintTempUtils.setTempData("AREA_NAME", cellInfoDto.getAreaName(), doc, "text");
        PrintTempUtils.setTempData("AREA_CODE", cellInfoDto.getAreaCode(), doc, "text");

        PrintTempUtils.setTempData("CELL_CODE", cellInfoDto.getCellCode(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE_QR_CODE", cellInfoDto.getCellCode(), doc, "qrCode");
        PrintTempUtils.setTempData("CELL_CODE_BAR_CODE", cellInfoDto.getCellCode(), doc, "barCode");

        PrintTempUtils.setTempData("SHELF_CODE", cellInfoDto.getShelfCode(), doc, "text");
        PrintTempUtils.setTempData("SHELF_CODE_QR_CODE", cellInfoDto.getShelfCode(), doc, "qrCode");
        PrintTempUtils.setTempData("SHELF_CODE_BAR_CODE", cellInfoDto.getShelfCode(), doc, "barCode");

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return ResultGenerator.genSuccessResult(tempHtml);
    }

    /**
     * 批量打印
     */
    @GetMapping("/plPrint")
    @Authority("cell_infos_plPrint")
    public Result plPrint(PlPrintCell plPrintCell, @User CurrentUser currentUser) {


        Integer wareId = plPrintCell.getWareId();
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "货位标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到货位标签模板");
        }
        List<String> htmls = new ArrayList<>();
        for (CellInfo cellInfo:plPrintCell.getCells()
             ) {

        CellInfoDto cellInfoDto = cellInfoService.findByCodeAndWareId(cellInfo.getCellCode(), wareId);


        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();

        PrintTempUtils.setTempData("WARE_CODE", cellInfoDto.getWareCode(), doc, "text");
        PrintTempUtils.setTempData("WARE_NAME", cellInfoDto.getWareName(), doc, "text");

        PrintTempUtils.setTempData("AREA_NAME", cellInfoDto.getAreaName(), doc, "text");
        PrintTempUtils.setTempData("AREA_CODE", cellInfoDto.getAreaCode(), doc, "text");

        PrintTempUtils.setTempData("CELL_CODE", cellInfoDto.getCellCode(), doc, "text");
        PrintTempUtils.setTempData("CELL_CODE_QR_CODE", cellInfoDto.getCellCode(), doc, "qrCode");
        PrintTempUtils.setTempData("CELL_CODE_BAR_CODE", cellInfoDto.getCellCode(), doc, "barCode");

        PrintTempUtils.setTempData("SHELF_CODE", cellInfoDto.getShelfCode(), doc, "text");
        PrintTempUtils.setTempData("SHELF_CODE_QR_CODE", cellInfoDto.getShelfCode(), doc, "qrCode");
        PrintTempUtils.setTempData("SHELF_CODE_BAR_CODE", cellInfoDto.getShelfCode(), doc, "barCode");

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
            htmls.add(tempHtml);
        }
        return ResultGenerator.genSuccessResult(htmls);
    }

    /**
     * 导入仓位拣货排序
     */
    @PostMapping("/importCellPickOrder")
    @Transactional
    @Authority("cell_infos_importCellPickOrder")
    public Result importCellPickOrder(CellImportModel cellImportModel) {
        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";

        try {
            billWorkbook = jxl.Workbook.getWorkbook(cellImportModel.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            Integer rowCount = 0;
            for (int i = 2; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 1, i);
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }

            for (int i = 2; i < rowCount + 2; i++) {
                String cellCode = ExcelUtils.getCellString(billSheet, 1, i).trim();
                String cellPickOrder = ExcelUtils.getCellString(billSheet, 2, i).trim();
                String cellGroundOrder = ExcelUtils.getCellString(billSheet, 3, i).trim();
                if (StringUtils.isBlank(cellCode)) {
                    msg = "缺少货位编号！";
                    throw new RuntimeException();
                }
                cellInfoService.updateCellInfo(new CellInfo() {{
                    setCellCode(cellCode);
                    setPickOrder(cellPickOrder);
                    setGroundOrder(cellGroundOrder);
                }}, cellImportModel.getWareId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 从Excel读取库位信息
     */
    @PostMapping("/readCellList")
    @Authority("cell_infos_import")
    public Result readCellList(@RequestPart("file") MultipartFile file) {
        List<CellExcelModel> list = new ArrayList<>();
        try{
            list = EasyExcel.read(file.getInputStream(),CellExcelModel.class, new CellListener()).doReadAllSync();
        }catch (Exception e) {
            if (e instanceof ExcelDataConvertException) {
                ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) e;
                String cellMsg = "";
                CellData cellData = excelDataConvertException.getCellData();
                //这里有一个celldatatype的枚举值,用来判断CellData的数据类型CellDataTypeEnum
                CellDataTypeEnum type = cellData.getType();
                if (type.equals(CellDataTypeEnum.NUMBER)) {
                    cellMsg = cellData.getNumberValue().toString();
                } else if (type.equals(CellDataTypeEnum.STRING)) {
                    cellMsg = cellData.getStringValue();
                } else if (type.equals(CellDataTypeEnum.BOOLEAN)) {
                    cellMsg = cellData.getBooleanValue().toString();
                }
                String errorMsg = String.format("excel表格:第%s行,第%s列,数据值为:%s,该数据值不符合要求,请检验后重新导入!<span style=\"color:red\">请检查其他的记录是否有同类型的错误!</span>", excelDataConvertException.getRowIndex() + 1, excelDataConvertException.getColumnIndex(), cellMsg);
                //log.error(errorMsg);
                throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, errorMsg);
            }else if (e instanceof ServiceException){
                throw new ServiceException(CommonCode.SERVICE_ERROR,e.getMessage());
            }else {
                throw new ServiceException(CommonCode.SYSTEM_ERROR);
            }
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 保存从Excel读取的库位信息
     */
    @PostMapping("/import")
    @Authority("cell_infos_import")
    public Result importCellInfo(@RequestBody CellInfoImportParam excelModel) {
        cellInfoService.saveExcelMode(excelModel);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 验证货位信息重复
     */
    @PostMapping("/validator")
    @Authority("cell_infos_validator")
    public Result validatorAreaInfo(@RequestBody ValidatorCellInfoModel model) {
        int count = cellInfoService.validatorChongfu(model);
        if (count > 0) {
            if (model.getCellCode() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.CODE_EXISTS);
            }
            if (model.getCellName() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.NAME_EXISTS);
            }
        }
        return ResultGenerator.genSuccessResult(CommonCode.NAME_CODE_NO_EXISTS);
    }
}
