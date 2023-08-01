package com.deer.wms.ware.task.web;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.ware.task.model.ShipBox;
import com.deer.wms.ware.task.model.ShipBoxCriteria;
import com.deer.wms.ware.task.model.ShipBoxDetail;
import com.deer.wms.ware.task.model.ShipBoxDto;
import com.deer.wms.ware.task.model.ShipBoxInsert;
import com.deer.wms.ware.task.service.ShipBoxDetailService;
import com.deer.wms.ware.task.service.ShipBoxService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2021/01/06.
 *
 * 装运箱信息
 */
@RestController
@RequestMapping("/ship/boxs")
public class ShipBoxController {

    @Autowired
    private ShipBoxService shipBoxService;
    @Autowired
    private ShipBoxDetailService shipBoxDetailService;
    @Autowired
    private PrintTempInfoService printTempInfoService;

    @PostMapping("/add")
    @Authority("ship_boxs_add")
    public Result add(@RequestBody ShipBox shipBox) {
        shipBoxService.save(shipBox);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/insert")
    @Authority("ship_boxs_insert")
    public Result insert(@RequestBody ShipBoxInsert insert, @User CurrentUser currentUser) {
        ShipBox shipBox = insert.getShipBox();
        if (shipBox.getShipBoxCode() == null || shipBox.getShipBoxCode().equals("")) {

        }
        shipBox.setCreateTime(DateUtils.getNowDateTimeString());
        shipBox.setCreateUserId(currentUser.getUserId());
        shipBox.setCreateUserName(currentUser.getUserName());
        shipBoxService.save(shipBox);
        if (shipBox.getShipBoxCode() == null || shipBox.getShipBoxCode().equals("")) {
            shipBox.setShipBoxCode("ZX" + shipBox.getShipBoxId());
            shipBoxService.update(shipBox);
        }
        List<String> list = insert.getList();
        for (String shipBillCode : list
        ) {
            ShipBoxDetail detail = new ShipBoxDetail();
            detail.setShipBillCode(shipBillCode);
            detail.setShipBoxId(shipBox.getShipBoxId());
            shipBoxDetailService.save(detail);
        }
        return ResultGenerator.genSuccessResult(boxPrint2(shipBox.getShipBoxId()));
    }

    @GetMapping("/delete")
    @Authority("ship_boxs_delete")
    public Result delete(Integer id) {
        shipBoxService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("ship_boxs_update")
    public Result update(@RequestBody ShipBox shipBox) {
        shipBoxService.update(shipBox);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("ship_boxs_get")
    public Result detail(@PathVariable Integer id) {
        ShipBox shipBox = shipBoxService.findById(id);
        return ResultGenerator.genSuccessResult(shipBox);
    }

    @GetMapping("/list")
    @Authority("ship_boxs_list")
    public Result list(ShipBoxCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ShipBoxDto> list = shipBoxService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 打印装箱标签
     */
    @GetMapping("/boxPrint")
    @Authority("ship_boxs_boxPrint")
    public Result boxPrint(Integer shipBoxId, @User CurrentUser currentUser) {

        return ResultGenerator.genSuccessResult(boxPrint2(shipBoxId));
    }

    public String boxPrint2(Integer shipBoxId) {
        ShipBox shipBox = shipBoxService.findById(shipBoxId);
     /*   ShipBoxDetailCriteria criteria = new ShipBoxDetailCriteria();
        criteria.setShipBoxId(shipBoxId);
        List<ShipBoxDetail> shipBoxDetailList = shipBoxDetailService.findList(criteria);
        if (CollectionUtils.isEmpty(shipBoxDetailList)) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "箱子为空");
        }*/
        PrintTempInfo printTempInfo = printTempInfoService.findByWare(shipBox.getWareId(), "装箱标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到装箱标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();
        Double volume = null;
        if (shipBox.getLength() != null && shipBox.getWidth() != null && shipBox.getHeight() != null) {
            volume = shipBox.getLength() * shipBox.getWidth() * shipBox.getHeight();
        }

        PrintTempUtils.setTempData("SHIP_BILL_NO_BAR_CODE", shipBox.getShipBoxCode(), doc, "barCode");
        PrintTempUtils.setTempData("SHIP_BILL_NO", shipBox.getShipBoxCode(), doc, "text");
        PrintTempUtils.setTempData("SEND_ADDRESS", shipBox.getAddress(), doc, "text");//寄送地址
        PrintTempUtils.setTempData("SHIP_NAME", shipBox.getShipName(), doc, "text");//渠道名称
        PrintTempUtils.setTempData("PACK_NO", shipBox.getPackNo(), doc, "text");//包裹件数
        PrintTempUtils.setTempData("CREATE_TIME", shipBox.getCreateTime(), doc, "text");//封箱时间
        PrintTempUtils.setTempData("BOX_WEIGHT", shipBox.getWeight().intValue(), doc, "text");//重量
        PrintTempUtils.setTempData("BOX_VOLUME", volume.intValue(), doc, "text");//体积
        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return tempHtml;
    }
}
