package cn.apecode.strategy.impl;

import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.FileUtils;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.strategy.UploadFileStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @description:
 * @author: apecode
 * @date: 2022-06-23 16:17
 **/
@Service
public abstract class AbstractUploadFileStrategyImpl implements UploadFileStrategy {
    @Override
    public UploadFileInfoDto uploadFile(MultipartFile file, String path) {
        try {
            // 文件名
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 文件扩展名
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            String filename = md5 + extName;
            UploadFileInfoDto uploadFileInfoDto = new UploadFileInfoDto();
            if (!exists(path + filename)) {
                upload(path, filename, file.getInputStream());
            }
            try {
                // 获取图片宽高
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                // 不是图片文件则宽高为0
                if (Objects.isNull(bufferedImage)) {
                    uploadFileInfoDto.setHeight(0);
                    uploadFileInfoDto.setWidth(0);
                } else {
                    // 宽度
                    uploadFileInfoDto.setHeight(bufferedImage.getHeight());
                    // 高度
                    uploadFileInfoDto.setWidth(bufferedImage.getWidth());
                }
                // 文件大小
                uploadFileInfoDto.setSize(String.valueOf(file.getSize()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getFileAccessUrl(path + filename, uploadFileInfoDto);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("文件上传失败");
        }
    }

    /**
     * @param path
     * @param fileName
     * @param inputStream
     * @description: 上传文件
     * @auther apecode
     * @date 2022/6/23 16:24
     */
    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * @param filePath
     * @return {@link boolean}
     * @description: 判断文件是否存在
     * @auther apecode
     * @date 2022/6/23 16:24
     */
    public abstract boolean exists(String filePath);

    /**
     * @param filePath
     * @return {@link String}
     * @description: 获取文件url地址
     * @auther apecode
     * @date 2022/6/23 16:24
     */
    public abstract UploadFileInfoDto getFileAccessUrl(String filePath, UploadFileInfoDto uploadFileInfo);
}
