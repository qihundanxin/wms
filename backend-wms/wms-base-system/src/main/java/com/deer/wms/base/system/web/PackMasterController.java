package com.deer.wms.base.system.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.base.system.model.Pack.InsertData;
import com.deer.wms.base.system.model.Pack.PackDetail;
import com.deer.wms.base.system.model.Pack.PackMaster;
import com.deer.wms.base.system.model.Pack.PackMasterCriteria;
import com.deer.wms.base.system.model.Pack.PackMasterDto;
import com.deer.wms.base.system.service.PackDetailService;
import com.deer.wms.base.system.service.PackMasterService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.util.DateUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 包装master信息api接口
 * 
 * Created by guo on 2019/11/18.
 */
@RestController
@RequestMapping("/pack/masters")
public class PackMasterController {

    @Autowired
    private PackMasterService packMasterService;

    @Autowired
    private PackDetailService packDetailService;

    /**
     * 新增packMaster以及packMaster列表
     */
    @PostMapping("/insert")
    @Authority("packs_masters_insert")
    public Result add(@RequestBody InsertData insertData, @User CurrentUser currentUser) {
        PackMasterCriteria criteria = new PackMasterCriteria();
        String packCode = insertData.getPackMaster().getPackCode();
        criteria.setPackCode(packCode);
        List<PackMasterDto> list = packMasterService.findList(criteria);
        if (list.size() == 0) {
            PackMaster packMaster = insertData.getPackMaster();
            String nowDate = DateUtils.getNowDateTimeString();
//        String packCode ="BZ" + "-"+ RandomNo.createNo();
            packMaster.setCreateTime(nowDate);
            packMasterService.save(packMaster);

            /**
             * 新增packDetail列表
             * */

            List<PackDetail> packDetails = insertData.getPackDetails();
            for (PackDetail packDetail : packDetails) {
                packDetail.setPackCode(packCode);

                packDetailService.save(packDetail);
            }
        } else {
            return ResultGenerator.genSuccessResult(CommonCode.PACK_CODE_EXISTS, null);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除packMaster信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("packs_masters_delete")
    public Result delete(String packCode, @User CurrentUser currentUser) {
        packMasterService.deleteByCode(packCode);
        packDetailService.deleteByCode(packCode);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新packMaster信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("packs_masters_update")
    public Result update(@RequestBody InsertData insertData, @User CurrentUser currentUser) {
        List<PackMasterDto> list = packMasterService.findByPackCode(insertData.getPackMaster().getPackCode());
        if (list.size() == 0 || list.size() == 1 && list.get(0).getPackCode().equals(insertData.getPackMaster().getPackCode())) {
            PackMaster packMaster = insertData.getPackMaster();
            packMasterService.update(packMaster);
            List<PackDetail> packDetails = insertData.getPackDetails();
            for (PackDetail packDetail : packDetails) {
                packDetailService.update(packDetail);
            }
        } else {
            return ResultGenerator.genSuccessResult(CommonCode.PACK_CODE_EXISTS, null);
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("packs_masters_detail")
    public Result detail(Integer id) {
        PackMaster packMaster = packMasterService.findById(id);
        return ResultGenerator.genSuccessResult(packMaster);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("packs_masters_list")
    public Result list(PackMasterCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<PackMasterDto> list = packMasterService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
