package com.deer.wms.file.configurer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.deer.wms.file.constant.FileUseType;
import com.deer.wms.file.util.FileS3Tool;
import com.deer.wms.project.root.constant.CacheKey;
import com.deer.wms.project.root.util.CacheTool;
import com.deer.wms.project.root.util.DateUtils;
import com.deer.wms.project.root.util.Np;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

// TODO 需要多个FileS3Tool实现，不同的桶。不同的桶要使用不同的缓存
@Component
public class FileS3ToolImpl implements FileS3Tool {

    private String bucketName = "foodsup-wms";
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private CacheTool cache;

    @Override
    public String getFullPath(String path, String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return null;
        }
        if (fileName.indexOf("/") == 0) {
            fileName = fileName.substring(1);
        }
        if (StrUtil.isBlank(fileName) || fileName.indexOf("/") != -1) {
            return null;
        }
        if (path == null) {
            path = "";
        }
        while (path.indexOf("/") == 0) {// 去除首位斜杠
            path = path.substring(1);
        }
        while (path.length() > 0 && path.substring(path.length() - 1, path.length()).equals("/")) {// 去除末位斜杠
            path = path.substring(0, path.length() - 1);
        }
        if (path.length() == 0) {
            return null;
        }
        return path + "/" + fileName;
    }

    @Override
    public String save(FileUseType fileUseType, String fileName, File file) {
        String path = "/wms" + fileUseType.getPath() + "/" + DateUtils.dateToStr(new Date(), "yyyyMMdd") + "/" + DateUtils.dateToStr(new Date(), "HHmmss") + "/"
                + IdUtil.fastSimpleUUID().substring(0, 6);
        String fullPath = this.getFullPath(path, fileName);
        if (fullPath == null) {
            return null;
        }
        // 上传
        PutObjectRequest request = new PutObjectRequest(bucketName, fullPath, file);
        s3Client.putObject(request);
        return fullPath;
    }

    @Override
    public String save(FileUseType fileUseType, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        //获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //获取默认定位到的当前用户目录("user.dir"),也就是当前应用的根路径
        String tempDir = System.getProperty("user.dir");
        //根目录下生成临时文件
        File file = new File(tempDir+File.separator+originalFilename);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        String path = "/wms" + fileUseType.getPath() + "/" + DateUtils.dateToStr(new Date(), "yyyyMMdd") + "/" + DateUtils.dateToStr(new Date(), "HHmmss") + "/"
                + IdUtil.fastSimpleUUID().substring(0, 6);
        String fullPath = this.getFullPath(path, originalFilename);
        if (fullPath == null) {
            return null;
        }
        // 上传
        PutObjectRequest request = new PutObjectRequest(bucketName, fullPath, file);
        s3Client.putObject(request);
        //删除临时文件
        file.delete();
        return fullPath;
    }

    @Override
    public String getUrl(String fullPath) {
        return this.getUrl(fullPath, 10 * 60, 0);
    }

    @Override
    public String getUrl(String fullPath, int timeoutSecond, Integer cacheSecond) {
        if (StrUtil.isBlank(fullPath)) {
            return null;
        }
        String result = cache.getObject(CacheKey.S3_FILE, fullPath);
        if (result != null) {
            return result;
        }
        // 文件是否存在
        try {
            s3Client.getObject(bucketName, fullPath);
        } catch (AmazonS3Exception e) {
            return null;
        }
        // 访问路径与缓存时长
        if (timeoutSecond <= 1) {
            timeoutSecond = 1;
        }
        if (cacheSecond == null) {
            cacheSecond = timeoutSecond / 2;
        }
        if (cacheSecond > timeoutSecond - 10) {
            cacheSecond = timeoutSecond - 10;
        }
        if (cacheSecond < 0) {
            cacheSecond = 0;
        }
        // 得到URL
        Date expiration = new Date(System.currentTimeMillis() + timeoutSecond * 1000);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fullPath)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        result = Np.i(url).toString();
        // 缓存
        if (cacheSecond > 0) {
            cache.setObject(CacheKey.S3_FILE, fullPath, result, cacheSecond);
        }
        return result;
    }

}
