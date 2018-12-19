package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.UsersExtMapper;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.model.vo.UsersVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersManager {

    private final UsersExtMapper usersExtMapper;

    public UsersManager(UsersExtMapper usersExtMapper) {
        this.usersExtMapper = usersExtMapper;
    }

    public Users getUsersByUsername(String username) {
        return usersExtMapper.getUsersByUsername(username);
    }

    public void addUser(Users user) {
        usersExtMapper.insertSelective(user);
    }

    public UsersVO getUsersById(Long userId) {
        return usersExtMapper.getUsersById(userId);
    }

    public List<UsersVO> getUserList(String username, Long[] departmentIds) {
        return usersExtMapper.getUserList(username, departmentIds);
    }

    public void updateUsers(Users users) {
        usersExtMapper.updateByPrimaryKeySelective(users);
    }

    public void updateUserPraise(Long userId, Integer praise) {
        usersExtMapper.updateUserPraise(userId, praise);
    }

    @Async
    public void updateUserRank(List<Long> userIds, Integer rank) {
        usersExtMapper.updateUserRank(userIds, rank);
    }

    public List<Users> batchUserList(List<Long> userIds) {
        return usersExtMapper.batchUserList(userIds);
    }

    public List<Users> getUserListByDepartmentId(Long departmentId) {
        return usersExtMapper.getUserListByDepartmentId(departmentId);
    }
}
