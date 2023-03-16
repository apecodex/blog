package cn.apecode.mapper;

import cn.apecode.dto.PictureBackDto;
import cn.apecode.entity.Picture;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 图片 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface PictureMapper extends BaseMapper<Picture> {

    /**
     * @description: 根据相册id获取图片列表
     * @param page
     * @param albumId
     * @return {@link IPage<PictureBackDto>}
     * @auther apecode
     * @date 2022/6/23 23:03
    */
    IPage<PictureBackDto> listPictureByPhotoAlbumId(Page<Picture> page, @Param("albumId") Integer albumId);

    /**
     * @description: 获取回收站图片
     * @param page
     * @return {@link IPage<PictureBackDto>}
     * @auther apecode
     * @date 2022/6/24 14:45
    */
    IPage<PictureBackDto> listDeletePicture(Page<Picture> page);
}
