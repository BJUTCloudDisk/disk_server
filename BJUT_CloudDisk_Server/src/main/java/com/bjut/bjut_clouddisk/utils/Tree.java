package com.bjut.bjut_clouddisk.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;


// 多叉树类
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    TreeNode root;  // 根节点

    // 构造方法
    public Tree(File file) {
        this.root = new TreeNode(file);
        buildTree(this.root, file);  // 从根节点开始构建多叉树
    }

    // 递归构建多叉树的方法
    public void buildTree(TreeNode node, File file) {
        if (file.isDirectory()) {  // 如果是文件夹，就遍历其下的文件和文件夹，并添加为子节点
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    TreeNode child = new TreeNode(f);
                    node.addChild(child);
                    // 对于是文件夹的子节点，递归处理
                    if (child.isFolder){
                        buildTree(child, f);
                    }
                }
            }
        }
    }

    // 格式化日期的方法，把毫秒数转换为yyyy-MM-dd HH:mm:ss格式的字符串
    public String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millis);
        return sdf.format(date);
    }

    // 打印多叉树的方法，用缩进表示层级关系
    public StringBuilder printTree() {
        StringBuilder treeStr = new StringBuilder();
        return printNode(this.root, 0, treeStr); // 从根节点开始打印，初始缩进为0
    }

    // 递归打印节点的方法
    public StringBuilder printNode(TreeNode node, int indent, StringBuilder treeStr) {
        // 根据缩进打印空格
        // System.out.print(" ");
        treeStr.append(" ".repeat(Math.max(0, indent)));
        treeStr.append(node.name + " (" + node.size + " bytes, " + node.date + ")");
        // System.out.println(node.name + " (" + node.size + " bytes, " + node.date + ")"); // 打印节点的名称、大小和日期
        for (TreeNode child : node.children) { // 遍历子节点，并增加缩进
            printNode(child, indent + 4, treeStr);
        }
        return treeStr;
    }

    private static String getFileType(File file){
        if (file.isDirectory()){
            return "folder";
        }
        String type;
        try {
            type = Files.probeContentType(file.toPath());
        } catch (Exception e) {
            type = "unknown";
        }
        return type;
    }
}
