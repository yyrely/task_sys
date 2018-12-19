package com.chuncongcong.task.manager;


import com.chuncongcong.task.dao.mapper.ext.TaskExtMapper;
import com.chuncongcong.task.model.domain.TaskConditionBean;
import com.chuncongcong.task.model.entity.Task;
import com.chuncongcong.task.model.vo.TaskStateVO;
import com.chuncongcong.task.model.vo.TaskVO;
import com.chuncongcong.task.model.vo.UsersVO;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TaskManager {

    private final TaskExtMapper taskExtMapper;

    public TaskManager(TaskExtMapper taskExtMapper) {
        this.taskExtMapper = taskExtMapper;
    }

    public void addTask(Task task) {
        taskExtMapper.insertSelective(task);
    }

    public void updateTask(Task task) {
        taskExtMapper.updateByPrimaryKeySelective(task);
    }

    public List<TaskVO> getTaskList(TaskConditionBean taskCondition) {
        return taskExtMapper.getTaskList(taskCondition);
    }

    public TaskVO getTaskInfo(Long taskId) {
        return taskExtMapper.getTaskInfo(taskId);
    }

    public List<TaskStateVO> getTaskNumByState(Long userId, Instant startTime, Instant endTime) {
        return taskExtMapper.getTaskNumByState(userId, startTime, endTime);
    }

    public List<Task> getTaskListByTime(Long userId, Instant startDateTime, Instant endDateTime) {
        return taskExtMapper.getTaskListByTime(userId, startDateTime, endDateTime);
    }

    public Task getTask(Long taskId) {
        return taskExtMapper.getTask(taskId);
    }

    public List<UsersVO> getTaskByUsers(List<Long> userIds) {
        return taskExtMapper.getUsersTask(userIds);
    }

    public void removeTask(Long taskId) {
        taskExtMapper.removeTask(taskId);
    }

    public List<Task> getNowadaysTask(Long userId) {
        return taskExtMapper.getNowadaysTask(userId);
    }
}
