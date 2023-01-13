package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.PhotoAlbumInfoDto;
import cn.apecode.blog.dto.PictureBackDto;
import cn.apecode.blog.dto.PictureFrontDto;
import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.entity.PhotoAlbum;
import cn.apecode.blog.entity.Picture;
import cn.apecode.blog.enums.FilePathEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.PhotoAlbumMapper;
import cn.apecode.blog.mapper.PictureMapper;
import cn.apecode.blog.service.PhotoAlbumService;
import cn.apecode.blog.service.PictureService;
import cn.apecode.blog.strategy.context.UploadFileStrategyContext;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 图片 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private PhotoAlbumMapper photoAlbumMapper;
    @Autowired
    private PhotoAlbumService photoAlbumService;
    @Autowired
    private UploadFileStrategyContext uploadFileStrategyContext;

    /**
     * @param condition
     * @return {@link PageResult<PictureBackDto>}
     * @description: 根据相册路径获取图片列表（后台）
     * @auther apecode
     * @date 2022/6/23 23:00
     */
    @Override
    public PageResult<PictureBackDto> listPictureByPhotoAlbumId(ConditionVo condition) {
        Integer albumId = null;
        if (StringUtils.isBlank(condition.getAlbumPath())) {
            throw new BizException("相册id不能为空");
        }
        albumId = SecurityUtils.decrypt(condition.getAlbumPath());
        if (Objects.isNull(albumId)) throw new BizException("相册id有误");
        Page<Picture> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<PictureBackDto> pictureBackDtoIPage = pictureMapper.listPictureByPhotoAlbumId(page, albumId);
        pictureBackDtoIPage.getRecords().stream().peek(picture -> picture.setId(SecurityUtils.encrypt(String.valueOf(picture.getId())))).collect(Collectors.toList());
        return new PageResult<>(pictureBackDtoIPage.getRecords(), (int) pictureBackDtoIPage.getTotal());
    }

    /**
     * @param picture
     * @description: 保存照片
     * @auther apecode
     * @date 2022/6/23 23:20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void savePicture(PictureVo picture) {
        if (picture.getPictureUrlList().isEmpty()) return;
        Integer albumId = SecurityUtils.decrypt(picture.getAlbumId());
        if (Objects.isNull(albumId)) throw new BizException("相册id有误");
        // 判断相册是否存在
        boolean exists = photoAlbumMapper.exists(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getId, albumId));
        if (!exists) throw new BizException("相册id不存在");
        List<Picture> pictureList = picture.getPictureUrlList().stream().map(item -> Picture.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .albumId(albumId)
                .pictureName(IdWorker.getIdStr())
                .pictureSrc(item.getUrl())
                .width(item.getWidth())
                .height(item.getHeight())
                .size(item.getSize())
                .updateTime(CommonUtils.getLocalDateTime())
                .build()).collect(Collectors.toList());
        // 更新相册时间
        if (this.saveBatch(pictureList)) {
            photoAlbumMapper.updateById(PhotoAlbum.builder()
                    .id(albumId)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build());
        }
    }

    /**
     * @param pictureInfo
     * @description: 更新照片信息
     * @auther apecode
     * @date 2022/6/23 23:58
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePicture(PictureInfoVo pictureInfo) {
        Integer pictureId = SecurityUtils.decrypt(pictureInfo.getPicturePath());
        if (Objects.isNull(pictureId)) throw new BizException("图片id有误");
        // 判断图片是否存在
        Picture isPicture = pictureMapper.selectOne(Wrappers.<Picture>lambdaQuery().eq(Picture::getId, pictureId));
        if (Objects.isNull(isPicture)) throw new BizException("图片不存在");
        Picture picture = Picture.builder()
                .id(pictureId)
                .pictureName(pictureInfo.getPictureName())
                .build();
        // 更新相册时间
        if (this.updateById(picture)) {
            photoAlbumMapper.updateById(PhotoAlbum.builder()
                    .id(picture.getAlbumId())
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build());
        }
    }

    /**
     * @param pictureIds
     * @description: 删除照片
     * @auther apecode
     * @date 2022/6/24 0:07
     */
    @Override
    public void deletePicture(List<String> pictureIds) {
        List<Integer> ids = pictureIds.stream().map(id -> {
            Integer pictureId = SecurityUtils.decrypt(id);
            if (Objects.isNull(pictureId)) throw new BizException("图片id有误");
            return pictureId;
        }).collect(Collectors.toList());
        pictureMapper.deleteBatchIds(ids);
    }

    /**
     * @param picture
     * @description: 移动图片相册
     * @auther apecode
     * @date 2022/6/24 0:15
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePictureAlbum(PictureVo picture) {
        if (picture.getPictureIdList().isEmpty()) return;
        // 判断相册是否存在
        Integer albumId = SecurityUtils.decrypt(picture.getAlbumId());
        if (Objects.isNull(albumId)) throw new BizException("相册id有误");
        boolean exists = photoAlbumMapper.exists(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getId, albumId));
        if (!exists) throw new BizException("相册不存在");
        List<Picture> pictureList = picture.getPictureIdList().stream().map(item -> {
            Integer pictureId = SecurityUtils.decrypt(item);
            if (Objects.isNull(pictureId)) throw new BizException("图片id '" + item + "' 有误");
            return Picture.builder()
                    .id(pictureId)
                    .albumId(albumId)
                    .build();
        }).collect(Collectors.toList());
        if (this.updateBatchById(pictureList)) {
            // 更新相册时间
            photoAlbumMapper.updateById(PhotoAlbum.builder()
                    .id(albumId)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build());
        }
    }

    /**
     * @param delete
     * @description: 更新图片删除状态
     * @auther apecode
     * @date 2022/6/24 0:26
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePictureDelete(DeleteVo delete) {
        // 更新图片状态
        List<Picture> pictures = delete.getIdList().stream().map(item -> {
            Integer id = SecurityUtils.decrypt(item);
            if (Objects.isNull(id)) throw new BizException("图片id有误");
            return Picture.builder()
                    .id(id)
                    .isDelete(delete.getIsDelete())
                    .build();
        }).collect(Collectors.toList());
        this.updateBatchById(pictures);
        // 若恢复图片的相册已被删除，则恢复相册
        if (!delete.getIsDelete()) {
            List<Integer> idList = delete.getIdList().stream().map(id -> {
                        Integer pictureId = SecurityUtils.decrypt(id);
                        if (Objects.isNull(pictureId)) throw new BizException("图片id '" + id + "' 有误");
                        return pictureId;
                    }
            ).collect(Collectors.toList());
            List<PhotoAlbum> photoAlbumList = pictureMapper.selectList(Wrappers.<Picture>lambdaQuery().select(Picture::getAlbumId).in(Picture::getId, idList)
                    .groupBy(Picture::getAlbumId)).stream().map(item -> PhotoAlbum.builder()
                    .id(item.getAlbumId())
                    .isDelete(false)
                    .build()).collect(Collectors.toList());
            photoAlbumService.updateBatchById(photoAlbumList);
        }
    }

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 上传图片
     * @auther apecode
     * @date 2022/6/24 0:41
     */
    @Override
    public UploadFileInfoDto uploadPicture(MultipartFile file) {
        return uploadFileStrategyContext.executeUploadFileStrategy(file, FilePathEnum.ALBUM.getPath());
    }

    /**
     * @param photoAlbumId
     * @return {@link PageResultWithObject<PictureFrontDto, PhotoAlbumInfoDto>}
     * @description: 根据相册路径获取图片列表
     * @auther apecode
     * @date 2022/6/24 13:58
     */
    @Override
    public PageResultWithObject<PictureFrontDto, PhotoAlbumInfoDto> listPictureByPhotoAlbumId(String photoAlbumId) {
        // 判断相册是否存在
        Integer albumPath = SecurityUtils.decrypt(photoAlbumId);
        if (Objects.isNull(albumPath)) throw new BizException("相册id '" + photoAlbumId + "' 有误");
        PhotoAlbum photoAlbum = photoAlbumMapper.selectOne(Wrappers.<PhotoAlbum>lambdaQuery().eq(PhotoAlbum::getId, albumPath));
        if (Objects.isNull(photoAlbum)) throw new BizException("找不到该相册");
        PhotoAlbumInfoDto photoAlbumInfoDto = BeanCopyUtils.copyObject(photoAlbum, PhotoAlbumInfoDto.class);
        Page<Picture> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<Picture> pictureIPage = pictureMapper.selectPage(page, Wrappers.<Picture>lambdaQuery().eq(Picture::getAlbumId, photoAlbum.getId()).eq(Picture::getIsDelete, false).orderByDesc(Picture::getId));
        return new PageResultWithObject<>(BeanCopyUtils.copyList(pictureIPage.getRecords(), PictureFrontDto.class), (int) pictureIPage.getTotal(), photoAlbumInfoDto);
    }

    /**
     * @return {@link PageResult<PictureBackDto>}
     * @description: 获取回收站图片
     * @auther apecode
     * @date 2022/6/24 14:42
     */
    @Override
    public PageResult<PictureBackDto> listDeletePicture() {
        Page<Picture> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<PictureBackDto> pictureBackDtoIPage = pictureMapper.listDeletePicture(page);
        pictureBackDtoIPage.getRecords().stream().peek(picture -> picture.setId(SecurityUtils.encrypt(String.valueOf(picture.getId())))).collect(Collectors.toList());
        return new PageResult<>(pictureBackDtoIPage.getRecords(), (int) pictureBackDtoIPage.getTotal());
    }

    /**
     * @param path
     * @description: 物理删除图片
     * @auther apecode
     * @date 2022/7/15 13:24
     */
    @Override
    public void deletePicPermanently(List<String> path) {
        path.stream().peek(p -> {
            // 找到倒数第二个字符出现的位置
            int index = StringUtils.lastOrdinalIndexOf(p, "/", 2);
            if (index != -1) {
                String rePath = p.substring(index + 1);
                uploadFileStrategyContext.executeDeleteFileStrategy(rePath);
            }
        }).collect(Collectors.toList());
    }
}
