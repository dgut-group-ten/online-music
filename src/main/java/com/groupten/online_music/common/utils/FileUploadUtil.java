package com.groupten.online_music.common.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {
    public static String uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            return "file is empty!";
        //获取文件名并生成中间文件名
        String originalFileName = multipartFile.getOriginalFilename();
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        String tmpFileName = UUID.randomUUID().toString() + fileType + ".tmp";
        String newFileName = null;
        File uploadingFile = null;
        File uploadedFile = null;

        try {
            //获取根路径和文件上传路径
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File uploadTmp = new File(path.getAbsolutePath(), "static/tmp/");
            //在服务端生成中间文件
            if (!uploadTmp.exists()) uploadTmp.mkdirs();
            uploadingFile = new File(uploadTmp + "\\" + tmpFileName);
            multipartFile.transferTo(uploadingFile);
            //上传文件成功,移动到目的文件夹
            File uploadPath = new File(path.getAbsolutePath(), "static/headIcon/");
            newFileName = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));
            if (!uploadPath.exists()) uploadPath.mkdirs();
            uploadedFile = new File(uploadPath + "\\" + newFileName);
            if (!uploadingFile.renameTo(uploadedFile)) {//移动文件失败
                System.out.println("中间文件生成成功! 文件移动至目标文件夹失败!");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件上传失败!");
            return null;
        } finally {
            uploadingFile.delete();//删除中间文件
        }

        return "https://localhost:8081/headIcon/" + newFileName;
    }
}
