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

import com.deer.wms.base.system.model.Pack.Pack;
import com.deer.wms.base.system.model.Pack.PackCriteria;
import com.deer.wms.base.system.model.Pack.PackDetailDto;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.PackService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 包装材料信息api接口
 * 
 * Created by guo on 2019/11/18.
 */
@RestController
@RequestMapping("/packs")
public class PackController {

    @Autowired
    private PackService packService;

    @Autowired
    private PackDetailService packDetailService;

    /**
     * 新增pack以及pack列表
     */
    @PostMapping("/insert")
    @Authority("packs_insert")
    public Result add(@RequestBody Pack pack, @User CurrentUser currentUser) {
        List<Pack> list = packService.findByPackData(pack.getPackData());
        if (list.size() != 0) {
            throw new ServiceException(CommonCode.PACK_DATA_EXISTS, "此包材名称已存在");
        } else {
            String nowDate = DateUtils.getNowDateTimeString();
            pack.setCreateTime(nowDate);
            packService.save(pack);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除pack信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("packs_delete")
    public Result delete(Integer id) {
        List<PackDetailDto> list = packDetailService.findByPackId(id);
        if (list.size() == 0) {
            packService.deleteById(id);
        } else {
            throw new ServiceException(CommonCode.PACK_DATA_EXISTS, "此包材名称正在使用");
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新pack信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("packs_update")
    public Result update(@RequestBody Pack pack) {
        Pack pack1 = packService.findById(pack.getId());
        if (!pack1.equals(null)) {
            packService.update(pack);
        } else {
            throw new ServiceException(CommonCode.PACK_DATA_EXISTS, "此包材不存在");
        }

        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("packs_detail")
    public Result detail(@PathVariable Integer id) {
        Pack pack = packService.findById(id);
        return ResultGenerator.genSuccessResult(pack);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("packs_list")
    public Result list(PackCriteria criteria) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<Pack> list = packService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
