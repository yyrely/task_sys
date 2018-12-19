package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.domain.TaskConditionBean;
import com.chuncongcong.task.model.entity.Task;
import com.chuncongcong.task.model.vo.TaskStateVO;
import com.chuncongcong.task.model.vo.TaskVO;
import com.chuncongcong.task.model.vo.UsersVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface TaskExtMapper extends com.chuncongcong.task.dao.mapper.TaskMapper {

    /**
     * 获取任务列表
     *
     * @param taskCondition
     * @return
     */
    List<TaskVO> getTaskList(
            @Param("taskCondition") TaskConditionBean taskCondition);

    /**
     * 获取用户列表及其任务
     *
     * @param userIds
     * @return
     */
    List<UsersVO> getUsersTask(@Param("userIds") List<Long> userIds);

    /**
     * 根据任务id获取任务负责人
     *
     * @param taskId
     * @return
     */
    Long getManagerId(Long taskId);

    /**
     * 根据任务id获取任务参与人
     *
     * @param taskId
     * @return
     */
    Long getJoinId(Long taskId);
    /**
     * 任务信息
     *
     * @param taskId
     * @return
     */
    TaskVO getTaskInfo(
            @Param("taskId") Long taskId);


    /**
     * 统计每个任务状态的数量
     *
     * @param userId
     * @return
     */
    List<TaskStateVO> getTaskNumByState(
            @Param("userId") Long userId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);

    /**
     * 根据时间返回任务列表
     *
     * @param userId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    List<Task> getTaskListByTime(
            @Param("userId") Long userId,
            @Param("startDateTime") Instant startDateTime,
            @Param("endDateTime") Instant endDateTime);

    /**
     * 获取任务基本信息
     *
     * @param taskId
     * @return
     */
    Task getTask(@Param("taskId") Long taskId);

    /**
     * 删除任务及子任务
     *
     * @param taskId
     */
    void removeTask(@Param("taskId") Long taskId);

    /**
     * 获取今日任务
     * @param userId
     * @return
     */
    List<Task> getNowadaysTask(Long userId);
}