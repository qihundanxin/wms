package com.deer.wms.system.manage.web;

import java.util.List;

import com.deer.wms.intercept.annotation.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deer.wms.intercept.annotation.User;
import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.model.dept.Dept;
import com.deer.wms.system.manage.model.dept.DeptCriteria;
import com.deer.wms.system.manage.model.dept.DeptDto;
import com.deer.wms.system.manage.model.user.UserInfoDto;
import com.deer.wms.system.manage.service.DeptService;
import com.deer.wms.system.manage.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 部门信息api接口
 * 
 * Created by guo on 2019/11/05.
 */
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 添加部门信息
     */
    @PostMapping("/insert")
    @Authority("depts_insert")
    public Result add(@RequestBody DeptDto dept, @User CurrentUser currentUser) {
        deptService.createDept(dept, currentUser);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除部门信息
     */
    @Transactional
    @GetMapping("/delete")
    @Authority("depts_delete")
    public Result delete(Integer deptId, @User CurrentUser currentUser) {

        List<UserInfoDto> userInfos = userInfoService.findByDeptId(deptId);
        if (userInfos.size() == 0) {
            Integer parentId = deptId;
            List<Dept> depts = deptService.findByParentId(parentId);
            for (Dept dept : depts) {
                deptService.deleteById(dept.getDeptId());
            }
            deptService.deleteById(deptId);
            return ResultGenerator.genSuccessResult();
        } else {
            throw new ServiceException(CommonCode.DEPT_BOUND);
        }
    }

    /**
     * 更新部门信息
     */
    @Transactional
    @PostMapping("/update")
    @Authority("depts_update")
    public Result update(@RequestBody Dept dept) {
        List<DeptDto> list = deptService.findByDeptCode(dept.getDeptCode());
        if (list.size() == 0) {
            deptService.update(dept);
        } else if (list.size() == 1) {
            if (list.get(0).getDeptId().equals(dept.getDeptId())) {
                List<DeptDto> list1 = deptService.findByDeptName(dept.getDeptName());
                if (list1.size() == 0) {
                    deptService.update(dept);
                } else if (list1.size() == 1 && list1.get(0).getDeptId().equals(dept.getDeptId())) {
                    deptService.update(dept);
                } else {
                    throw new ServiceException(CommonCode.DEPT_EXISTS, "此部门已存在");
                }

            } else if (!list.get(0).getDeptId().equals(dept.getDeptId())) {

                throw new ServiceException(CommonCode.DEPT_CODE_EXISTS, "此部门编码已存在");
            }
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 单个部门查询
     */
    @GetMapping("/detail")
    @Authority("depts_detail")
    public Result detail(Integer id) {
        Dept dept = deptService.findById(id);
        return ResultGenerator.genSuccessResult(dept);
    }

    @GetMapping("/list")
    @Authority("depts_list")
    public Result list(DeptCriteria criteria, @User CurrentUser currentUser) {
        if (currentUser.getIsAdmin() == 1) {
            criteria.setUserId(null);
        }
        PageHelper.startPage(criteria.getPageNum(), criteria.getPageSize());
        List<DeptDto> list = deptService.findList(criteria);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/treeParentList")
    @Authority("depts_treeParentList")
    public Result list(Dept dept) {
        List<Dept> depts = deptService.findAll();
        List<Integer> list = deptService.treeParentList(depts, dept);
        return ResultGenerator.genSuccessResult(list);
    }

}
