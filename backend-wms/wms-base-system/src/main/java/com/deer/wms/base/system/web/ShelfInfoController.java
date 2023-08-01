package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.system.manage.model.ValidatorShelfInfoModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Cell.CellInfo;
import com.deer.wms.base.system.model.Shelf.ShelfCell;
import com.deer.wms.base.system.model.Shelf.ShelfInfo;
import com.deer.wms.base.system.model.Shelf.ShelfInfoCriteria;
import com.deer.wms.base.system.model.Shelf.ShelfInfoDto;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.base.system.service.CellInfoService;
import com.deer.wms.base.system.service.ShelfInfoService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.BeanUtils;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.service.WareInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 货架信息api接口
 * 
 * Created by guo on 2019/11/27.
 */
@RestController
@RequestMapping("/shelf/infos")
public class ShelfInfoController {

    @Autowired
    private ShelfInfoService shelfInfoService;

    @Autowired
    private CellInfoService cellInfoService;

    @Autowired
    private AreaInfoService areaInfoService;

    @Autowired
    private WareInfoService wareInfoService;

    @PostMapping("/insert")
    @Authority("shelf_infos_insert")
    public Result add(@RequestBody ShelfInfo shelfInfo) {
        shelfInfoService.save(shelfInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 添加货架信息
     */
    @PostMapping("/autoInsert")
    @Transactional
    @Authority("shelf_infos_autoInsert")
    public Result autoInsert(@RequestBody ShelfCell shelfCell, @User CurrentUser currentUser) {
        shelfInfoService.saveShelfInfo(shelfCell);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("shelf_infos_delete")
    public Result delete(Integer id) {
        Integer shelfInfoId = id;
        shelfInfoService.deleteByshelfInfoId(shelfInfoId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("shelf_infos_update")
    public Result update(@RequestBody ShelfCell shelfCell) {
        shelfInfoService.updateShelfInfo(shelfCell);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新货架信息
     */
    @PostMapping("/autoUpdate")
    @Authority("shelf_infos_autoUpdate")
    public Result update(@RequestBody ShelfCell shelfCell, @User CurrentUser currentUser) {

        cellInfoService.deleteByShelfId(shelfCell.getShelfInfoId());
        ShelfInfo shelfInfo = shelfInfoService.findById(shelfCell.getShelfInfoId());
        BeanUtils.copyProperties(shelfCell, shelfInfo);
        shelfInfoService.update(shelfInfo);
        AreaInfo areaInfo = areaInfoService.findById(shelfInfo.getAreaId());
        String wareFeel = shelfInfo.getShelfCode();
        String areaFeel = areaInfo.getAreaCode();

        Integer shelfRow = shelfInfo.getShelfRow();
        Integer shelfColumn = shelfInfo.getShelfColumn();
        for (int i = 1; i <= shelfRow; i++) {
            for (int j = 1; j <= shelfColumn; j++) {
                CellInfo cellInfo = new CellInfo();
                BeanUtils.copyProperties(shelfCell, cellInfo);

                String one = j <= 9 ? "0" + j : "" + j;
                String two = i <= 9 ? "0" + i : "" + i;
//                String three = shelfNo <= 9 ? "0" + shelfNo : "" + shelfNo;
                String cellCode = areaFeel + "-" + wareFeel + "-" + two + "-" + one;
                cellInfo.setCellCode(cellCode);
                cellInfo.setShelfInfoId(shelfInfo.getShelfInfoId());
                cellInfo.setSColumn(j);
                cellInfo.setSRow(i);
                cellInfoService.save(cellInfo);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("shelf_infos_detail")
    public Result detail(Integer id) {
        ShelfInfo shelfInfo = shelfInfoService.findById(id);
        return ResultGenerator.genSuccessResult(shelfInfo);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("shelf_infos_list")
    public Result list(ShelfInfoCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ShelfInfoDto> list = shelfInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private PrintTempInfoService printTempInfoService;

    /**
     * 打印
     */
    @GetMapping("/print")
    @Authority("shelf_infos_print")
    public Result print(Integer id, Integer wareId, @User CurrentUser currentUser) {

        ShelfInfo shelfInfo = shelfInfoService.findById(id);

        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "货位组标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到货位标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();

        PrintTempUtils.setTempData("SHELF_CODE", shelfInfo.getShelfCode(), doc, "text");
        PrintTempUtils.setTempData("SHELF_CODE_QR_CODE", shelfInfo.getShelfCode(), doc, "qrCode");// TODO xuesinuo 货架打印，根本没在用
        PrintTempUtils.setTempData("SHELF_CODE_BAR_CODE", shelfInfo.getShelfCode(), doc, "barCode");

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return ResultGenerator.genSuccessResult(tempHtml);
    }

    /**
     * 验证货位组信息重复
     */
    @PostMapping("/validator")
    @Authority("shelf_infos_validator")
    public Result validatorAreaInfo(@RequestBody ValidatorShelfInfoModel model) {
        int count = shelfInfoService.validatorChongfu(model);
        if (count > 0) {
            if (model.getShelfCode() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.CODE_EXISTS);
            }
            if (model.getShelfName() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.NAME_EXISTS);
            }
        }
        return ResultGenerator.genSuccessResult(CommonCode.NAME_CODE_NO_EXISTS);
    }

}
