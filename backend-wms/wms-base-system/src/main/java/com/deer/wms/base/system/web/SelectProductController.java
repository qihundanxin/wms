package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.SelectProduct;
import com.deer.wms.base.system.model.SelectProductCriteria;
import com.deer.wms.base.system.model.SelectProductModel;
import com.deer.wms.base.system.service.SelectProductService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.ExcelUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2020/12/19.
 */
@RestController
@RequestMapping("/select/products")
public class SelectProductController {

    @Autowired
    private SelectProductService selectProductService;

    @PostMapping("/add")
    @Authority("select_products_add")
    public Result add(@RequestBody SelectProduct selectProduct) {
        selectProductService.save(selectProduct);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("select_products_delete")
    public Result delete(Integer id) {
        selectProductService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("select_products_update")
    public Result update(@RequestBody SelectProduct selectProduct) {
        selectProductService.update(selectProduct);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("select_products_get")
    public Result detail(@PathVariable Integer id) {
        SelectProduct selectProduct = selectProductService.findById(id);
        return ResultGenerator.genSuccessResult(selectProduct);
    }

    @GetMapping("/list")
    @Authority("select_products_list")
    public Result list(SelectProductCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SelectProduct> list = selectProductService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/importData")
    @Authority("select_products_importData")
    public Result importData(SelectProductModel model) {
        jxl.Workbook billWorkbook = null;
        String msg = "Excel 内容有问题，无法导入！";
        List<SelectProduct> list = new ArrayList<SelectProduct>();
        try {
            billWorkbook = jxl.Workbook.getWorkbook(model.getModel().getInputStream());
            jxl.Sheet billSheet = billWorkbook.getSheet(0);

            int rows = billSheet.getRows();

            Integer rowCount = 0;
            for (int i = 1; i < rows; i++) {
                String content = ExcelUtils.getCellString(billSheet, 1, i).trim();
                if (content == null || content == "") {
                    break;
                }
                rowCount++;
            }

            for (int i = 1; i < rowCount + 1; i++) {
                String target_keyword = ExcelUtils.getCellString(billSheet, 1, i);
                String paid_order_count = ExcelUtils.getCellString(billSheet, 3, i).trim();
                String click_through_rate = ExcelUtils.getCellString(billSheet, 10, i).trim();
                String l = ExcelUtils.getCellString(billSheet, 11, i).trim();
                String search_popularity = ExcelUtils.getCellString(billSheet, 12, i).trim();
                String payRate = ExcelUtils.getCellString(billSheet, 15, i).trim();

                Double click_through_rate2 = Double.parseDouble(click_through_rate);
                Double search_popularity2 = Double.parseDouble(search_popularity);
                Double paid_order_count2 = Double.parseDouble(paid_order_count);
                Double payRate2 = Double.parseDouble(payRate);

                SelectProduct selectProduct = new SelectProduct();
                selectProduct.setTargetKeyword(target_keyword);
                selectProduct.setPaidOrderCount(paid_order_count2);
                selectProduct.setClickThroughRate(click_through_rate2);
                selectProduct.setL(l);
                selectProduct.setPaymentRate(payRate2);
                selectProduct.setSearchPopularity(search_popularity2);
                if (model.getSelectType() == 1 && search_popularity2 > model.getM4000() && click_through_rate2 < model.getClick20() && fasdf(model.getKeywords(), target_keyword)) {
                    list.add(selectProduct);
                }

                //点击率倒数10
                else if (model.getSelectType() == 2 && search_popularity2 > model.getM4000() && fasdf(model.getKeywords(), target_keyword)) {
                    selectProductService.save(selectProduct);
                }
                //m/d前600
                else if (model.getSelectType() == 3 && (search_popularity2 / paid_order_count2) > model.getMd600() && search_popularity2 > model.getM4000() && fasdf(model.getKeywords(), target_keyword)) {
                    list.add(selectProduct);
                }
                //支付率倒数10
                else if (model.getSelectType() == 4 && search_popularity2 > model.getM4000() && fasdf(model.getKeywords(), target_keyword)) {
                    selectProductService.save(selectProduct);
                }
            }

            //点击率倒数10
            if (model.getSelectType() == 2) {

                list = selectProductService.selectBy(" order by click_through_rate limit " + model.getClick10().intValue());
            }
            //m/d前600
            else if (model.getSelectType() == 3) {
               /* list=selectProductService.selectBy("where " +
                        "search_popularity/paid_order_count  >"  + model.getMd600().intValue());*/
            }
            //支付率倒数10
            else if (model.getSelectType() == 4) {
                list = selectProductService.selectBy("order by " +
                        "payment_rate  " +
                        "limit    " + model.getPayRate10().intValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(CommonCode.WRONG_EXCEL_CONTENT, msg);

        } finally {
            billWorkbook.close();
        }

        selectProductService.deleteAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    private static Boolean fasdf(String keyword, String bbb) {

        String[] keywordList = keyword.split("");
        for (String aaa : keywordList) {
            if (aaa.equals(bbb) && !aaa.equals("")) {
                return false;
            }
        }
        return true;
    }

}
