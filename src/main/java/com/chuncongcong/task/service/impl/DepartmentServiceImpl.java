package com.chuncongcong.task.service.impl;

import com.chuncongcong.task.context.RequestContext;
import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.manager.DepartmentManager;
import com.chuncongcong.task.manager.UsersManager;
import com.chuncongcong.task.model.domain.DUserBean;
import com.chuncongcong.task.model.domain.DepartmentAndUserBean;
import com.chuncongcong.task.model.entity.Department;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/7 10:23
 */

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentManager departmentManager;
    private final UsersManager usersManager;

    public DepartmentServiceImpl(DepartmentManager departmentManager, UsersManager usersManager) {
        this.departmentManager = departmentManager;
        this.usersManager = usersManager;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getDepartmentAndUsers() {
        List<Department> departmentList = departmentManager.getDepartmentList();
        List<DepartmentAndUserBean> list = new ArrayList<>();
        for (Department department : departmentList) {
            List<Users> usersList = usersManager.getUserListByDepartmentId(department.getId());
            List<DUserBean> dUserBeansList = new ArrayList<>();
            usersList.forEach(ul ->
                    dUserBeansList.add(DUserBean.builder()
                            .id(ul.getId())
                            .label(ul.getUsername())
                            .role(ul.getRole())
                            .build())
            );
            list.add(DepartmentAndUserBean.builder()
                    .id(department.getId())
                    .label(department.getName())
                    .userList(dUserBeansList)
                    .build());
        }
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object getDepartment() {
        Users user = RequestContext.getUserInfo();
        return departmentManager.getDepartment(user.getDepartmentId());
    }

    @Override
    public Object getUserListByDepartment(Long departmentId) {
        Users user = RequestContext.getUserInfo();
        if (user.getDepartmentId() != departmentId) {
            throw new ServiceException(BaseErrorCode.AUTHORITY_ILLEGAL);
        }
        return usersManager.getUserListByDepartmentId(departmentId);
    }

    @Override
    public Object getDepartmentList() {
        List<Department> departmentList = departmentManager.getDepartmentList();
        List<DepartmentAndUserBean> list = new ArrayList<>();
        departmentList.forEach(d -> {
            DepartmentAndUserBean departmentAndUserBean = new DepartmentAndUserBean();
            departmentAndUserBean.setId(d.getId());
            departmentAndUserBean.setLabel(d.getName());
            departmentAndUserBean.setUserList(null);
            list.add(departmentAndUserBean);
        });
        return list;

    }

}
