package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.TaskUserExtMapper;
import com.chuncongcong.task.model.entity.TaskUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskUserManager {
    private final TaskUserExtMapper taskUserExtMapper;

    public TaskUserManager(TaskUserExtMapper taskUserExtMapper) {
        this.taskUserExtMapper = taskUserExtMapper;
    }


    public void insertBatch(List<TaskUser> taskUsers) {
        taskUserExtMapper.insertBatch(taskUsers);
    }


    public void removeTaskJoinUsers(Long taskId, List<Long> delJoinUsers) {
        taskUserExtMapper.removeTaskJoinUsers(taskId, delJoinUsers);
    }

    public List<TaskUser> getUserIds(Long taskId) {
        return taskUserExtMapper.getUserIds(taskId);
    }

    public Long getManagerUserId(Long taskId) {
        return taskUserExtMapper.getManagerUserId(taskId);
    }


}
