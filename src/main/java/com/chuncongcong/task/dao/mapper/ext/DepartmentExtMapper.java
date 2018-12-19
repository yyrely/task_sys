package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentExtMapper extends com.chuncongcong.task.dao.mapper.DepartmentMapper {
    /**
     * 部门列表
     * @return
     */
    List<Department> getDepartmentList();

    /**
     * 根据id获取部门信息
     *
     * @param departmentId
     * @return
     */
    Department getDepartment(Long departmentId);
}