package com.bjut.bjut_clouddisk.controller;

import cn.hutool.core.util.ZipUtil;
import com.bjut.bjut_clouddisk.utils.EncryptionDES;
import com.bjut.bjut_clouddisk.BjutCloudDiskApplication;
import com.bjut.bjut_clouddisk.common.MyResponse;
import com.bjut.bjut_clouddisk.config.DiskConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import static com.bjut.bjut_clouddisk.utils.FileInfo.getDirectoryInfo;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
@RestController
public class FileController {
    @Autowired
    DiskConfig diskConfig;

    @Value("${disk.IP}")
    public String IP;

    @Value("${disk.port}")
    public String port;

    @RequestMapping("/download")
    public MyResponse<String> fileDownLoad(HttpServletResponse response, @RequestParam("filePath") String filePath) {
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        System.out.println("要下载的文件路径是" + diskPath + filePath);
        File file = new File(diskPath + filePath);
        if (!file.exists()) {
            return MyResponse.error("文件不存在");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + filePath);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return MyResponse.error("下载失败");
        }
        return MyResponse.success("下载成功");
    }

    /***
     * 上传文件到服务器
     * @param file
     * @param filePath
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public MyResponse<String> uploadFile(@RequestParam MultipartFile file, @RequestParam("filePath") String filePath) throws IOException {
        // 获取服务器的保存文件的路径
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        String savePath = diskPath + filePath;
        System.out.println("文件上传路径是" + savePath);

        String[] dirPath = savePath.split("/");
        StringBuilder finalDirPath = new StringBuilder();
        for (int i = 0; i < dirPath.length - 1; i++) {
            finalDirPath.append(dirPath[i]).append("/");
        }
        // 创建文件对象
        File saveDir = new File(finalDirPath.toString());
        // 判断文件夹是否存在，不存在则创建
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        // 判断文件是否为空，不为空则上传
        if (!file.isEmpty()) {
            // 获取文件的原始名称
            String fileName = file.getOriginalFilename();
            // 创建文件对象，用于保存上传的文件
            File saveFile = new File(saveDir, fileName);
            // 调用transferTo方法，将文件写入到指定位置
            file.transferTo(saveFile);

            return MyResponse.success("上传成功");
        } else {
            // 文件为空，返回错误信息或者其他处理逻辑
            return MyResponse.error("上传失败");
        }
    }

//    @RequestMapping("/share")
//    public MyResponse<String> share(
//            @RequestParam("username") String username,
//            @RequestParam("acceptUsername") String acceptUsername,
//            @RequestParam("filePath") String filePath
//    ) {
//        WebSocket.sendOneMessage(acceptUsername,
//                "[" + username + "]" +
//                        "请求分享文件" +
//                        "[" + filePath + "]");
//        // TODO 前端要发送下载请求，以实现分享文件的功能，通过这个正则表达式\[(.*?)\]就可以获取到username和filePath，拼起来，调用download接口就行
//        return MyResponse.success("已向对方发送分享文件请求");
//    }

    /***
     * 生成分享链接
     * @param filePath
     * @return
     * @throws Exception
     */
    @GetMapping("/share")
    public MyResponse<String> share(@RequestParam("filePath") String filePath) throws Exception {
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        String savePath = diskPath + filePath;
        File file = new File(savePath);
        if (file.exists()) {
            EncryptionDES des = new EncryptionDES();
            String shareLink = des.encrypt(filePath);
            System.out.println("分享链接是：" + shareLink);

            return MyResponse.success(shareLink).message("创建分享链接成功");
        } else {
            return MyResponse.error("文件不存在").message("文件不存在");
        }

    }

    /***
     * 通过分享链接下载文件
     * @param response
     * @param shareLink
     * @return
     * @throws Exception
     */
    @GetMapping("/getShare")
    public MyResponse<String> getShare(HttpServletResponse response, @RequestParam("shareLink") String shareLink) throws Exception {
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
        EncryptionDES des = new EncryptionDES();
        String savePath = diskPath + des.decrypt(shareLink);
        System.out.println("要获取的文件是：" + savePath);
        File file = new File(savePath);
        if (!file.exists()) {
            return MyResponse.error("文件不存在");
        }
        response.reset();
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + savePath);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return MyResponse.error("获取分享文件失败");
        }

        return MyResponse.success("获取分享文件成功");
    }

    /***
     * 多线程分块下载文件
     * @param request
     * @param response
     * @param filePath
     * @throws Exception
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
    @RequestMapping("/spiltDownLoad")
    @Async // 使用@Async注解来标记这个方法是异步执行的
    public void spiltDownLoad(HttpServletRequest request, HttpServletResponse response, @RequestParam("filePath") String filePath) throws Exception {

        //解决前端接受自定义heards
        response.setHeader("Access-Control-Expose-Headers",
                "Content-Disposition,Accept-Range,fSize,fName,Content-Range,Content-Lenght,responseType");
        String diskPath = BjutCloudDiskApplication.class.getResource("/disk/").getFile();
//        System.out.println("要下载的文件路径是" + diskPath + filePath);
        File file = new File(diskPath + filePath);
        //设置编码
        response.setCharacterEncoding("utf-8");
        InputStream is = null;
        OutputStream os = null;
        try {
            //分片下载
            long fSize = file.length();
            response.setHeader("responseType", "blob");
            //前段识别下载
            response.setContentType("application/x-download");
            //response.setContentType("application/octet-stream");
            //文件名
            String fileName = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment; filename=" +
                    fileName);
            //http Range 告诉前端支持分片下载
            response.setHeader("Accept-Range", "bytes");
            //告诉前文件大小 文件名字
            response.setHeader("fSize", String.valueOf(fSize));
            response.setHeader("fName", fileName);
            //起始位置 结束位置 读取了多少
            long pos = 0, last = fSize - 1, sum = 0;
            //需不需要分片下载
            if (null != request.getHeader("Range")) {
                //支持分片下载 206
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                //bytes=10-100
                String numRange = request.getHeader("Range").replaceAll("bytes=", "");
                String[] strRange = numRange.split("-");
                //取起始位置
                if (strRange.length >= 2) {
                    pos = Long.parseLong(strRange[0].trim());
                    last = Long.parseLong(strRange[1].trim());
                    if (last > fSize - 1) {
                        last = fSize - 1;
                    }
                } else {
                    pos = Long.parseLong(numRange.replaceAll("-", "").trim());
                }
            }
            //读多少
            long rangeLenght = last - pos + 1;
            //告诉前端有多少分片
            String contentRange = "bytes " + pos + "-" + last + "/" + fSize;
            //规范告诉文件大小
            // 修改：设置Content-Range响应首部
            response.setHeader("Content-Range", contentRange);

            os = new BufferedOutputStream(response.getOutputStream());

            is = new BufferedInputStream(new FileInputStream(file));
            is.skip(pos);
            byte[] buffer = new byte[1024 * 1024 * 100];
            int lenght = 0;
            while (sum < rangeLenght) {
                lenght = is.read(buffer, 0, (rangeLenght - sum) <= buffer.length ? ((int) (rangeLenght - sum)) : buffer.length);
                sum = sum + lenght;
                os.write(buffer, 0, lenght);

                os.flush();

            }


        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /***
     * 获取文件夹下所有指定类型的文件信息
     * @param filePath
     * @param fileType
     * @return
     * @throws Exception
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
    @GetMapping("/getInfo")
    public MyResponse<List<HashMap<String, Object>>> getInfo(@RequestParam("filePath") String filePath,
                                                             @RequestParam("fileType") String fileType) throws Exception {
        List<HashMap<String, Object>> picData = getDirectoryInfo(filePath, fileType);
        return MyResponse.success(picData);
    }

    /***
     * 压缩文件
     * @param filePath
     * @return
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
    @RequestMapping("package")
    @ResponseBody
    public MyResponse<String> packagePath(@RequestParam("filePath") String filePath) {
        ZipUtil.zip(filePath);

        return MyResponse.success("压缩成功");
    }

    /***
     * 解压文件
     * @param filePath
     * @return
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST}, allowedHeaders = "*")
    @RequestMapping("unPackage")
    @ResponseBody
    public MyResponse<String> unPackage(@RequestParam("filePath") String filePath) {
        if (filePath.contains(".zip")) {
            ZipUtil.unzip(filePath);
        }

        return MyResponse.success("解压成功");
    }
}

