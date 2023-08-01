package com.deer.wms.ASN.manage.web;

import com.deer.wms.ASN.manage.model.AcceptEx;
import com.deer.wms.ASN.manage.model.AcceptExCriteria;
import com.deer.wms.ASN.manage.model.AcceptExDto;
import com.deer.wms.ASN.manage.model.AcceptExImg;
import com.deer.wms.ASN.manage.model.asn.AsnDetail;
import com.deer.wms.ASN.manage.service.AcceptExImgService;
import com.deer.wms.ASN.manage.service.AcceptExService;
import com.deer.wms.ASN.manage.service.AsnDetailService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by  on 2021/01/02
 * 异常入库信息
 */
@RestController
@RequestMapping("/accept/exs")
public class AcceptExController {

    @Autowired
    private AcceptExService acceptExService;

    @Autowired
    private AcceptExImgService acceptExImgService;

    @Autowired
    private AsnDetailService asnDetailService;

    @PostMapping("/add")
    @Authority("accept_exs_add")
    public Result add(@RequestBody AcceptEx acceptEx, @User CurrentUser currentUser) {
        acceptEx.setCreateUserId(currentUser.getUserId());
        acceptEx.setCreateUserName(currentUser.getUserName());
        acceptExService.save(acceptEx);
        String billDetailNo = acceptEx.getBillDetailNo();
        AsnDetail asnDetail = asnDetailService.findBy("detailNo", billDetailNo);
        asnDetail.setExState(1);
        asnDetailService.update(asnDetail);
        List<AcceptExImg> list = acceptExImgService.findByShipBillCode2(acceptEx.getShipBillNo());
        for (AcceptExImg img : list
        ) {
            img.setAcceptExId(acceptEx.getAcceptExId());
            acceptExImgService.update(img);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("accept_exs_delete")
    public Result delete(Integer id) {
        acceptExService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("accept_exs_update")
    public Result update(@RequestBody AcceptEx acceptEx) {
        acceptExService.update(acceptEx);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("accept_exs_get")
    public Result detail(@PathVariable Integer id) {
        AcceptEx acceptEx = acceptExService.findById(id);
        return ResultGenerator.genSuccessResult(acceptEx);
    }

    @GetMapping("/list")
    @Authority("accept_exs_list")
    public Result list(AcceptExCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AcceptExDto> list = acceptExService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
