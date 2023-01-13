package cn.apecode.blog.strategy.context;

import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.strategy.UploadFileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static cn.apecode.blog.enums.UploadModeEnum.getStrategy;

/**
 * @description: 上传策略
 * @author: apecode
 * @date: 2022-06-23 16:34
 **/
@Service
public class UploadFileStrategyContext {

    @Value("${upload.mode}")
    private String mode;
    @Autowired
    private Map<String, UploadFileStrategy> uploadFileStrategyMap;

    public UploadFileInfoDto executeUploadFileStrategy(MultipartFile file, String path) {
        return uploadFileStrategyMap.get(getStrategy(mode)).uploadFile(file, path);
    }

    public Boolean executeDeleteFileStrategy(String path) {
        return uploadFileStrategyMap.get(getStrategy(mode)).deleteFile(path);
    }
}
