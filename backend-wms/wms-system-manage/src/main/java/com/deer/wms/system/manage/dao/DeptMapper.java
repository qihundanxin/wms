package com.deer.wms.system.manage.dao;

import com.deer.wms.project.root.core.mapper.Mapper;
import com.deer.wms.system.manage.model.dept.Dept;
import com.deer.wms.system.manage.model.dept.DeptCriteria;
import com.deer.wms.system.manage.model.dept.DeptDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeptMapper extends Mapper<Dept> {

    List<DeptDto> findList(DeptCriteria criteria);

    List<DeptDto> findByDeptCode(@Param("deptCode") String deptCode);

    List<DeptDto> findByDeptName(@Param("deptName") String deptName);

    List<Dept> findByParentId(Integer parentId);

    void deleteByCodeAndCom(DeptCriteria criteria1);
}