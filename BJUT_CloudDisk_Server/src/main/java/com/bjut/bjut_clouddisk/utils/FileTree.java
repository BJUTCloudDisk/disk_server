package com.bjut.bjut_clouddisk.utils;

import java.io.File;
import java.util.List;


public class FileTree {
    public static List<TreeNode> getFileTree(String filePath) {
        File file = new File(filePath);
        Tree tree = new Tree(file);
        return tree.root.children;
    }
}
