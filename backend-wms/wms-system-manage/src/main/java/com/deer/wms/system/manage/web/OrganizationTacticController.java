package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.organization.OrganizationTactic;
import com.deer.wms.system.manage.model.organization.OrganizationTacticCriteria;
import com.deer.wms.system.manage.model.organization.OrganizationTacticDto;
import com.deer.wms.system.manage.service.OrganizationTacticService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 货主仓库上架策略关联信息api接口
 * 
 * Created by guo on 2019/12/31.
 */
@RestController
@RequestMapping("/organization/tactics")
public class OrganizationTacticController {

    @Autowired
    private OrganizationTacticService organizationTacticService;

    @PostMapping("/insert")
    @Authority("organizations_tactics_insert")
    public Result add(@RequestBody OrganizationTactic organizationTactic) {
        organizationTacticService.save(organizationTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("organizations_tactics_delete")
    public Result delete(Integer id) {
        organizationTacticService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("organizations_tactics_update")
    public Result update(@RequestBody OrganizationTactic organizationTactic) {
        organizationTacticService.update(organizationTactic);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("organizations_tactics_detail")
    public Result detail(@PathVariable Integer id) {
        OrganizationTactic organizationTactic = organizationTacticService.findById(id);
        return ResultGenerator.genSuccessResult(organizationTactic);
    }

    @GetMapping("/list")
    @Authority("organizations_tactics_list")
    public Result list(OrganizationTacticCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<OrganizationTacticDto> list = organizationTacticService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
