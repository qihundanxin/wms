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

import com.deer.wms.base.system.model.Area.AreaInfo;
import com.deer.wms.base.system.model.Area.AreaInfoCriteria;
import com.deer.wms.base.system.model.Area.AreaInfoDto;
import com.deer.wms.base.system.service.AreaInfoService;
import com.deer.wms.busine.tactic.service.UpperTacticDetailService;
import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.system.manage.model.ValidatorAreaInfoModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 货区信息api接口
 * 
 * Created by guo on 2019/11/21.
 */
@RestController
@RequestMapping("/area/infos")
public class AreaInfoController {

    @Autowired
    private AreaInfoService areaInfoService;

    @Autowired
    private UpperTacticDetailService upperTacticDetailService;

    /**
     * 新增货区信息
     */
    @PostMapping("/insert")
    @Authority("area_infos_insert")
    public Result add(@RequestBody AreaInfo areaInfo, @User CurrentUser currentUser) {
//        List<AreaInfoDto> list = areaInfoService.findByCode(areaInfo.getAreaCode());
//        List<AreaInfoDto> list1 = areaInfoService.findByName(areaInfo.getAreaName());
//        if (list.size() == 0 && list1.size() == 0) {
//            String nowDate = DateUtils.getNowDateTimeString();
//            areaInfo.setAddTime(nowDate);
//            areaInfoService.save(areaInfo);
//        } else if (list.size() == 0 && list1.size() != 0) {
//            throw new ServiceException(CommonCode.AREA_EXISTS, "此货区已存在");
//        } else {
//            throw new ServiceException(CommonCode.AREA_CODE_EXISTS, "此货区编码已存在");
//        }
        areaInfoService.saveAreaInfo(areaInfo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除货区信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("area_infos_delete")
    public Result delete(Integer id, @User CurrentUser currentUser) {
        areaInfoService.deleteByAreaId(id);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新货区信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("area_infos_update")
    public Result update(@RequestBody AreaInfo areaInfo, @User CurrentUser currentUser) {
//        List<AreaInfoDto> list = areaInfoService.findByCode(areaInfo.getAreaCode());
//        if (list.size() == 0) {
//            String nowDate = DateUtils.getNowDateTimeString();
//            areaInfo.setAddTime(nowDate);
//            areaInfoService.update(areaInfo);
//        } else if (list.size() == 1) {
//            if (list.get(0).getAreaId().equals(areaInfo.getAreaId())) {
//                List<AreaInfoDto> list1 = areaInfoService.findByName(areaInfo.getAreaName());
//                if (list1.size() == 0) {
//                    String nowDate = DateUtils.getNowDateTimeString();
//                    areaInfo.setAddTime(nowDate);
//                    areaInfoService.update(areaInfo);
//                } else if (list1.size() == 1 && list1.get(0).getAreaId().equals(areaInfo.getAreaId())) {
//                    String nowDate = DateUtils.getNowDateTimeString();
//                    areaInfo.setAddTime(nowDate);
//                    areaInfoService.update(areaInfo);
//                } else {
//                    throw new ServiceException(CommonCode.AREA_EXISTS, "此货区已存在");
//                }
//
//            } else if (!list.get(0).getAreaId().equals(areaInfo.getAreaId())) {
//
//                throw new ServiceException(CommonCode.AREA_CODE_EXISTS, "此货区编码已存在");
//            }
//        }
        areaInfoService.updateAreaInfo(areaInfo);
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("/detail")
    @Authority("area_infos_detail")
    public Result detail(Integer id) {
        AreaInfo areaInfo = areaInfoService.findById(id);
        return ResultGenerator.genSuccessResult(areaInfo);
    }

    @Transactional
    @GetMapping("/list")
    @Authority("area_infos_list")
    public Result list(AreaInfoCriteria criteria, @User CurrentUser currentUser) {
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<AreaInfoDto> list = areaInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 验证货区信息重复
     */
    @PostMapping("/validator")
    @Authority("area_infos_validator")
    public Result validatorAreaInfo(@RequestBody ValidatorAreaInfoModel model) {
        int count = areaInfoService.validatorChongfu(model);
        if (count > 0) {
            if (model.getAreaCode() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.CODE_EXISTS);
            }
            if (model.getAreaName() != null) {
                return ResultGenerator.genSuccessResult(CommonCode.NAME_EXISTS);
            }
        }
        return ResultGenerator.genSuccessResult(CommonCode.NAME_CODE_NO_EXISTS);
    }
}
