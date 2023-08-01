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
import com.deer.wms.system.manage.model.ware.WareUser;
import com.deer.wms.system.manage.model.ware.WareUserCriteria;
import com.deer.wms.system.manage.model.ware.WareUserDto;
import com.deer.wms.system.manage.service.WareUserService;

/**
 * 用户仓库关联信息api接口
 * 
 * Created by guo on 2019/11/05.
 */
@RestController
@RequestMapping("/ware/users")
public class WareUserController {

    @Autowired
    private WareUserService wareUserService;

    @PostMapping("/insert")
    @Authority("ware_users_insert")
    public Result add(@RequestBody WareUser wareUser) {
        wareUserService.save(wareUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("ware_users_delete")
    public Result delete(Integer wareUserId) {
        wareUserService.deleteById(wareUserId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("ware_users_update")
    public Result update(@RequestBody WareUser wareUser) {
        wareUserService.update(wareUser);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("ware_users_get")
    public Result detail(@PathVariable Integer id) {
        WareUser wareUser = wareUserService.findById(id);
        return ResultGenerator.genSuccessResult(wareUser);
    }

    @GetMapping("/list")
    @Authority("ware_users_list")
    public Result list(WareUserCriteria criteria) {
        List<WareUserDto> list = wareUserService.findList(criteria);
        return ResultGenerator.genSuccessResult(list);
    }

}
