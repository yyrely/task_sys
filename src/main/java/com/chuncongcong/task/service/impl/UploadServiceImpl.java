package com.chuncongcong.task.service.impl;

import com.chuncongcong.task.exception.BaseErrorCode;
import com.chuncongcong.task.exception.ServiceException;
import com.chuncongcong.task.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Hu
 * @date 2018/12/15 18:43
 */

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${upload.file.portrait}")
    private String uploadPortraitPath;

    @Value("${upload.file.task}")
    private String uploadTaskPath;

    @Value("${upload.file.message}")
    private String uploadMessagePath;

    @Override
    public Object uploadFile(MultipartFile file, HttpServletRequest request, String flag) throws IOException {
        if (file == null) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }
        String newFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        String uploadFilePath;
        if("portrait".equals(flag)) {
            uploadFilePath = uploadPortraitPath;
        }else if("task".equals(flag)) {
            uploadFilePath = uploadTaskPath;
        }else if("message".equals(flag)) {
            uploadFilePath = uploadMessagePath;
        }else {
            throw new ServiceException(BaseErrorCode.PARAM_ILLEGAL);
        }

        File parentFile = new File(uploadFilePath);
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        File resultFile = new File(uploadFilePath + newFileName);

        file.transferTo(resultFile);

        return uploadFilePath + newFileName;
    }
}
