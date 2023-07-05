package com.bjut.bjut_clouddisk.controller;

import com.bjut.bjut_clouddisk.common.MyResponse;
import com.bjut.bjut_clouddisk.utils.FileManager;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
@RestController
public class FileManageController {

    // http://localhost:8080/Mkdir?filePath=&name=b
    @RequestMapping("/Mkdir")
    public MyResponse<String> mkdir(
            @RequestParam("filePath") String filePath, @RequestParam("name") String name){
        boolean success = FileManager.mkdir(filePath, name);
        if (success) {
            return MyResponse.success("创建成功");
        } else {
            return MyResponse.error("创建失败");
        }
    }


    @RequestMapping("/Rename")
    public MyResponse<String> Rename(
            @RequestParam("filePath") String filePath, @RequestParam("name") String name){
        boolean success = FileManager.rename(filePath, name);
        if (success) {
            return MyResponse.success("重命名成功");
        } else {
            return MyResponse.error("重命名失败");
        }
    }


    // http://localhost:8080/Move?srcPath=a&targetPath=c/a
    @RequestMapping("/Move")
    public MyResponse<String> move(
            @RequestParam("srcPath") String srcPath, @RequestParam("targetPath") String targetPath){
        boolean success = FileManager.move(srcPath, targetPath);
        if (success) {
            return MyResponse.success("移动成功");
        } else {
            return MyResponse.error("移动失败");
        }
    }

    // http://localhost:8080/Remove?filePath=a
    @RequestMapping("/Remove")
    public MyResponse<String> remove(
            @RequestParam("filePath") String filePath){
        boolean success = FileManager.remove(filePath);
        if (success) {
            return MyResponse.success("删除成功");
        } else {
            return MyResponse.error("删除失败");
        }
    }
}

