package com.bjut.bjut_clouddisk.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.tomcat.util.http.FastHttpDateFormat.formatDate;

// 多叉树节点类
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeNode implements Serializable {
    private static Pattern suffixPattern = Pattern.compile("[^.]+\\.(\\w+)$");  // 正则提取后缀
    @Serial
    private static final long serialVersionUID = 1L;

    String name; // 文件或文件夹的名称
    String suffix;  // 后缀名，如果是文件夹则留空
    long size; // 文件或文件夹的大小，单位为字节
    String date; // 文件或文件夹的最近修改日期，格式为yyyy-MM-dd HH:mm:ss

    String type;    // 文件类型
    Boolean isFolder;   //是否文件夹
    List<TreeNode> children; // 子节点列表

    // 构造方法
    public TreeNode(String name, long size, String date, Boolean isFolder) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.isFolder = isFolder;
        // 提取后缀
        if (isFolder) {
            this.children = new ArrayList<>();
            this.type = "文件夹";
        } else {
            this.children = null;
            this.suffix = getFileSuffix(name);
            this.type = getFileType(this.name);
        }
    }

    public TreeNode(File file) {
        this(
                file.getName(),
                file.length(),
                formatDate(file.lastModified()),
                file.isDirectory()
        );
    }

    // 添加子节点
    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    private static String getFileSuffix(String name){
        Matcher matcher = suffixPattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getFileType(String name){
        String type;
        try {
            String suffix = getFileSuffix(name);
            type = TypeSuffixMapJsonReader.typeSuffixMap.get(suffix);
        } catch (Exception e) {
            type = "未知";
        }
        return type;
    }
}
