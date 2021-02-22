package com.dongyimai.shop.controller;

import com.dongyimai.entity.Result;
import com.offcn.util.FastDFSClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传Controller
 * @author Administrator
 *
 */
@RestController
public class UploadController {
    private String FILE_SERVER_URL = "http://192.168.188.146/";
    //文件服务器地址
    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        //获取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            //创建FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //拼接返回的url和IP地址，拼接成完整的url
            String url = FILE_SERVER_URL + path;
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
