package com.deer.wms.base.system.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Supplier.SupplierManage;
import com.deer.wms.base.system.model.Supplier.SupplierManageCriteria;
import com.deer.wms.base.system.model.Supplier.SupplierManageDto;
import com.deer.wms.base.system.service.SupplierManageService;
import com.deer.wms.intercept.annotation.Authority;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.constant.CacheTime;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 供应商信息api接口
 * 
 * Created by guo on 2019/11/21.
 */
@RestController
@RequestMapping("/supplier/manages")
public class SupplierManageController {

    @Autowired
    private SupplierManageService supplierManageService;

    @Autowired
    private CacheTool cache;

    /**
     * 新增供应商信息
     */
    @PostMapping("/insert")
    @Authority("supplier_manages_insert")
    public Result add(@RequestBody SupplierManage supplierManage, @User CurrentUser currentUser) {
        List<SupplierManageDto> list = supplierManageService.findByCode(supplierManage.getSupplierCode());
        List<SupplierManageDto> list1 = supplierManageService.findByName(supplierManage.getSupplierName());
        if (list.size() == 0 && list1.size() == 0) {
            String nowDate = DateUtils.getNowDateTimeString();
            supplierManage.setCreateTime(nowDate);
            supplierManageService.save(supplierManage);
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.SUPPLIER_EXISTS, "此供应商已存在");
        } else {
            throw new ServiceException(CommonCode.SUPPLIER_CODE_EXISTS, "此供应商编码已存在");
        }
        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除供应商信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("supplier_manages_delete")
    public Result delete(String supplierCode, @User CurrentUser currentUser) {
        supplierManageService.deleteBySupplierCode(supplierCode);
        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新供应商信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("supplier_manages_update")
    public Result update(@RequestBody SupplierManage supplierManage, @User CurrentUser currentUser) {
        List<SupplierManageDto> list = supplierManageService.findByCode(supplierManage.getSupplierCode());
        if (list.size() == 0) {
            String nowDate = DateUtils.getNowDateTimeString();
            supplierManage.setCreateTime(nowDate);
            supplierManageService.update(supplierManage);
        } else if (list.size() == 1) {
            if (list.get(0).getSupplierId().equals(supplierManage.getSupplierId())) {
                List<SupplierManageDto> list1 = supplierManageService.findByName(supplierManage.getSupplierName());
                if (list1.size() == 0) {
                    String nowDate = DateUtils.getNowDateTimeString();
                    supplierManage.setCreateTime(nowDate);
                    supplierManageService.update(supplierManage);
                } else if (list1.size() == 1 && list1.get(0).getSupplierId().equals(supplierManage.getSupplierId())) {
                    String nowDate = DateUtils.getNowDateTimeString();
                    supplierManage.setCreateTime(nowDate);
                    supplierManageService.update(supplierManage);
                } else {
                    throw new ServiceException(CommonCode.SUPPLIER_EXISTS, "此供应商已存在");
                }

            } else if (!list.get(0).getSupplierId().equals(supplierManage.getSupplierId())) {

                throw new ServiceException(CommonCode.SUPPLIER_CODE_EXISTS, "此供应商编码已存在");
            }
        }

        cache.del(CacheKey.ALL_SUPPLIER_MANAGE);

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("supplier_manages_detail")
    public Result detail(Integer id) {
        SupplierManage supplierManage = supplierManageService.findById(id);
        return ResultGenerator.genSuccessResult(supplierManage);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("supplier_manages_list")
    public Result list(SupplierManageCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SupplierManageDto> list = supplierManageService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/hasAsn")
    @Authority("supplier_manages_hasAsn")
    public Result hasAsn(SupplierManageCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<SupplierManageDto> list = supplierManageService.hasAsn(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/allList")
    @Authority("supplier_manages_allList")
    public Result allList(@User CurrentUser currentUser) {

        List<SupplierManage> list = cache.getObject(CacheKey.ALL_SUPPLIER_MANAGE, null);
        if (list == null || list.size() == 0) {
            list = supplierManageService.findAll();
            cache.setObject(CacheKey.ALL_SUPPLIER_MANAGE, null, list, CacheTime.ALL_SUPPLIER_MANAGE);
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
