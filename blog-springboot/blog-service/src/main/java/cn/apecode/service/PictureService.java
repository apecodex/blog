package cn.apecode.service;

import cn.apecode.dto.PhotoAlbumInfoDto;
import cn.apecode.dto.PictureBackDto;
import cn.apecode.dto.PictureFrontDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.entity.Picture;
import cn.apecode.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图片 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface PictureService extends IService<Picture> {

    /**
     * @description: 根据相册路径获取图片列表（后台）
     * @param condition
     * @return {@link PageResult<PictureBackDto>}
     * @auther apecode
     * @date 2022/6/23 23:00
    */
    PageResult<PictureBackDto> listPictureByPhotoAlbumId(ConditionVo condition);

    /**
     * @description: 保存照片
     * @param picture
     * @auther apecode
     * @date 2022/6/23 23:20
    */
    void savePicture(PictureVo picture);

    /**
     * @description: 更新照片信息
     * @param pictureInfo
     * @auther apecode
     * @date 2022/6/23 23:58
    */
    void updatePicture(PictureInfoVo pictureInfo);

    /**
     * @description: 删除照片
     * @param pictureIds
     * @auther apecode
     * @date 2022/6/24 0:07
    */
    void deletePicture(List<String> pictureIds);

    /**
     * @description: 移动图片相册
     * @param picture
     * @auther apecode
     * @date 2022/6/24 0:15
    */
    void updatePictureAlbum(PictureVo picture);

    /**
     * @description: 更新图片删除状态
     * @param delete
     * @auther apecode
     * @date 2022/6/24 0:26
    */
    void updatePictureDelete(DeleteVo delete);

    /**
     * @description: 上传图片
     * @param file
     * @return {@link UploadFileInfoDto}
     * @auther apecode
     * @date 2022/6/24 0:40
    */
    UploadFileInfoDto uploadPicture(MultipartFile file);

    /**
     * @description: 根据相册路径获取图片列表
     * @param photoAlbumId
     * @return {@link PageResultWithObject<PictureFrontDto,  PhotoAlbumInfoDto >}
     * @auther apecode
     * @date 2022/6/24 13:58
    */
    PageResultWithObject<PictureFrontDto, PhotoAlbumInfoDto> listPictureByPhotoAlbumId(String photoAlbumId);

    /**
     * @description: 获取回收站图片
     * @return {@link PageResult<PictureBackDto>}
     * @auther apecode
     * @date 2022/6/24 14:42
    */
    PageResult<PictureBackDto> listDeletePicture();

    /**
     * @description: 物理删除图片
     * @param path
     * @auther apecode
     * @date 2022/7/15 13:23
    */
    void deletePicPermanently(List<String> path);
}
