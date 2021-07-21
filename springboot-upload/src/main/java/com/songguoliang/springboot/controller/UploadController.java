package com.songguoliang.springboot.controller;

import com.songguoliang.springboot.domain.ResponseBean;
import com.songguoliang.springboot.util.Compress;
import com.songguoliang.springboot.util.Thumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 上传文件
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@Controller
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);


    @PostMapping("/upload")
    @ResponseBody
    public ResponseBean upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
        String filePath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        String compressFilePath = request.getSession().getServletContext().getRealPath("/compressFile/");
        String thumbnailPath = request.getSession().getServletContext().getRealPath("/thumbnail/");

        File dir = new File(filePath);
        if (!dir.isDirectory()) {//文件目录不存在，就创建一个
            dir.mkdirs();
        }

        if (file.isEmpty()) {
            return ResponseBean.error("上传失败，请选择文件",null);
        }
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = System.currentTimeMillis() + ext;
        File dest = new File(filePath + File.separator + newFileName);
        try {
            String url =  request.getScheme() +"://" + request.getServerName()
                    + ":" +request.getServerPort();
            file.transferTo(dest);
            LOGGER.info("上传成功");
            Compress.compressionVideo(filePath, newFileName, compressFilePath, newFileName, 2);
            LOGGER.info("压缩成功");
            String thumbnailName = Thumbnail.videoImage(compressFilePath + newFileName, thumbnailPath);
            LOGGER.info("创建缩略图成功");
            HashMap<String, Object> data = new HashMap<>();
            data.put("compressFileUrl", url + "/compressFile/");
            data.put("compressFileName", newFileName);
            data.put("thumbnailUrl", url + "/thumbnail/");
            data.put("thumbnailName", thumbnailName);
            return ResponseBean.success("上传成功", data);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return ResponseBean.error("上传失败",null);
    }

    @RequestMapping("/setResolution")
    public ResponseBean setResolution(String resolution, String path, HttpServletRequest request){
        String compressFilePath = request.getSession().getServletContext().getRealPath("/compressFile/");
        String newFileName = System.currentTimeMillis() + "ext";
        Compress.compressionVideo(compressFilePath, path.substring(path.lastIndexOf('/') + 1), compressFilePath, newFileName, Integer.valueOf(resolution));
        LOGGER.info("分辨率调整成功");
        String url =  request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort() + "/compressFile/" + newFileName;
        System.out.println(url);
        return ResponseBean.success("成功", url);
    }
}
