package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.PhotoAlbumBackDto;
import cn.apecode.blog.dto.PhotoAlbumFrontDto;
import cn.apecode.blog.entity.PhotoAlbum;
import cn.apecode.blog.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 相册 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {

    /**
     * @description: 获取后台相册列表
     * @param page
     * @param condition
     * @return {@link IPage<PhotoAlbumBackDto>}
     * @auther apecode
     * @date 2022/6/23 14:15
    */
    IPage<PhotoAlbumBackDto> listPhotoAlbumBack(Page<PhotoAlbum> page,@Param("condition") ConditionVo condition);

    /**
     * @description: 获取相册列表
     * @param limitCurrent
     * @param size
     * @return {@link List<PhotoAlbumFrontDto>}
     * @auther apecode
     * @date 2022/6/23 18:54
    */
    List<PhotoAlbumFrontDto> listPhotoAlbumFront(@Param("current") Long limitCurrent, @Param("size") Long size);

}
