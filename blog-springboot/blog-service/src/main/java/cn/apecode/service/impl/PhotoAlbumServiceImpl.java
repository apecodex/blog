package cn.apecode.service.impl;

import cn.apecode.utils.UserUtils;
import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.PhotoAlbumBackDto;
import cn.apecode.dto.PhotoAlbumFrontDto;
import cn.apecode.entity.PhotoAlbum;
import cn.apecode.entity.Picture;
import cn.apecode.mapper.PhotoAlbumMapper;
import cn.apecode.mapper.PictureMapper;
import cn.apecode.service.PhotoAlbumService;
import cn.apecode.utils.PageUtils;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.PhotoAlbumVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 相册 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {

    private final PhotoAlbumMapper photoAlbumMapper;
    private final PictureMapper pictureMapper;

    /**
     * @description: 获取后台相册列表
     * @param condition
     * @return {@link PageResult<PhotoAlbumBackDto>}
     * @auther apecode
     * @date 2022/6/23 14:02
    */
    @Override
    public PageResult<PhotoAlbumBackDto> listPhotoAlbumBack(ConditionVo condition) {
        Page<PhotoAlbum> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<PhotoAlbumBackDto> photoAlbumBackDtoIPage = photoAlbumMapper.listPhotoAlbumBack(page, condition);
        photoAlbumBackDtoIPage.getRecords().stream().peek(album -> album.setId(SecurityUtils.encrypt(String.valueOf(album.getId())))).collect(Collectors.toList());
        return new PageResult<>(photoAlbumBackDtoIPage.getRecords(), (int) photoAlbumBackDtoIPage.getTotal());
    }

    /**
     * @description: 添加相册
     * @param photoAlbum
     * @auther apecode
     * @date 2022/6/23 14:36
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePhotoAlbum(PhotoAlbumVo photoAlbum) {
        // 判断相册名是否存在
        boolean exists = photoAlbumMapper.exists(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getAlbumName, photoAlbum.getAlbumName()));
        if (exists) throw new BizException("相册名 '" + photoAlbum.getAlbumName() + "' 已存在");
        PhotoAlbum album = PhotoAlbum.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .albumName(photoAlbum.getAlbumName())
                .albumCover(photoAlbum.getAlbumCover())
                .albumDesc(photoAlbum.getAlbumDesc())
                .status(photoAlbum.getStatus())
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        this.save(album);
    }

    /**
     * @description: 根据id删除相册
     * @param photoAlbumId
     * @auther apecode
     * @date 2022/6/23 17:20
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePhotoAlbumById(String photoAlbumId) {
        Integer albumId = SecurityUtils.decrypt(photoAlbumId);
        if (Objects.isNull(albumId)) throw new BizException("相册id有误");
        boolean exists = pictureMapper.exists(Wrappers.<Picture>lambdaQuery().eq(Picture::getAlbumId, albumId));
        if (exists) {
            // 图片则逻辑删除相册和图片
            photoAlbumMapper.updateById(PhotoAlbum.builder()
                            .id(albumId)
                            .isDelete(true)
                            .build());
            pictureMapper.update(new Picture(), Wrappers.<Picture>lambdaUpdate()
                    .set(Picture::getIsDelete, true)
                    .eq(Picture::getAlbumId, albumId));
        } else {
            // 若相册下不存在图片则直接删除
            photoAlbumMapper.deleteById(albumId);
        }
    }

    /**
     * @description: 获取相册列表
     * @param condition
     * @return {@link PageResult<PhotoAlbumFrontDto>}
     * @auther apecode
     * @date 2022/6/23 19:24
    */
    @Override
    public PageResult<PhotoAlbumFrontDto> listPhotoAlbumFront(ConditionVo condition) {
        // 获取相册总数
        Long count = photoAlbumMapper.selectCount(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getIsDelete, false).eq(PhotoAlbum::getStatus, true));
        List<PhotoAlbumFrontDto> photoAlbumFrontDtoList = photoAlbumMapper.listPhotoAlbumFront(PageUtils.getLimitCurrent(), PageUtils.getSize());
        photoAlbumFrontDtoList.stream().peek(photoAlbum -> photoAlbum.setId(SecurityUtils.encrypt(String.valueOf(photoAlbum.getId())))).collect(Collectors.toList());
        return new PageResult<>(photoAlbumFrontDtoList, count.intValue());
    }

    /**
     * @description: 修改相册
     * @param photoAlbum
     * @auther apecode
     * @date 2022/6/23 19:31
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePhotoAlbum(PhotoAlbumVo photoAlbum) {
        Integer albumId = SecurityUtils.decrypt(photoAlbum.getAlbumId());
        if (Objects.isNull(albumId)) throw new BizException("相册id有误");
        boolean exists = photoAlbumMapper.exists(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getId, albumId));
        if (!exists) throw new BizException("相册不存在");
        PhotoAlbum album = PhotoAlbum.builder()
                .id(albumId)
                .albumName(photoAlbum.getAlbumName())
                .albumDesc(photoAlbum.getAlbumDesc())
                .albumCover(photoAlbum.getAlbumCover())
                .status(photoAlbum.getStatus())
                .isDelete(photoAlbum.getIsDelete())
                .build();
        this.updateById(album);
    }
}
