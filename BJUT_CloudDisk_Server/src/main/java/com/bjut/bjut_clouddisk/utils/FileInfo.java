package com.bjut.bjut_clouddisk.utils;

import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileInfo {
    public static String getSingleCreatDate(String filePath) {

        File file = new File(filePath);
        String formatted = "";

        BasicFileAttributes attrs;
        try {
            attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime time = attrs.creationTime();  // 获取创建时间

            // 将时间戳格式化
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            formatted = simpleDateFormat.format(new Date(time.toMillis()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public static String getFileType(String filePath) throws IOException {
//        Path path = new File(filePath).toPath();
//        String mimeType = Files.probeContentType(path);
//        System.out.println(mimeType);
//        return mimeType;

//        return Optional.ofNullable(filePath)
//                .filter(f -> f.contains("."))
//                .map(f -> f.substring(filePath.lastIndexOf(".") + 1)).get();

        File file = new File(filePath);
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            String extension = fileName.substring(index + 1);
            System.out.println(extension); //输出 md
            return extension;
        } else {
            System.out.println("文件没有扩展名");
            return null;
        }

    }

    public static List<HashMap<String, Object>> getDirectoryInfo(String dirPath, String goalFileType) throws IOException {
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        dirPath = diskPath + dirPath;

        List<HashMap<String, Object>> picData = new ArrayList<>();

        File file = new File(dirPath);  //获取其file对象
        processFile(dirPath, file, goalFileType, picData);  //调用递归方法

        return picData;
    }

    public static void processFile(String dirPath, File file, String goalFileType, List<HashMap<String, Object>> picData) throws IOException {
        if (file.isDirectory()) {  //如果是目录，就遍历它的子文件
            File[] fs = file.listFiles();  //遍历path下的文件和目录，放在File数组中
            assert fs != null;
            for (File f : fs) {  //遍历File[]数组
                processFile(dirPath, f, goalFileType, picData);  //递归调用
            }
        } else {  //如果不是目录，就判断它的类型和创建时间
            String fileType = getFileType(file.getPath());
            if (fileType.equals(goalFileType)) {
                String timestamp = getSingleCreatDate(file.getPath());  // timestamp

                // 查找picData中是否已经有相同的timestamp
                HashMap<String, Object> map = null;
                for (HashMap<String, Object> m : picData) {
                    if (m.get("timestamp").equals(timestamp)) {
                        map = m;
                        break;
                    }
                }

                // 如果没有找到相同的timestamp，就创建一个新的HashMap和List
                if (map == null) {
                    map = new HashMap<>();
                    map.put("timestamp", timestamp);
                    List<String> picList = new ArrayList<>();
                    map.put("picList", picList);
                    picData.add(map);
                }

                // 把图片路径添加到对应的List中
                List<String> picList = (List<String>) map.get("picList");
                picList.add(file.getPath().replace(BjutCloudDiskApplication.class.getResource("/disk/").getFile(), "http://10.18.18.88:8081/disk/"));
            }
        }
    }



    public static void main(String[] args) throws IOException {
//        String test = getSingleCreatDate("/Users/wangzian/Movies/IMG_5442.mp4");
//        System.out.println("文件创建日期和时间是: " + test);
//        String type = getFileType("/Users/wangzian/Movies/IMG_5442.mp4");
//        System.out.println(type);

        List<HashMap<String, Object>> list = getDirectoryInfo("test", "png");
        System.out.println(list.size());
        for (HashMap<String, Object> element: list) {
            System.out.println(element);
        }
    }
}
