package com.chuncongcong.task.dao.mapper.ext;

import com.chuncongcong.task.model.entity.TaskUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskUpdateExtMapper extends com.chuncongcong.task.dao.mapper.TaskUpdateMapper {

    /**
     * 获取任务更新记录
     *
     * @param taskId
     * @return
     */
    List<TaskUpdate> getTaskUpdateList(Long taskId);

    /**
     * 删除更新任务记录
     *
     * @param taskId
     */
    void removeTaskUpdate(
            @Param("taskId") Long taskId);
}