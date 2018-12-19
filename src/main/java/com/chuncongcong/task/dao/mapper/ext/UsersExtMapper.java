package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.dao.mapper.UsersMapper;
import com.chuncongcong.task.model.entity.Users;
import com.chuncongcong.task.model.vo.UsersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersExtMapper extends UsersMapper {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    Users getUsersByUsername(
            @Param("username") String username);

    /**
     * 根据用户id,获取用户信息
     * @param userId
     * @return
     */
    UsersVO getUsersById(
            @Param("userId") Long userId);

    /**
     * 获取用户列表
     *
     * @param username
     * @return
     */
    List<UsersVO> getUserList(
            @Param("username") String username,
            @Param("departmentIds") Long[] departmentIds);

    /**
     * 更新用户点赞
     * @param userId
     */
    void updateUserPraise(
            @Param("userId") Long userId,
            @Param("praise") Integer praise);

    /**
     * 根据用户id加经验
     * @param userIds
     * @param num
     */
    void updateUserRank(
            @Param("userIds") List<Long> userIds,
            @Param("num") Integer num);


    /**
     * 批量查询用户信息
     *
     * @param userIds
     * @return
     */
    List<Users> batchUserList(
            @Param("userIds") List<Long> userIds);

    /**
     * 根据部门id获取用户列表
     *
     * @param departmentId
     * @return
     */
    List<Users> getUserListByDepartmentId(
            @Param("departmentId") Long departmentId);


}