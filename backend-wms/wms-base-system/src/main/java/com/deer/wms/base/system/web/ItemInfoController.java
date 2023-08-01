package com.deer.wms.base.system.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.deer.wms.base.system.model.Item.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.deer.wms.base.system.model.ImportModel;
import com.deer.wms.base.system.model.ItemUpc;
import com.deer.wms.base.system.service.ItemInfoLogService;
import com.deer.wms.base.system.service.ItemInfoService;
import com.deer.wms.base.system.service.ItemUpcService;
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
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.ExcelUtils;
import com.deer.wms.project.root.util.PrintTempUtils;
import com.deer.wms.project.root.util.RandomNo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by guo on 2019/11/19.
 * 产品信息
 */
@RestController
@RequestMapping("/item/infos")
@Slf4j
public class ItemInfoController {

    @Autowired
    private ItemInfoService itemInfoService;
    @Autowired
    private ItemInfoLogService itemInfoLogService;

    /**
     * 新增itemInfo以及itemInfo列表
     */
    @PostMapping("/insert")
    @Authority("item_infos_insert")
    public Result add(@RequestBody ItemInfo itemInfo, @User CurrentUser currentUser) {
        if(itemInfo.getItemCode()==null || itemInfo.getItemCode().trim().equals("")){
            Random random = new Random();
            int rannum = (int) (random.nextDouble() * (999 - 100 + 1)) + 100;
            String itemCode = "IM" + RandomNo.createNo() + rannum;
            itemInfo.setItemCode(itemCode);
        }
        // 没有携带itemtypecode  只携带id 查出来赋值
        ItemType itemType = itemInfoService.selectItemTypeCode(itemInfo.getItemTypeId());
        itemInfo.setItemTypeCode(itemType.getItemTypeCode());

        ItemInfo itemInfo1 = itemInfoService.findByCode(itemInfo.getItemCode(),itemInfo.getOrganizationId());
        if(itemInfo1!=null){
            throw new ServiceException(CommonCode.SERVICE_ERROR,"系统已存在该SKU");
        }
        itemInfoService.save(itemInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除itemInfo信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("item_infos_delete")
    public Result delete(Integer id, @User CurrentUser currentUser) {
        ItemInfo itemInfo = itemInfoService.findById(id);
        itemInfoService.deleteById(id);
        // 删除物料后后存储删除日志
        if(itemInfo!=null){
            log.debug("删除商品", itemInfo);
            ItemInfoLog itemInfoLog = new ItemInfoLog();
            itemInfoLog.setCreateTime(new Date());
            itemInfoLog.setCreateUserId(currentUser.getUserId());
            itemInfoLog.setCreateUserName(currentUser.getUserName());
            itemInfoLog.setItemInfoId(id);
            itemInfoLog.setItemCode(itemInfo.getItemCode());
            itemInfoLog.setBeforeData(JSON.toJSONString(itemInfo));
            itemInfoLog.setOperationType(4); //日志类型：删除
            itemInfoLog.setMemo("/item/infos/delete");
            log.info("追加删除商品日志", itemInfoLog);
            itemInfoLogService.save(itemInfoLog);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新itemInfo信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("item_infos_update")
    public Result update(@RequestBody ItemInfo itemInfo) {
        ItemType itemType = itemInfoService.selectItemTypeCode(itemInfo.getItemTypeId());
        itemInfo.setItemTypeCode(itemType.getItemTypeCode());
        itemInfoService.updateItem(itemInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("item_infos_detail")
    public Result detail(@PathVariable Integer id) {
        ItemInfo itemInfo = itemInfoService.findById(id);
        return ResultGenerator.genSuccessResult(itemInfo);
    }

    /**
     * pda 根据sku盲收列表查询
     * @param itemCode
     * @param organizationId
     * @return
     */
    @GetMapping("/findBYCode")
    @Authority("item_infos_findBYCode")
    public Result findBYCode(String itemCode,Integer organizationId) {
        ItemInfoDto itemInfo = itemInfoService.findByCode(itemCode,organizationId);
        return ResultGenerator.genSuccessResult(itemInfo);
    }

    /**
     * 物料查询列表
     */
    @Transactional
    @GetMapping("/list")
    @Authority("item_infos_list")
    public Result list(ItemInfoCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemInfoDto> list = itemInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 物料查询列表
     */
    @Transactional
    @GetMapping("/selectItem")
    @Authority("item_infos_selectItem")
    public Result selectItem(ItemInfoCriteria criteria, @User CurrentUser currentUser) {

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<ItemInfoDto> list = itemInfoService.selectItem(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 启用物料
     */
    @GetMapping("/enable")
    @Authority("item_infos_enable")
    public Result enable(String itemCode, @User CurrentUser currentUser) {

        itemInfoService.modifyItemState(itemCode, Constants.ITEM_ENABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 停用物料
     */
    @GetMapping("/disable")
    @Authority("item_infos_disable")
    public Result disable(String itemCode, @User CurrentUser currentUser) {

        itemInfoService.modifyItemState(itemCode, Constants.ITEM_DISABLE, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/uploadItems")
    @Transactional
    @Authority("item_infos_uploadItems")
    public Result uploadItems(ImportModel importModel) {

        try {
            InputStream is = importModel.getModel().getInputStream();
            Reader in = new InputStreamReader(is);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                ItemInfo itemInfo = itemInfoService.findBy("itemCode", record.get(1));
                if (itemInfo != null) {
                    itemInfoService.deleteById(itemInfo.getId());
                    // 增加删除日志
                    ItemInfoLog itemInfoLog = new ItemInfoLog();
                    itemInfoLog.setCreateTime(new Date());
                    itemInfoLog.setItemInfoId(itemInfo.getId());
                    itemInfoLog.setItemCode(itemInfo.getItemCode());
                    itemInfoLog.setBeforeData(JSON.toJSONString(itemInfo));
                    itemInfoLog.setOperationType(4); //日志类型：删除
                    itemInfoLog.setMemo("/item/infos/uploadItems");
                    itemInfoLogService.save(itemInfoLog);
                }
                itemInfo = new ItemInfo();
                itemInfo.setItemBarCode(record.get(0));
                itemInfo.setItemCode(record.get(1));
                itemInfo.setItemName(record.get(7));
                itemInfo.setCategoryId(record.get(2));
                itemInfo.setCategoryId2(record.get(3));
                itemInfo.setEnCategory(record.get(4));
                itemInfo.setItemName(record.get(5));
                itemInfo.setEnProduct(record.get(5));
                itemInfo.setBrand(record.get(6));
                itemInfo.setItemDesc(record.get(7));
                itemInfo.setShortDesc(record.get(8));
                itemInfo.setStock(Long.parseLong(record.get(9).trim()));
                itemInfo.setStatus(Integer.parseInt(record.get(10).trim()));
                itemInfo.setImgUrl(record.get(11));
                itemInfo.setUrl(record.get(12));
                itemInfoService.save(itemInfo);
            }

        } catch (Exception ex) {
            log.error("upload item info failed:[{}][{}]", ex.getMessage(), ex.getStackTrace());
        }

        return ResultGenerator.genSuccessResult();
    }

    @Autowired
    private ItemUpcService itemUpcService;

    @PostMapping("/uploadIUPC")
    @Authority("item_infos_uploadIUPC")
    public Result uploadIUPC(ImportModel importModel, @User CurrentUser currentUser) {

        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";

        try {
            InputStream inputStream = importModel.getModel().getInputStream();
            billWorkbook = jxl.Workbook.getWorkbook(inputStream);
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
             //   String itemId = ExcelUtils.getCellString(billSheet, 2, i).trim();
                String itemCode = ExcelUtils.getCellString(billSheet, 1, i).trim();
                String upc = ExcelUtils.getCellString(billSheet, 5, i).trim();

                ItemInfo itemInfo = itemInfoService.findBy("itemCode",itemCode);
                if(itemInfo!=null){
                    ItemUpc itemUpc  =new ItemUpc();
                    itemUpc.setCreateTime(DateUtils.getNowDateTimeString());
                    itemUpc.setUpc(upc);
//                itemUpc.setCreateUserId(currentUser.getUserId());
//                itemUpc.setCreateUserName(currentUser.getUserName());
                    itemUpc.setItemCode(itemInfo.getItemCode());
                    itemUpc.setItemId(itemInfo.getId());
                    itemUpc.setOrganizationId(itemInfo.getOrganizationId());
                    itemUpcService.save(itemUpc);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }
        return ResultGenerator.genSuccessResult();

    }

    @PostMapping("/uploadIUPC2")
    @Authority("item_infos_uploadIUPC2")
    public Result uploadIUPC2(ImportModel importModel, @User CurrentUser currentUser) {

        jxl.Workbook billWorkbook = null;

        String msg = "Excel 内容有问题，无法导入！";
        List<Map> list = new ArrayList<>();

        try {
            InputStream inputStream = importModel.getModel().getInputStream();
            billWorkbook = jxl.Workbook.getWorkbook(inputStream);
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
                //   String itemId = ExcelUtils.getCellString(billSheet, 2, i).trim();
                String name1 = ExcelUtils.getCellString(billSheet, 0, i).trim();
                String name2 = ExcelUtils.getCellString(billSheet, 1, i).trim();
                String orderCount = ExcelUtils.getCellString(billSheet, 3, i).trim();
                String itemCount = ExcelUtils.getCellString(billSheet, 13, i).trim();

                if(Integer.parseInt(orderCount)>100 && Integer.parseInt(itemCount)<2000){
                    Map map = new HashMap();
                    map.put("name1",name1);
                    map.put("name2",name2);
                    map.put("orderCount",orderCount);
                    map.put("itemCount",itemCount);
                    list.add(map);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }
        return ResultGenerator.genSuccessResult(list);

    }


    @Autowired
    private PrintTempInfoService printTempInfoService;

    /**
     * 打印
     *
     * @param itemCode
     * @return
     */
    @GetMapping("/print")
    @Authority("item_infos_print")
    public Result print(String itemCode, Integer wareId, @User CurrentUser currentUser) {

        List<ItemInfo> itemInfos = itemInfoService.findBySkuOrUpc(itemCode);

        if(itemInfos==null || itemInfos.size()==0){
            throw  new ServiceException(CommonCode.SERVICE_ERROR,"SKU无效");
        }
        ItemInfo itemInfo = itemInfos.get(0);

        PrintTempInfo printTempInfo = printTempInfoService.findByWare(wareId, "商品标签");
        if (printTempInfo == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未匹配到商品标签模板");
        }
        String html = printTempInfo.getTempHtml();
        Document doc = Jsoup.parse(html);
        //删除不显示的
        doc.select(".dis-none").remove();
        //直线中间蓝点的问题
        doc.select(".line-handle").remove();



        PrintTempUtils.setTempData("ITEM_NAME", itemInfo.getItemName(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE", itemInfo.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("ITEM_CODE_QR_CODE", itemInfo.getItemCode(), doc, "qrCode");
        PrintTempUtils.setTempData("ITEM_CODE_BAR_CODE", itemInfo.getItemCode(), doc, "barCode");
        PrintTempUtils.setTempData("SPEC", itemInfo.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("MODEL", itemInfo.getItemCode(), doc, "text");
        PrintTempUtils.setTempData("PRINT_TIME", DateUtils.getNowDateTimeString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_DATE", DateUtils.getNowDateString(), doc, "text");
        PrintTempUtils.setTempData("PRINT_USER_NAME", currentUser.getUserName(), doc, "text");


        //doc在创建时里面包含了html head body等节点  不需要
        Element labelContent = doc.getElementsByAttributeValue("class", "label-content").get(0);
        String tempHtml = labelContent.toString();

        return ResultGenerator.genSuccessResult(tempHtml);
    }

    @GetMapping("/xhcSku")
    @Authority("item_infos_xhcSku")
    public Result xhcSku() throws IOException {
        itemInfoService.xhcSku(null);
        return ResultGenerator.genSuccessResult( );
    }

    @PostMapping("/xhcSku2")
    @Authority("item_infos_xhcSku2")
    public Result xhcSku(@RequestBody TongBuSkuCriteria criteria) throws IOException {
        itemInfoService.xhcSku(criteria);
        return ResultGenerator.genSuccessResult( );
    }

}
