package com.groupten.online_music.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {
    private final static String uploadFolder = "/root/upload/";
    private final static String uploadLocation = "images/";

    public static String uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            return "file is empty!";
        //获取文件名并生成文件名
        String originalFileName = multipartFile.getOriginalFilename();
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString() + fileType;
        File uploadingFile = null;
        try {
            //设置文件上传路径
            File path = new File(ResourceUtils.getURL(uploadFolder).getPath());
            File uploadPath = new File(path.getAbsolutePath(), uploadLocation);
            //上传文件
            if (!uploadPath.exists()) uploadPath.mkdirs();
            uploadingFile = new File(uploadPath + "/" + uploadFileName);
            multipartFile.transferTo(uploadingFile);
//            //获取根路径和文件上传路径
//            File path = new File(ResourceUtils.getURL("classpath:").getPath());
//            File uploadTmp = new File(path.getAbsolutePath(), "static/upload/tmp/");
//            //在服务端生成中间文件
//            if (!uploadTmp.exists()) uploadTmp.mkdirs();
//            uploadingFile = new File(uploadTmp + "/" + tmpFileName);
//            multipartFile.transferTo(uploadingFile);
//            //上传文件成功,移动到目的文件夹
//            File uploadPath = new File(path.getAbsolutePath(), "static/upload/image");
//            newFileName = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));
//            if (!uploadPath.exists()) uploadPath.mkdirs();
//            uploadedFile = new File(uploadPath + "/" + newFileName);
//            if (!uploadingFile.renameTo(uploadedFile)) {//移动文件失败
//                System.out.println("中间文件生成成功! 文件移动至目标文件夹失败!");
//                return null;
//            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return null;
        }

        return uploadLocation + uploadFileName;
    }
}
