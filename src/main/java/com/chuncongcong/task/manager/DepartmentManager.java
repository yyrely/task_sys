package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.DepartmentExtMapper;
import com.chuncongcong.task.model.entity.Department;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/21 15:08
 */

@Component
public class DepartmentManager {
    private final DepartmentExtMapper departmentExtMapper;

    public DepartmentManager(DepartmentExtMapper departmentExtMapper) {
        this.departmentExtMapper = departmentExtMapper;
    }

    public List<Department> getDepartmentList() {
        return departmentExtMapper.getDepartmentList();
    }

    public Department getDepartment(Long departmentId) {
        return departmentExtMapper.getDepartment(departmentId);
    }
}
