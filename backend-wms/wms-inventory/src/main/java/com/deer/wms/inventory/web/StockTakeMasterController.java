package com.deer.wms.inventory.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.inventory.model.stockTake.StockTakeData;
import com.deer.wms.inventory.model.stockTake.StockTakeMaster;
import com.deer.wms.inventory.model.stockTake.StockTakeMasterCriteria;
import com.deer.wms.inventory.service.InventoryService;
import com.deer.wms.inventory.service.StockTakeMasterService;
import com.deer.wms.inventory.service.StockTakeTaskService;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.review.manage.service.CaseMasterService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 盘点单api接口
 * 
 * Created by 郭靖勋 on 2020/02/06.
 */
@RestController
@RequestMapping("/stock/take/masters")
public class StockTakeMasterController {

    @Autowired
    private StockTakeMasterService stockTakeMasterService;

    @Autowired
    private CaseMasterService caseMasterService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StockTakeTaskService stockTakeTaskService;

    /**
     * 新增盘点信息
     */
    @PostMapping("/insert")
    @Authority("stock_take_masters_insert")
    public Result add(@RequestBody StockTakeData stockTakeData, @User CurrentUser currentUser) {
        stockTakeMasterService.createStockTakeMaster(stockTakeData, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 审核
     */
    @GetMapping("/review")
    @Authority("stock_take_masters_review")
    public Result review(String billNo, Integer reviewResult, Integer caseDetailId, String memo, @User CurrentUser currentUser) {
        StockTakeMaster stockTakeMaster = stockTakeMasterService.findBy("billNo", billNo);
        //-1不通过   0-流转中  1-通过  2-初始化
        Integer lastReviewResult = caseMasterService.review(caseDetailId, currentUser.getUserId(), reviewResult, memo);

        if (lastReviewResult == -1) {
            stockTakeMaster.setState(-1);
            stockTakeMasterService.update(stockTakeMaster);
        } else if (lastReviewResult == 0) {
            stockTakeMaster.setState(0);
            stockTakeMasterService.update(stockTakeMaster);
        } else if (lastReviewResult == 2) {
            stockTakeMaster.setState(2);
            stockTakeMasterService.update(stockTakeMaster);
        } else if (lastReviewResult == 1) {
            stockTakeMaster.setState(1);
            stockTakeMasterService.update(stockTakeMaster);
            // 审核后生成盘点任务
            Integer createUserId = currentUser.getUserId();
            String createUserName = currentUser.getUserName();
            String createTime = DateUtils.getNowDateTimeString();
            int takeNumber = stockTakeMasterService.saveStockTakeTasks(stockTakeMaster, billNo, createUserId, createUserName, createTime);
            if (takeNumber <= 0) {
                stockTakeMaster.setState(4);
                stockTakeMasterService.update(stockTakeMaster);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 重新发起审核
     */
    @GetMapping("/reReview")
    @Authority("stock_take_masters_reReview")
    public Result reReview(String billNo, @User CurrentUser currentUser) {
        StockTakeMaster stockTakeMaster = stockTakeMasterService.findBy("billNo", billNo);

        //初始化状态 2
        stockTakeMaster.setState(2);
        stockTakeMasterService.update(stockTakeMaster);
        caseMasterService.createCase(billNo, 1, currentUser.getUserId(), stockTakeMaster.getWareId(), stockTakeMaster.getOrganizationId());

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("stock_take_masters_delete")
    public Result delete(Integer id) {
        stockTakeMasterService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("stock_take_masters_update")
    public Result update(@RequestBody StockTakeMaster stockTakeMaster) {
        stockTakeMasterService.update(stockTakeMaster);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("stock_take_masters_detail")
    public Result detail(Integer id) {
        StockTakeMaster stockTakeMaster = stockTakeMasterService.findById(id);
        return ResultGenerator.genSuccessResult(stockTakeMaster);
    }

    @GetMapping("/list")
    @Authority("stock_take_masters_list")
    public Result list(StockTakeMasterCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<StockTakeMaster> list = stockTakeMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/deleteByIds")
    @Authority("stock_take_masters_deleteByIds")
    public Result deleteByIds(@RequestBody StockTakeMasterCriteria criteria) {
        for (Integer id : criteria.getIds()) {
            stockTakeMasterService.deleteById(id);
        }
        return ResultGenerator.genSuccessResult();
    }
}