package com.deer.wms.file.util;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.internal.Nullable;
import com.deer.wms.file.constant.FileUseType;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3文件工具
 * 
 * @author xuesinuo
 *
 */
public interface FileS3Tool {
    /**
     * 保存文件到S3
     * 
     * @param fileUseType 存储到的路径，/或空对应根路径
     * @param fileName    保存的文件名
     * @param file        源文件
     * @return fullPath
     */
    String save(FileUseType fileUseType, String fileName, File file);

    /**
     * 保存文件到S3
     * @param fileUseType 存储到的路径，/或空对应根路径
     * @param multipartFile        源文件
     * @return fullPath
     */
    String save(FileUseType fileUseType,MultipartFile multipartFile) throws IOException;

    /**
     * 获取文件访问URL（如果是授权文件，默认可以访问时长为10分钟）
     * 
     * @param fullPath 文件全路径
     * @return 访问文件的URL
     */
    String getUrl(String fullPath);

    /**
     * 限制访问时长的文件访问URL（如果桶支持的话）
     * 
     * @param fullPath      文件全路径
     * @param timeoutSecond URL的有效时长（秒），建议不低于10分钟，频繁访问的资源建议加长时长并设置缓存
     * @param cacheSecond   缓存时长，【null】默认为有效时长的一半，【小于等于0】不缓存，最大为timeoutSecond减10分钟
     * @return 访问文件的URL
     */
    String getUrl(String fullPath, int timeoutSecond, @Nullable Integer cacheSecond);

    /**
     * 将path和fileName合并成fullPath
     * 
     * <pre>
     * path支持前、后，带斜杠、不带斜杠的所有情况
     * fileName支持前面带斜杠，文件名中不可带有斜杠
     * 
     * 最终将被格式化为"/upload/invoice/inv1.png"这种格式
     * </pre>
     * 
     * @param path     路径
     * @param fileName 文件名
     * @return 全路径
     */
    String getFullPath(String path, String fileName);
}
