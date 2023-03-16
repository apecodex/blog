package cn.apecode.service;

import cn.apecode.dto.PhotoAlbumBackDto;
import cn.apecode.dto.PhotoAlbumFrontDto;
import cn.apecode.entity.PhotoAlbum;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.PhotoAlbumVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    /**
     * @description: 获取后台相册列表
     * @param condition
     * @return {@link PageResult<PhotoAlbumBackDto>}
     * @auther apecode
     * @date 2022/6/23 14:02
    */
    PageResult<PhotoAlbumBackDto> listPhotoAlbumBack(ConditionVo condition);

    /**
     * @description: 添加相册
     * @param photoAlbum
     * @auther apecode
     * @date 2022/6/23 14:36
    */
    void savePhotoAlbum(PhotoAlbumVo photoAlbum);

    /**
     * @description: 根据id删除相册
     * @param photoAlbumId
     * @auther apecode
     * @date 2022/6/23 17:19
    */
    void deletePhotoAlbumById(String photoAlbumId);

    /**
     * @description: 获取相册列表
     * @param condition
     * @return {@link PageResult<PhotoAlbumFrontDto>}
     * @auther apecode
     * @date 2022/6/23 19:24
    */
    PageResult<PhotoAlbumFrontDto> listPhotoAlbumFront(ConditionVo condition);

    /**
     * @description: 修改相册
     * @param photoAlbum
     * @auther apecode
     * @date 2022/6/23 19:30
    */
    void updatePhotoAlbum(PhotoAlbumVo photoAlbum);
}
