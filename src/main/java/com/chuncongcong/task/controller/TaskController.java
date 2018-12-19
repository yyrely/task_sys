package com.chuncongcong.task.controller;

import com.chuncongcong.task.model.constants.Contants;
import com.chuncongcong.task.model.dto.TaskDTO;
import com.chuncongcong.task.service.TaskService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

/**
 * @author Hu
 * @date 2018/8/7 10:58
 */

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 获取今日任务
     * @return
     */
    @GetMapping("/nowadays")
    public Object getNowadaysTask(){
        return taskService.getNowadaysTask();
    }

    /**
     * 根据日期获取任务
     *
     * @param date
     * @return
     */
    @GetMapping
    public Object getTaskByDate(
            @RequestParam(value = "date",defaultValue = "") String date){
        return taskService.getTaskByDate(date);
    }

    /**
     * 获取不同任务状态的数量(根据不同时间)
     *
     * @return
     */
    @GetMapping("/count")
    public Object getTaskNumByState(
            @RequestParam(value = "startTime") Instant startTime,
            @RequestParam(value = "endTime") Instant endTime) {
        return taskService.getTaskNumByState(startTime, endTime);
    }

    /**
     * 保存要下载的任务ids
     *
     * @param taskDTOList
     * @return
     */
    @PostMapping("/download")
    public Object saveDownloadTaskIds(@RequestBody List<TaskDTO> taskDTOList) {
        return taskService.saveDownloadTaskIds(taskDTOList);
    }

    /**
     * 下载任务xls文件
     *
     * @param response
     * @param taskIdKey
     * @throws IOException
     */
    @GetMapping("/download/{taskIdKey}")
    public void downloadTask(HttpServletResponse response,
                             @PathVariable("taskIdKey") String taskIdKey) throws IOException {

        Workbook workbook = taskService.downloadTask(taskIdKey);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + new String(Contants.EXCEL_FILE_NAME.getBytes("gbk"), "iso8859-1") + ".xls");
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }

}
