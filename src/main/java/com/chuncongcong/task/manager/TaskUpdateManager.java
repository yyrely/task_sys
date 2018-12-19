package com.chuncongcong.task.manager;

import com.chuncongcong.task.dao.mapper.ext.TaskUpdateExtMapper;
import com.chuncongcong.task.model.entity.TaskUpdate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hu
 * @date 2018/8/14 17:28
 */

@Component
public class TaskUpdateManager {

    private final TaskUpdateExtMapper taskUpdateExtMapper;

    public TaskUpdateManager(TaskUpdateExtMapper taskUpdateExtMapper) {
        this.taskUpdateExtMapper = taskUpdateExtMapper;
    }

    public void addTaskUpdate(TaskUpdate taskUpdate) {
        taskUpdateExtMapper.insertSelective(taskUpdate);
    }

    public List<TaskUpdate> getTaskUpdateList(Long taskId) {
        return taskUpdateExtMapper.getTaskUpdateList(taskId);
    }

    public void removeTaskUpdate(Long taskId) {
        taskUpdateExtMapper.removeTaskUpdate(taskId);
    }
}
