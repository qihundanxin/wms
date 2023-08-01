package com.deer.wms.base.system.web;

import com.deer.wms.base.system.model.Item.InventorySafety;
import com.deer.wms.base.system.model.Item.InventorySafetyCriteria;
import com.deer.wms.base.system.model.Item.InventorySafetyInsert;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.service.InventorySafetyService;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.system.manage.model.organization.Organization;
import com.deer.wms.system.manage.service.OrganizationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ll on 2020/03/03.
 */
@RestController
@RequestMapping("/inventory/safetys")
public class InventorySafetyController {

    @Autowired
    private InventorySafetyService inventorySafetyService;
    @Autowired
    private PackDetailService packDetailService;
    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    @Authority("inventory_safetys_add")
    public Result add(@RequestBody InventorySafety inventorySafety) {
        PackDetail packDetail = packDetailService.findById(inventorySafety.getPackDetailId());
        inventorySafety.setPackDescribe(packDetail.getPackDescribe());
        Organization organization = organizationService.findById(inventorySafety.getOrganizationId());
        inventorySafety.setOrganizationName(organization.getOrganizationName());
        inventorySafetyService.save(inventorySafety);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/batchAdd")
    @Authority("inventory_safetys_batchAdd")
    public Result batchAdd(@RequestBody InventorySafetyInsert insert) {
        for (InventorySafety inventorySafety : insert.getInventorySafeties()) {
            inventorySafetyService.save(inventorySafety);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/delete")
    @Authority("inventory_safetys_delete")
    public Result delete(Integer id) {
        inventorySafetyService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @Authority("inventory_safetys_update")
    public Result update(@RequestBody InventorySafety inventorySafety) {
        Organization organization = organizationService.findById(inventorySafety.getOrganizationId());
        inventorySafety.setOrganizationName(organization.getOrganizationName());
        inventorySafetyService.update(inventorySafety);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/{id}")
    @Authority("inventory_safetys_get")
    public Result detail(@PathVariable Integer id) {
        InventorySafety inventorySafety = inventorySafetyService.findById(id);
        return ResultGenerator.genSuccessResult(inventorySafety);
    }

    @GetMapping
    @Authority("inventory_safetys_list")
    public Result list(InventorySafetyCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<InventorySafety> list = inventorySafetyService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
