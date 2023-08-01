package com.deer.wms.system.manage.service;

import com.deer.wms.intercept.common.data.CurrentUser;
import com.deer.wms.project.root.core.result.Result;
import com.deer.wms.system.manage.model.dept.Dept;
import com.deer.wms.project.root.core.service.Service;
import com.deer.wms.system.manage.model.dept.DeptCriteria;
import com.deer.wms.system.manage.model.dept.DeptDto;

import java.util.List;

/**
 * Created by guo on 2019/11/05.
 */
public interface DeptService extends Service<Dept, Integer> {

    List<DeptDto> findList(DeptCriteria criteria);

    List<DeptDto> findByDeptCode(String deptCode);

    List<DeptDto> findByDeptName(String deptName);

    List<Dept> findByParentId(Integer parentId);

    List<Integer> treeMenuList(List<DeptDto> list, Integer parentId);

    List<Integer> treeParentList(List<Dept> list, Dept dept);

    public Result createDept(DeptDto deptDto, CurrentUser currentUser);

    void deleteByCodeAndCom(DeptCriteria criteria);

}
