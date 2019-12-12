package com.leyouxianggou.upload.service.impl;

import com.leyouxianggou.upload.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 上传图片，返回图片的路径URL
     * @param file
     * @return
     */
    @Override
    public String uploadImage(MultipartFile file) {
        //校验文件
        //保存文件
        //返回文件路径
        return null;
    }
}
