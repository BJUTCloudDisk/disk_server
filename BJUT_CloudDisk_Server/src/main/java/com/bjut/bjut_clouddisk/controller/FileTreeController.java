package com.bjut.bjut_clouddisk.controller;

import com.bjut.bjut_clouddisk.utils.TreeNode;
import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;
import com.bjut.bjut_clouddisk.common.MyResponse;
import com.bjut.bjut_clouddisk.utils.FileFilter;
import com.bjut.bjut_clouddisk.utils.FileTree;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
@RestController
public class FileTreeController {

    // http://localhost:8080/FileTree?path=a
    @GetMapping("/FileTree")
    public MyResponse<List<TreeNode>> GetFileTree(@RequestParam String path) {
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        System.out.println("要获取文件树的路径是" + diskPath + path);
        List<TreeNode> tree = FileTree.getFileTree(diskPath + path);
        return MyResponse.success(tree).message("成功");
    }

    // http://localhost:8080/FilesWithTypeOf?path=F:/Code/Idea_Project/disk/test&type=document
    @GetMapping("/FilesWithTypeOf")
    public MyResponse<List<TreeNode>> GetFilesWithTypeOf(@RequestParam String path, @RequestParam String type){
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        System.out.println("要获取文件树的路径是" + diskPath + path);
        List<TreeNode> tree = FileTree.getFileTree(diskPath + path);
        List<TreeNode> allFileList = FileFilter.getFilesWithTypeOf(path, type);
        return MyResponse.success(allFileList).message("成功");
    }
}
