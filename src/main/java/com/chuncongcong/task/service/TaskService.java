package com.chuncongcong.task.service;

import com.chuncongcong.task.model.constants.TaskType;
import com.chuncongcong.task.model.dto.TaskDTO;
import org.apache.poi.ss.usermodel.Workbook;

import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/7 11:00
 */


public interface TaskService {

    /**
     * 获取所有任务(子任务)
     * @param parentId
     * @return
     */
    Object getAllTask(Long parentId);

    /**
     * 分成员查询任务
     *
     * @param userIds
     * @return
     */
    Object getTaskByUsers(List<Long> userIds);

    /**
     * 分部门查询任务
     *
     * @param departmentIds
     * @return
     */
    Object getTaskByDepartments(List<Long> departmentIds);

    /**
     * 根据任务类型查找任务
     *
     * @param taskType
     * @return
     */
    Object getTaskByType(TaskType taskType);

    /**
     * 获取任务详情
     * @param taskId
     * @param departmentId
     * @return
     */
    Object getTaskInfo(Long taskId, Long departmentId) throws Exception;


    /**
     * 添加任务
     * @param departmentId
     * @param taskDTO
     * @return
     */
    Object addTask(Long departmentId, TaskDTO taskDTO) throws Exception;

    /**
     * 更新任务状态
     * @param departmentId
     * @param taskDTO
     * @return
     */
    Object updateTaskState(Long departmentId, Long taskId, TaskDTO taskDTO) throws Exception;

    /**
     * 更新任务
     *
     * @param departmentId
     * @param taskId
     * @param taskDTO
     * @return
     */
    Object updateTask(Long departmentId, Long taskId, TaskDTO taskDTO);

    /**
     * 删除任务
     *
     * @param taskId
     * @param departmentId
     * @return
     */
    Object removeTask(Long departmentId, Long taskId);

    /**
     * 任务更新列表
     *
     * @param departmentId
     * @param taskId
     * @return
     */
    Object getTaskUpdateRecodes(Long departmentId, Long taskId);

    /**
     * 根据时间获取任务
     * @param date
     * @return
     */
    Object getTaskByDate(String date);

    /**
     * 统计不同任务状态个数
     * @return
     */
    Object getTaskNumByState(Instant startTime, Instant endTime);

    /**
     * 保存下载任务的id
     *
     * @param taskDTOList
     */
    Object saveDownloadTaskIds(List<TaskDTO> taskDTOList);

    /**
     * 下载任务xls文件
     *
     * @param taskIdKey
     * @return
     */
    Workbook downloadTask(String taskIdKey);

    /**
     * 获取今日任务
     * @return
     */
    Object getNowadaysTask();
}
