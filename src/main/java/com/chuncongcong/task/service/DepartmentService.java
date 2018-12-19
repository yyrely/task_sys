package com.chuncongcong.task.service;

/**
 * @author Hu
 * @date 2018/8/7 10:22
 */


public interface DepartmentService {
    /**
     * 获取部门列表
     * @return
     */
    Object getDepartmentAndUsers();


    /**
     * 获取部门信息
     * @return
     */
    Object getDepartment();

    /**
     * 获取部门下成员列表
     *
     * @param departmentId
     * @return
     */
    Object getUserListByDepartment(Long departmentId);

    /**
     * 获取部门列表
     *
     * @return
     */
    Object getDepartmentList();
}
