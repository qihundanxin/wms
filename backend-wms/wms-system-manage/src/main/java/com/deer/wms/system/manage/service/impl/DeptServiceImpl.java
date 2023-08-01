package com.deer.wms.system.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.CommonCode;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.project.root.core.result.ResultGenerator;
import com.deer.wms.project.root.core.service.AbstractService;
import com.deer.wms.project.root.exception.ServiceException;
import com.deer.wms.system.manage.dao.DeptMapper;
import com.deer.wms.system.manage.model.dept.Dept;
import com.deer.wms.system.manage.model.dept.DeptCriteria;
import com.deer.wms.system.manage.model.dept.DeptDto;
import com.deer.wms.system.manage.service.DeptService;

/**
 * Created by guo on 2019/11/05.
 */
@Service
@Transactional
public class DeptServiceImpl extends AbstractService<Dept, Integer> implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public Result createDept(DeptDto dept, CurrentUser currentUser) {

        /**
         * 新建部门
         */
//        String code = PinYinUtil.getPinYinHeadChar(dept.getDeptName());
//        dept.setDeptCode(code);
        List<DeptDto> list = this.findByDeptCode(dept.getDeptCode());
        List<DeptDto> list1 = this.findByDeptName(dept.getDeptName());

        if (list.size() == 0 && list1.size() == 0) {
            save(dept);
        } else if (list.size() == 0 && list1.size() != 0) {
            throw new ServiceException(CommonCode.DEPT_EXISTS, "此部门已存在");
        } else {
            throw new ServiceException(CommonCode.DEPT_CODE_EXISTS, "此部门编码已存在");
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public List<DeptDto> findList(DeptCriteria criteria) {
        return deptMapper.findList(criteria);
    }

    @Override
    public List<DeptDto> findByDeptCode(String deptCode) {
        return deptMapper.findByDeptCode(deptCode);
    }

    @Override
    public List<DeptDto> findByDeptName(String deptName) {
        return deptMapper.findByDeptName(deptName);
    }

    @Override
    public List<Dept> findByParentId(Integer parentId) {
        return deptMapper.findByParentId(parentId);
    }

    public List<Integer> treeMenuList(List<DeptDto> list, Integer parentId) {
        List<Integer> newList = new ArrayList<Integer>();
        for (DeptDto deptDto : list) {
            // 遍历出父id等于参数的id，add进子节点集合
            if (deptDto.getParentId() == parentId) {
                // 递归遍历下一级
                // list.remove(itemType);
                treeMenuList(list, deptDto.getDeptId());
                newList.add(deptDto.getDeptId());
            }
        }
        return newList;
    }

    @Override
    public void deleteByCodeAndCom(DeptCriteria criteria) {
        deptMapper.deleteByCodeAndCom(criteria);
    }

    public List<Integer> treeParentList(List<Dept> list, Dept dept) {
        List<Integer> newList = new ArrayList<Integer>();
        for (Dept depts : list) {
            // 遍历出父id等于参数的id，add进子节点集合
            Integer deptId = depts.getDeptId();
            // Integer parentId = type.getParentId();
            if (dept.getParentId() == deptId) {
                // 递归遍历下一级
                // list.remove(itemType);
                treeParentList(list, depts);
                newList.add(deptId);
            }
        }
        newList.add(dept.getDeptId());
        return newList;
    }

}
