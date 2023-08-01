package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.PrintTempInfo;
import com.deer.wms.busine.tactic.model.PrintTempInfoCriteria;
import com.deer.wms.busine.tactic.service.PrintTempInfoService;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.BarCodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Created by  on 2020/11/13.
 *
 * 打印模板信息
 */
@RestController
@RequestMapping("/print/temp/infos")
public class PrintTempInfoController {

    @Autowired
    private PrintTempInfoService printTempInfoService;

    @PostMapping("/add")
    @Authority("print_temp_infos_add")
    public Result add(@RequestBody PrintTempInfo printTempInfo) {
        if (printTempInfo.getTempId() != null) {
            printTempInfoService.update(printTempInfo);
        } else {

            printTempInfoService.save(printTempInfo);
        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("print_temp_infos_delete")
    public Result delete(Integer id) {
        printTempInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("print_temp_infos_update")
    public Result update(@RequestBody PrintTempInfo printTempInfo) {
        printTempInfoService.update(printTempInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("print_temp_infos_detail")
    public Result detail(Integer id) {
        PrintTempInfo printTempInfo = printTempInfoService.findById(id);
        return ResultGenerator.genSuccessResult(printTempInfo);
    }

    @GetMapping("/getPrintData")
    @Authority("print_temp_infos_getPrintData")
    public Result getPrintData(Integer id) {
        PrintTempInfo printTempInfo = printTempInfoService.findById(3);
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        if (doc.getElementById("ORDER_SHOP_NAME") != null) {
            List<Element> aaa = doc.getElementsByAttributeValue("id", "ORDER_SHOP_NAME");
            for (Element ele : aaa
            ) {
                ele.text("郭靖勋");
            }
            // doc.getElementById("ORDER_SHOP_NAME").text("郭靖勋");
        }

        if (doc.getElementById("EUB_RECEIVER_ZIPCODE_BARCODE") != null) {
            doc.getElementById("EUB_RECEIVER_ZIPCODE_BARCODE").getElementsByTag("img").attr("src", BarCodeUtils.barCode("123456", 50D, 202D));

        }

        if (doc.getElementById("PRODUCT_STOCK_SKU_QRCODE") != null) {
            doc.getElementById("PRODUCT_STOCK_SKU_QRCODE").getElementsByTag("img").attr("src", BarCodeUtils.encode("fsadfsadf", 250, 250));

        }

        if (doc.getElementById("FULL_ITEMS_DETAIL_TABLE") != null) {
            Element FULL_ITEMS_DETAIL_TABLE = doc.getElementById("FULL_ITEMS_DETAIL_TABLE");
            Element tbody = FULL_ITEMS_DETAIL_TABLE.getElementsByTag("tBody").get(0);
            Element ITEM_LIST_DETAIL = doc.getElementById("ITEM_LIST_DETAIL");
            for (int i = 0; i < 5; i++) {
                Element newEle = ITEM_LIST_DETAIL.clone();
                newEle.attr("id", "id" + i + 1);
                if (i == 0) {
                    ITEM_LIST_DETAIL = newEle;
                } else {
                    newEle.appendTo(tbody);
                }
            }
        }

        //直线中间蓝点的问题
        if (doc.getElementsByAttributeValue("class", "line-handle") != null) {
            List<Element> aaa = doc.getElementsByAttributeValue("class", "line-handle");
            for (Element ele : aaa
            ) {
                ele.attr("class", ".dis-none");
            }
            // doc.getElementById("ORDER_SHOP_NAME").text("郭靖勋");
        }
        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();
        printTempInfo.setTempHtml(tempHtml);
        return ResultGenerator.genSuccessResult(printTempInfo);
    }

    @GetMapping("/list")
    @Authority("print_temp_infos_list")
    public Result list(PrintTempInfoCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PrintTempInfo> list = printTempInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
