package com.platform.wetrade.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Component
@Slf4j
public class FileUtil {

    @Value("${img.upload.path}")
    private String uploadPath;

    @Value("${img.get.context}")
    private String context;

    private String domain = "http://localhost:8080";

    public boolean getFile(String fileName, HttpServletResponse response)  {

        String filePath = uploadPath + fileName;
        InputStream in = null;
        OutputStream out = null;
        try {

            in = new FileInputStream(filePath);
            out= response.getOutputStream();

            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = in.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();

        } catch (Exception e) {
            return false;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                return false;
            }

            try {
                in.close();
            } catch (IOException e) {
                return  false;
            }

        }

        return true;


    }

    public String upload(MultipartFile file) throws IOException {

        String newFileName = getNewFileName(file);
        String filePath = uploadPath + newFileName;
        File destFile = new File(filePath);
        file.transferTo(destFile);

        return generateSrc(newFileName);


    }

    public boolean isValid(MultipartFile file) {
        // 获取上传文件的 MIME 类型
        String contentType = file.getContentType();
        log.info("============== type: " + contentType);
        if (!contentType.contains("image")) {
            return false;
        }
        return true;

    }

    private String getNewFileName(MultipartFile file){

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + suffix;

        return newFileName;
    }

    private String generateSrc(String filename){
        return domain + context + filename;
    }

}
