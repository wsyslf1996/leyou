package com.leyouxianggou.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyouxianggou.common.enums.ExceptionEnum;
import com.leyouxianggou.common.exception.LyException;
import com.leyouxianggou.upload.config.UploadProperties;
import com.leyouxianggou.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private UploadProperties properties;

    /**
     * 上传图片，返回图片的路径URL
     * @param file
     * @return
     */
    @Override
    public String uploadImage(MultipartFile file) {
        String fileUrl = null;
            try{
            //校验文件(1.校验文件后缀名是否符合要求，2.校验文件是否是真实图片)
            if(!properties.getAllowTypes().contains(file.getContentType())){
                log.error("非允许的文件类型");
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if(bufferedImage ==null){
                log.error("读取图片失败");
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            };

            //保存文件
            String extension=StringUtils.substringAfterLast(file.getOriginalFilename(),".");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            String baseUrl = properties.getBaseUrl();
            fileUrl = ( baseUrl.endsWith("/") ? baseUrl : baseUrl+ "/" ) + storePath.getFullPath();
        }catch (Exception e){
            throw new LyException(ExceptionEnum.BRAND_INSERT_ERROR);
        };
        //返回文件路径
        log.info("文件上传成功，URL："+ fileUrl);
        return fileUrl;
    }
}
