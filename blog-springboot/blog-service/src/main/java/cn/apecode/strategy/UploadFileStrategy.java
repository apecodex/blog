package cn.apecode.strategy;

import cn.apecode.dto.UploadFileInfoDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description: 上传接口
 * @author: apecode
 * @date: 2022-06-23 16:16
 **/
public interface UploadFileStrategy {

    /**
     * @description: 上传文件
     * @param file
     * @param path
     * @return {@link String}
     * @auther apecode
     * @date 2022/6/23 16:17
    */
    UploadFileInfoDto uploadFile(MultipartFile file, String path);

    /**
     * @description: 删除文件
     * @param path
     * @return {@link Boolean}
     * @auther apecode
     * @date 2022/7/7 12:54
    */
    Boolean deleteFile(String path);
}
