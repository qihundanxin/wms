package com.deer.wms.system.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.busine.tactic.model.Upper.UpperTactic;
import com.deer.wms.busine.tactic.service.UpperTacticService;
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
import com.deer.wms.system.manage.model.address.ShippingAddressCriteria;
import com.deer.wms.system.manage.model.address.ShippingAddressDto;
import com.deer.wms.system.manage.model.user.UserInfoDto;
import com.deer.wms.system.manage.model.ware.WareInfo;
import com.deer.wms.system.manage.model.ware.WareInfoCriteria;
import com.deer.wms.system.manage.model.ware.WareInfoDto;
import com.deer.wms.system.manage.model.ware.WareInfoDto2;
import com.deer.wms.system.manage.service.ShippingAddressService;
import com.deer.wms.system.manage.service.UserInfoService;
import com.deer.wms.system.manage.service.WareInfoService;
import com.deer.wms.system.manage.service.WareUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 仓库管理api接口
 * 
 * Created by guo on 2019/11/06.
 */
@RestController
@RequestMapping("/ware/infos")
public class WareInfoController {

    @Autowired
    private WareInfoService wareInfoService;

    @Autowired
    private WareUserService wareUserService;

    @Autowired
    private UpperTacticService upperTacticService;

    @Autowired
    private ShippingAddressService shippingAddressService;

    @Autowired
    private CacheTool cacheTool;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 添加仓库信息
     */
    @PostMapping("/insert")
    @Authority("ware_infos_insert")
    public Result add(@RequestBody WareInfoDto wareInfo, @User CurrentUser currentUser) {
        WareInfo wareInfo1 = null;
        List<WareInfoDto2> list = wareInfoService.findByName(wareInfo.getWareName());
        if (list.size() == 0) {
            wareInfo.setDutyUserId(currentUser.getUserId());
            wareInfo1 = wareInfoService.createWare(wareInfo);
        } else {
            throw new ServiceException(CommonCode.WARE_EXISTS, "此仓库已存在");
        }
        return ResultGenerator.genSuccessResult(wareInfo1);
    }

    /**
     * 删除仓库信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("ware_infos_delete")
    public Result delete(Integer wareId, @User CurrentUser currentUser) {
        if (currentUser == null) {
            throw new ServiceException(CommonCode.SERVICE_ERROR, "未登录！");
        }
        WareInfoCriteria criteria = new WareInfoCriteria();
        criteria.setWareId(wareId);
        ShippingAddressCriteria criteria1 = new ShippingAddressCriteria();
        criteria1.setWareId(wareId);
        List<ShippingAddressDto> list1 = shippingAddressService.findList(criteria1);
        if (list1.size() != 0) {
            throw new ServiceException(CommonCode.WARE_SHIPPADDRESS_BOUND, "此仓库已绑定发货地址");
        }

        List<UpperTactic> list = upperTacticService.findByWareId(wareId);
        if (list.size() != 0) {
            throw new ServiceException(CommonCode.WARE_UPPER_TACTIC_BOUND, "此仓库已绑定上架策略");
        }
        wareInfoService.deleteByCodeAndCom(criteria);
        wareUserService.deleteByWareId(wareId);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新仓库信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("ware_infos_update")
    public Result update(@RequestBody WareInfo wareInfo, @User CurrentUser currentUser) {
        wareInfoService.update(wareInfo);
//    /*
//     * 保存仓库保管员列表
//     */
//        wareUserService.deleteByWareId(wareInfo.getWareId());
//        Integer wareId = wareInfo.getWareId();
//        WareUser wareUser = new WareUser();
//        wareUser.setWareId(wareId);
//        wareUser.setCompanyId(wareInfo.getCompanyId());
//        List<Integer> userIds = wareInfoDto.getWareUserIds();
//        if (userIds != null && userIds.size() > 0) {
//            for (Integer userId : userIds) {
//                wareUser.setUserId(userId);
//                wareUser.setWareId(wareInfo.getWareId());
//                wareUser.setWareUserId(null);
//                wareUserService.save(wareUser);
//            }
//        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新仓库信息Status
     */
    @Transactional
    @PostMapping("/changeStatus")
    @Authority("ware_infos_changeStatus")
    public Result changeStatus(@RequestBody WareInfo wareInfo) {

        String wareId = wareInfo.getWareId().toString();
        String status = wareInfo.getStatus();

        wareInfoService.updateStatus(wareId, status);

        return ResultGenerator.genSuccessResult();
    }

    /**
     * 单个仓库查询
     */
    @GetMapping("/detail")
    @Authority("ware_infos_detail")
    public Result detail(Integer id) {
        WareInfo wareInfo = wareInfoService.findById(id);
        return ResultGenerator.genSuccessResult(wareInfo);
    }

    @GetMapping("/list")
    @Authority("ware_infos_list")
    public Result list(WareInfoCriteria criteria, @User CurrentUser currentUser) {

        if (currentUser.getIsAdmin() != 1) {
            criteria.setUserId(currentUser.getUserId());
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WareInfoDto> list = wareInfoService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);

        /*
         * if(currentUser.getIsAdmin() ==1){ PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize()); List<WareInfoDto2>list = wareInfoService.findSome(criteria); PageInfo pageInfo = new
         * PageInfo(list); return ResultGenerator.genSuccessResult(pageInfo); }else { criteria.setUserId(currentUser.getUserId()); PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
         * List<WareInfoDto>list = wareInfoService.findList(criteria); PageInfo pageInfo = new PageInfo(list); return ResultGenerator.genSuccessResult(pageInfo); }
         */
    }

    @GetMapping("/findSome")
    @Authority("ware_infos_findSome")
    public Result findSome(WareInfoCriteria criteria, @User CurrentUser currentUser) {
        // if (currentUser == null || null == currentUser.getUserId() ) {
        /*
         * if(null == criteria.getUserId() || ("").equals(criteria.getUserId())) { criteria.setUserId(currentUser.getUserId()); }
         */

        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<WareInfoDto2> list = wareInfoService.findSome(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/findUserByWareId")
    @Authority("ware_infos_findUserByWareId")
    public Result findUserByWareId(Integer wareId, @User CurrentUser currentUser) {
        List<UserInfoDto> list = userInfoService.findUserByWareId(wareId);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @Transactional
    @GetMapping("/allList")
    @Authority("ware_infos_allList")
    public Result allList() {
        /*
         * if (currentUser == null) { throw new ServiceException(CommonCode.MISSING_PERMISSION_PARAMTER, null); }
         */
        List<WareInfo> list = null;// = (List<WareInfo>) redisUtil.get("allWareInfo");
        if (list == null || list.size() == 0) {
            list = wareInfoService.findAll();
            cacheTool.setObject(CacheKey.ALL_WARE_INFO, null, list, CacheTime.ALL_WARE_INFO);
        }

        // PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(list);
    }
}
