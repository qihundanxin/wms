package com.deer.wms.review.manage.web;

import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.review.manage.model.CaseUser;
import com.deer.wms.review.manage.model.CaseUserCriteria;
import com.deer.wms.review.manage.service.CaseUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;

import java.util.List;

/**
 * Created by guo on 2019/11/19.
 *
 * 审核人
 */
@RestController
@RequestMapping("/case/users")
public class CaseUserController {

    @Autowired
    private CaseUserService caseUserService;

    @PostMapping
    @Authority("case_users_add")
    public Result add(@RequestBody CaseUser caseUser) {
        caseUserService.save(caseUser);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @Authority("case_users_delete")
    public Result delete(@PathVariable Integer id) {
        caseUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @Authority("case_users_update")
    public Result update(@RequestBody CaseUser caseUser) {
        caseUserService.update(caseUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("case_users_get")
    public Result detail(@PathVariable Integer id) {
        CaseUser caseUser = caseUserService.findById(id);
        return ResultGenerator.genSuccessResult(caseUser);
    }

    @GetMapping
    @Authority("case_users_list")
    public Result list(CaseUserCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<CaseUser> list = caseUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
