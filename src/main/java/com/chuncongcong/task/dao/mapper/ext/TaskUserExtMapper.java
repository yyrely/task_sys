package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.entity.TaskUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskUserExtMapper extends com.chuncongcong.task.dao.mapper.TaskUserMapper {


    /**
     * 批量添加
     *
     * @param taskUsers
     */
    void insertBatch(
            @Param("taskUsers") List<TaskUser> taskUsers);

    /**
     * 批量删除
     *
     * @param delUserIds
     * @param taskId
     */
    void removeTaskJoinUsers(
            @Param("taskId") Long taskId,
            @Param("delUserIds") List<Long> delUserIds);

    /**
     * 根据任务id获取
     * @param taskId
     * @return
     */
    List<TaskUser> getUserIds(
            @Param("taskId") Long taskId);

    /**
     * 获取任务负责人id
     *
     * @param taskId
     * @return
     */
    Long getManagerUserId(
            @Param("taskId") Long taskId);


}