package com.chuncongcong.task.controller;

import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.model.domain.TaskConditionBean;
import com.chuncongcong.task.model.dto.TaskDTO;
import com.chuncongcong.task.service.DepartmentService;
import com.chuncongcong.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Hu
 * @date 2018/8/7 10:20
 */

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final TaskService taskService;

    public DepartmentController(DepartmentService departmentService, TaskService taskService) {
        this.departmentService = departmentService;
        this.taskService = taskService;
    }

    /**
     * 获取公司部门列表
     * @return
     */
    @GetMapping
    public Object getDepartmentAndUsers() {
        return departmentService.getDepartmentAndUsers();
    }

    @GetMapping("/lists")
    public Object getDepartmentList() {
        return departmentService.getDepartmentList();
    }

    @GetMapping("/{departmentId}/users")
    public Object getUserList(@PathVariable("departmentId") Long departmentId) {
        return departmentService.getUserListByDepartment(departmentId);
    }

    /**
     * 部门信息
     * @return
     */
    @GetMapping("/info")
    public Object getDepartment() {
        return departmentService.getDepartment();
    }

    /**
     * 添加任务
     *
     * @param departmentId
     * @param taskDTO
     * @return
     * @throws Exception
     */
    @PostMapping("/{departmentId}/tasks")
    public Object addTask(
            @PathVariable("departmentId") Long departmentId,
            @RequestBody TaskDTO taskDTO) throws Exception {
        return taskService.addTask(departmentId, taskDTO);
    }

    /**
     * 更新任务状态
     *
     * @param taskDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/{departmentId}/tasks/{taskId}/states")
    public Object updateTaskState(
            @PathVariable("taskId") Long taskId,
            @PathVariable("departmentId") Long departmentId,
            @RequestBody TaskDTO taskDTO) throws Exception {
        return taskService.updateTaskState(departmentId, taskId, taskDTO);
    }

    /**
     * 更新任务
     *
     * @param taskDTO
     * @return
     * @throws Exception
     */
    @PutMapping("/{departmentId}/tasks/{taskId}")
    public Object updateTask(
            @PathVariable("taskId") Long taskId,
            @PathVariable("departmentId") Long departmentId,
            @RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(departmentId, taskId, taskDTO);
    }

    /**
     * 获取任务详情
     *
     * @param departmentId
     * @param taskId
     * @return
     * @throws Exception
     */
    @GetMapping("/{departmentId}/tasks/{taskId}")
    public Object getTaskInfo(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("taskId") Long taskId) throws Exception {
        return taskService.getTaskInfo(taskId, departmentId);
    }

    /**
     * 移除任务
     *
     * @param departmentId
     * @param taskId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{departmentId}/tasks/{taskId}")
    public Object removeTask(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("taskId") Long taskId) {
        return taskService.removeTask(departmentId, taskId);
    }

    /**
     * 按部门获取任务列表
     *
     * @param taskCondition
     * @return
     */
    @GetMapping("/tasks")
    public Object getTaskList(
            TaskConditionBean taskCondition) {

        if ((taskCondition.getUserIds() == null || taskCondition.getUserIds().size() == 0) && (taskCondition.getDepartmentIds() == null || taskCondition.getDepartmentIds().size() == 0) && taskCondition.getTaskType() == null) {
            //查找全部
            return taskService.getAllTask(taskCondition.getParentId());
        }

        if (taskCondition.getTaskType() != null && (taskCondition.getUserIds() == null || taskCondition.getUserIds().size() == 0) && (taskCondition.getDepartmentIds() == null || taskCondition.getDepartmentIds().size() == 0)) {
            //按任务分类查找
            return taskService.getTaskByType(taskCondition.getTaskType());
        }

        if (taskCondition.getDepartmentIds() != null && taskCondition.getDepartmentIds().size() != 0 && (taskCondition.getUserIds() == null || taskCondition.getUserIds().size() == 0) && taskCondition.getTaskType() == null) {
            //查看部门任务
            return taskService.getTaskByDepartments(taskCondition.getDepartmentIds());
        }

        if (taskCondition.getUserIds() != null && taskCondition.getUserIds().size() != 0 && (taskCondition.getDepartmentIds() == null || taskCondition.getDepartmentIds().size() == 0) && taskCondition.getTaskType() == null) {
            //查看成员任务
            return taskService.getTaskByUsers(taskCondition.getUserIds());
        }

        throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
    }

    /**
     * 获取任务更新记录列表
     * @param departmentId
     * @param taskId
     * @return
     */
    @GetMapping("/{departmentId}/tasks/{taskId}/recodes")
    public Object getUpdateRecodes(
            @PathVariable("departmentId") Long departmentId,
            @PathVariable("taskId") Long taskId) {
        return taskService.getTaskUpdateRecodes(departmentId, taskId);
    }


}
