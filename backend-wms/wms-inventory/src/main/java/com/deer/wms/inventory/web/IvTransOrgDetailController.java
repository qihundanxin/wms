package com.deer.wms.inventory.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.inventory.model.IvTransOrgDetail;
import com.deer.wms.inventory.model.IvTransOrgDetailCriteria;
import com.deer.wms.inventory.service.IvTransOrgDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by ll on 2020/03/13.
 *
 * 货权转移详情
 */
@RestController
@RequestMapping("/iv/trans/org/details")
public class IvTransOrgDetailController {

    @Autowired
    private IvTransOrgDetailService ivTransOrgDetailService;

    @PostMapping
    @Authority("iv_trans_org_details_add")
    public Result add(@RequestBody IvTransOrgDetail ivTransOrgDetail) {
        ivTransOrgDetailService.save(ivTransOrgDetail);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("iv_trans_org_details_delete")
    public Result delete(@PathVariable Integer id) {
        ivTransOrgDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("iv_trans_org_details_update")
    public Result update(@RequestBody IvTransOrgDetail ivTransOrgDetail) {
        ivTransOrgDetailService.update(ivTransOrgDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("iv_trans_org_details_get")
    public Result detail(@PathVariable Integer id) {
        IvTransOrgDetail ivTransOrgDetail = ivTransOrgDetailService.findById(id);
        return ResultGenerator.genSuccessResult(ivTransOrgDetail);
    }

    @PostMapping("/list")
    @Authority("iv_trans_org_details_list")
    public Result list(@RequestBody IvTransOrgDetailCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<IvTransOrgDetail> list = ivTransOrgDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
