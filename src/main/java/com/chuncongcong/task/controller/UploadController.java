package com.chuncongcong.task.controller;

import com.chuncongcong.task.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Hu
 * @date 2018/12/15 18:40
 */

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    /**
     * 上传头像
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/portraits")
    public Object uploadPortrait(MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadService.uploadFile(file, request,"portrait");
    }

    /**
     * 上传任务附件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/tasks")
    public Object uploadTask(MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadService.uploadFile(file, request,"task");
    }

    /**
     * 上传消息附件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/messages")
    public Object uploadMessage(MultipartFile file, HttpServletRequest request) throws IOException {
        return uploadService.uploadFile(file, request,"message");
    }

}
