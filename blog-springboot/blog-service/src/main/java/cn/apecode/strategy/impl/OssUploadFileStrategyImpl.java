package cn.apecode.strategy.impl;

import cn.apecode.utils.OSSConfigYml;
import cn.apecode.dto.UploadFileInfoDto;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.VoidResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @description: 阿里云OSS实现类
 * @author: apecode
 * @date: 2022-06-23 16:50
 **/
@Service("ossUploadFileStrategyImpl")
public class OssUploadFileStrategyImpl extends AbstractUploadFileStrategyImpl {

    @Resource
    private OSSConfigYml ossConfigYml;

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        getOssClient().putObject(ossConfigYml.getBucketName(), path + fileName, inputStream);
    }

    @Override
    public boolean exists(String filePath) {
        return getOssClient().doesObjectExist(ossConfigYml.getBucketName(), filePath);
    }

    @Override
    public UploadFileInfoDto getFileAccessUrl(String filePath, UploadFileInfoDto uploadFileInfo) {
        return uploadFileInfo.setUrl(ossConfigYml.getUrl() + filePath);
    }

    public OSS getOssClient() {
        return new OSSClientBuilder().build(ossConfigYml.getEndpoint(), ossConfigYml.getAccessKeyId(), ossConfigYml.getAccessKeySecret());
    }

    @Override
    public Boolean deleteFile(String path) {
        VoidResult voidResult = getOssClient().deleteObject(ossConfigYml.getBucketName(), path);
        return voidResult.getResponse().isSuccessful();
    }
}
