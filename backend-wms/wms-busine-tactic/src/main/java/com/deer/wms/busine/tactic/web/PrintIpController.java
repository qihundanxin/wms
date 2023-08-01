package com.deer.wms.busine.tactic.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.PrintDetail;
import com.deer.wms.busine.tactic.model.PrintInsert;
import com.deer.wms.busine.tactic.model.PrintIp;
import com.deer.wms.busine.tactic.model.PrintIpCriteria;
import com.deer.wms.busine.tactic.model.PrintIpDto;
import com.deer.wms.busine.tactic.service.PrintDetailService;
import com.deer.wms.busine.tactic.service.PrintIpService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* Created by  on 2021/07/30.
*/
@RestController
@RequestMapping("/print/ips")
public class PrintIpController {

    @Autowired
    private PrintIpService printIpService;

    @Autowired
    private PrintDetailService printDetailService;

    @PostMapping("/insert")
    @Authority("print_ips_insert")
    public Result add(@RequestBody PrintInsert printInsert, @User CurrentUser currentUser) {

        PrintIp printIp = printInsert.getPrintIp();
        if(printIp.getPrintIpId()==null){
            String nowDate = DateUtils.getNowDateTimeString();
            printIp.setCreateTime(nowDate);
            printIp.setCreateUserName(currentUser.getUserName());
            printIp.setCreateUserId(currentUser.getUserId());
            printIpService.save(printIp);
            List<PrintDetail> list = printInsert.getList();
            for (PrintDetail printDetail:list  ) {
                printDetail.setPrintIp(printIp.getIp());
                printDetail.setPrintIpId(printIp.getPrintIpId());
                printDetailService.save(printDetail);
            }
        }else {
            String nowDate = DateUtils.getNowDateTimeString();
            printIp.setUpdateTime(nowDate);
            printIp.setUpdateUserName(currentUser.getUserName());
            printIp.setUpdateUserId(currentUser.getUserId());
            printIpService.update(printIp);
            List<PrintDetail> list = printInsert.getList();
            for (PrintDetail printDetail:list  ) {
                printDetail.setPrintIp(printIp.getIp());
                printDetail.setPrintIpId(printIp.getPrintIpId());
                printDetailService.update(printDetail);
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/add")
    @Authority("print_ips_add")
    public Result add(@RequestBody PrintIp printIp) {
        printIpService.save(printIp);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("print_ips_delete")
    public Result delete(  Integer id) {
        printIpService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("print_ips_update")
    public Result update(@RequestBody PrintIp printIp) {
        printIpService.update(printIp);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("print_ips_detail")
    public Result detail( Integer id) {
        PrintIpDto printIp = printIpService.findByIPId(id);
        return ResultGenerator.genSuccessResult(printIp);
    }

    @GetMapping("/list")
    @Authority("print_ips_list")
    public Result list(PrintIpCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PrintIpDto> list = printIpService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
