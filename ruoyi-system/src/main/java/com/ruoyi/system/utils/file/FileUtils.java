package com.ruoyi.system.utils.file;


import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * @author admin
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年11月11日 09:50:00
 */
public class FileUtils {

    /**
     * 将MultiPartFile 转换成File类型
     * 不带数字的修正版本 建议使用
     * @param file 前端上传的文件
     * @return File
     */
    public static File multipartFileTransFileOnFix(MultipartFile file,String reslut){
        String originalFilename = file.getOriginalFilename();
        //如果不是一个zip的压缩包
        return createFile(file,originalFilename,reslut);
    }

    /**
     * 创建文件
     * @param file 前端上传的文件
     * @param originalFilename 文件的真实名称
     * @return File
     */
    public static File createFile(MultipartFile file,String originalFilename,String reslut){
        File targetFile=null;
        // 获取文件存储的路径
        //获取文件的后缀名
        String extension = FilenameUtils.getExtension(originalFilename);

        // 获取文件的名称
        String baseName = FilenameUtils.getBaseName(originalFilename);
        try {
            //转换文件
            targetFile=new File(reslut +baseName+"."+extension);
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }
}
