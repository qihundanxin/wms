package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
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

import com.deer.wms.base.system.model.PrinterInfo;
import com.deer.wms.base.system.model.PrinterInfoCriteria;
import com.deer.wms.base.system.service.PrinterInfoService;
import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* Created by  on 2021/08/25.
*/
@RestController
@RequestMapping("/printer/infos")
public class PrinterInfoController {

    @Autowired
    private PrinterInfoService printerInfoService;

    @PostMapping("/add")
    @Authority("printer_infos_add")
    public Result add(@RequestBody PrinterInfo printerInfo) {
        printerInfoService.save(printerInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("printer_infos_delete")
    public Result delete(  Integer id) {
        printerInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("printer_infos_update")
    public Result update(@RequestBody PrinterInfo printerInfo) {
        printerInfoService.update(printerInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("printer_infos_get")
    public Result detail(@PathVariable Integer id) {
        PrinterInfo printerInfo = printerInfoService.findById(id);
        return ResultGenerator.genSuccessResult(printerInfo);
    }

    @GetMapping("/list")
    @Authority("printer_infos_list")
    public Result list(PrinterInfoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PrinterInfo> list = printerInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findPdaDefault")
    @Authority("printer_infos_findPdaDefault")
    public Result findPdaDefault(Integer wareId ) {
         PrinterInfo  list = printerInfoService.findPdaDefault(wareId);
        return ResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/changePda")
    @Authority("printer_infos_changePda")
    public Result findPdaDefault( PrinterInfo printerInfo) {
        printerInfoService.changePda(printerInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/getPda")
    @Authority("printer_infos_getPda")
    public Result getPda( Integer wareId) {
        PrinterInfo printerInfo=printerInfoService.findPdaDefault(wareId);
        return ResultGenerator.genSuccessResult(printerInfo);
    }

    @Autowired
    private PrintTempInfoService printTempInfoService;

    @GetMapping("/print")
    @Authority("printer_infos_print")
    public Result print(Integer id,Integer wareId) {

        PrinterInfo printerInfo = printerInfoService.findById(id);

        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "二维码/条形码");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();
        String aaa = printerInfo.getIp()
                +"%" +printerInfo.getPrinterIndex()
                +"%" +printerInfo.getPage()
                +"%" +printerInfo.getFx()
                +"%" +printerInfo.getDs();
        PrintTempUtils.setTempData("QR_CODE", aaa, doc, "qrCode");// TODO xuesinuo 打印机设置，打印测试页，没有模板
        PrintTempUtils.setTempData("BAR_CODE", aaa, doc, "barCode");
        PrintTempUtils.setTempData("CODE", aaa, doc, "text");

        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return ResultGenerator.genSuccessResult(tempHtml);
    }

}
