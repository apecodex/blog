package cn.apecode.blog.strategy.impl;

import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.exception.BizException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

/**
 * @description: 本地上传实现类
 * @author: apecode
 * @date: 2022-06-23 16:26
 **/
@Service("localUploadFileStrategyImpl")
public class LocalUploadFileStrategyImpl extends AbstractUploadFileStrategyImpl {

    @Value("${upload.local.staticAccessPath}")
    private String staticAccessPath;
    @Value("${upload.local.uploadFolder}")
    private String uploadFolder;
    @Value("${upload.local.url}")
    private String url;

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        File directory = new File(uploadFolder + path);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new BizException("创建目录失败");
            }
        }
        File file = new File(uploadFolder + path + fileName);
        if (file.createNewFile()) {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            byte[] bytes = new byte[1024];
            int length;
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            bos.flush();
            inputStream.close();
            bis.close();
            bos.close();
        }
    }

    @Override
    public boolean exists(String filePath) {
        return new File(uploadFolder + filePath).exists();
    }

    @Override
    public UploadFileInfoDto getFileAccessUrl(String filePath, UploadFileInfoDto uploadFileInfo) {
        return uploadFileInfo.setUrl(url + staticAccessPath + filePath);
    }

    @Override
    public Boolean deleteFile(String path) {
        File file = new File(uploadFolder + path);
        return file.delete();
    }
}
