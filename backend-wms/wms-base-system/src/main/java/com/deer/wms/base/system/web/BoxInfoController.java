package com.deer.wms.base.system.web;

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

import com.deer.wms.base.system.model.BoxInfo;
import com.deer.wms.base.system.model.BoxInfoCriteria;
import com.deer.wms.base.system.service.BoxInfoService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2021/03/17.
 * 拣货容器
 */
@RestController
@RequestMapping("/box/infos")
public class BoxInfoController {

    @Autowired
    private BoxInfoService boxInfoService;

    @PostMapping("/add")
    @Authority("box_infos_add")
    public Result add(@RequestBody BoxInfo boxInfo) {

        BoxInfoCriteria boxInfoCriteria = new BoxInfoCriteria();
        boxInfoCriteria.setWareId(boxInfo.getWareId());
        boxInfoCriteria.setBoxCode(boxInfo.getBoxCode());
        List<BoxInfo> list = boxInfoService.findList(boxInfoCriteria);
        if (list.size() > 0) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "编码重复");
        }
        boxInfoService.save(boxInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("box_infos_delete")
    public Result delete(Integer id) {
        boxInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("box_infos_update")
    public Result update(@RequestBody BoxInfo boxInfo) {
        boxInfoService.update(boxInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("box_infos_get")
    public Result detail(@PathVariable Integer id) {
        BoxInfo boxInfo = boxInfoService.findById(id);
        return ResultGenerator.genSuccessResult(boxInfo);
    }

    @GetMapping("/list")
    @Authority("box_infos_list")
    public Result list(BoxInfoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<BoxInfo> list = boxInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Autowired
    private PrintTempInfoService printTempInfoService;

    /**
     * 打印
     */
    @GetMapping("/print")
    @Authority("box_infos_print")
    public Result print(String boxCode, Integer wareId, @User CurrentUser currentUser) {

        BoxInfo boxInfo = boxInfoService.findByBoxCode(boxCode,null, wareId);

        if(boxInfo==null){
            throw  new ServiceException(CommonCode.SERVICE_ERROR,"容器编码无效");
        }

        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "二维码/条形码");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到二维码/条形码模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();
        PrintTempUtils.setTempData("CODE", boxInfo.getBoxCode(), doc, "text");
        PrintTempUtils.setTempData("QR_CODE", boxInfo.getBoxCode(), doc, "qrCode");// TODO xuesinuo 拣货容器打印，没有模板
        PrintTempUtils.setTempData("BAR_CODE", boxInfo.getBoxCode(), doc, "barCode");


        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return ResultGenerator.genSuccessResult(tempHtml);
    }

}
