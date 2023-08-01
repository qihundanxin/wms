package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Pack.PackDetailCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 包装detail信息api接口
 * 
 * Created by guo on 2019/11/18.
 */
@RestController
@RequestMapping("/pack/details")
public class PackDetailController {

    @Autowired
    private PackDetailService packDetailService;

    /**
     * 新增packDetail以及packDetail列表
     */
    @PostMapping("/insert")
    @Authority("packs_details_insert")
    public Result add(@RequestBody PackDetail packDetail, @User CurrentUser currentUser) {
        packDetailService.save(packDetail);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除packDetail信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("packs_details_delete")
    public Result delete(Integer id) {
        packDetailService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新packMaster信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("packs_details_update")
    public Result update(@RequestBody PackDetail packDetail, @User CurrentUser currentUser) {
        packDetailService.update(packDetail);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("packs_details_detail")
    public Result detail(@PathVariable Integer id) {
        PackDetail packDetail = packDetailService.findById(id);
        return ResultGenerator.genSuccessResult(packDetail);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("packs_details_list")
    public Result list(PackDetailCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        criteria.setState(1);
        List<PackDetailDto> list = packDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/findByPackCode")
    @Authority("packs_details_findByPackCode")
    public Result findByPackCode(PackDetail packDetail, @User CurrentUser currentUser) {

        List<PackDetailDto> list = packDetailService.findByPackCode(packDetail.getPackCode());
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/list2")
    @Authority("packs_details_list2")
    public Result list2(PackDetailCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PackDetailDto> list = packDetailService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
