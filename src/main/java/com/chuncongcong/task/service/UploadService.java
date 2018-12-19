package com.chuncongcong.task.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Hu
 * @date 2018/12/15 18:43
 */

public interface UploadService {
    /**
     * 上传文件
     * @param file
     * @param request
     * @param flag
     * @return
     */
    Object uploadFile(MultipartFile file, HttpServletRequest request, String flag) throws IOException;
}
