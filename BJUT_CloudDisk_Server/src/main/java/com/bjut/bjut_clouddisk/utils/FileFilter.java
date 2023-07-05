package com.bjut.bjut_clouddisk.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class FileFilter {
    static Map<String, List<String>> suffixMap = new HashMap<>();
    static {
        suffixMap.put("picture", Arrays.asList(".jpg", ".png", ".gif"));
        suffixMap.put("document", Arrays.asList(".doc", ".md"));
        suffixMap.put("movie", Arrays.asList(".mp4", ".avi", ".mkv", ".mov"));
        suffixMap.put("sound", Arrays.asList(".mp3", ".flac"));
    }

    public static List<TreeNode> getFilesWithTypeOf(String path, String types){
        List<TreeNode> filteredFileList = new ArrayList<>();
        File rootPath = new File(path);
        File[] allFileList = rootPath.listFiles();
        if (allFileList == null){
            return filteredFileList;
        }
        List<String> targetSuffixList = suffixMap.get(types);
        for(File f : allFileList){
            if(f.isFile()){
                for (String suff : targetSuffixList){
                    if(f.getName().endsWith(suff)){
                        TreeNode treeNode = new TreeNode(f);
                        filteredFileList.add(treeNode);
                        break;
                    }
                }
            }
        }
        return filteredFileList;
    }
}
