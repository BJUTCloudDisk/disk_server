package com.bjut.bjut_clouddisk.utils;

import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileManager {
    static String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();

    public enum MKDIR_CODE{
        SUCCESS,
        DUPLICATE
    }

    public static boolean mkdir(String filePath, String name) {
        File file = new File(diskPath + filePath + '/' + name);
        return file.mkdir();
    }

    public enum RENAME_CODE{
        SUCCESS,
        DUPLICATE
    }

    // http://localhost:8080/Rename?filePath=b&name=c
    public static boolean rename(String filePath, String newName) {
        File oldFile = new File(diskPath + filePath); // 创建原文件对象
        File newFile = new File(oldFile.getParent() + '/' + newName); // 创建新文件对象
        if (!oldFile.getParent().equals(newFile.getParent())){
            return false;
        }
        if (newFile.exists()) { // 检查新文件是否已存在
            return false;
        }
        return oldFile.renameTo(newFile); // 如果不存在，重命名并返回结果
    }

    public static boolean move(String srcPath, String targetPath) {
        File srcFile = new File(diskPath + srcPath);
        File targetFile = new File(diskPath + targetPath);
        if (targetFile.exists()) { // 检查目标是否已存在
            return false;
        }
        return srcFile.renameTo(targetFile); // 如果不存在，移动并返回结果
    }

    public static boolean remove(String filePath) {
        File file = new File(diskPath + filePath); // 创建文件或文件夹对象
        if (!file.exists()) { // 检查是否存在
            return false; // 如果不存在，返回false
        }
        try {
            FileUtils.deleteDirectory(file); // 调用deleteDirectory方法删除文件夹及其内容
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
