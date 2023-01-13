package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.PhotoAlbumBackDto;
import cn.apecode.blog.dto.PhotoAlbumFrontDto;
import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.enums.FilePathEnum;
import cn.apecode.blog.service.PhotoAlbumService;
import cn.apecode.blog.strategy.context.UploadFileStrategyContext;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.PhotoAlbumVo;
import cn.apecode.blog.vo.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "相册模块")
@RestController
public class PhotoAlbumController {

    @Autowired
    private PhotoAlbumService photoAlbumService;
    @Autowired
    private UploadFileStrategyContext uploadFileStrategyContext;

    @ApiOperation(value = "获取后台相册列表", httpMethod = "GET")
    @GetMapping("/admin/albums")
    public ResponseCode<PageResult<PhotoAlbumBackDto>> listPhotoAlbumBack(ConditionVo condition) {
        return ResponseCode.ok(photoAlbumService.listPhotoAlbumBack(condition));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "添加相册", httpMethod = "POST")
    @PostMapping("/admin/album")
    public ResponseCode<?> savePhotoAlbum(@Validated PhotoAlbumVo photoAlbum) {
        photoAlbumService.savePhotoAlbum(photoAlbum);
        return ResponseCode.ok("添加成功");
    }

    @ApiOperation(value = "上传相册封面", httpMethod = "POST")
    @PostMapping(value = "/admin/album/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> updatePhotoAlbumCover(@ApiParam(value = "上传相册封面", required = true) @RequestPart("file") MultipartFile file) {
        return ResponseCode.ok(uploadFileStrategyContext.executeUploadFileStrategy(file, FilePathEnum.ALBUM.getPath()), "上传成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "根据id删除相册", httpMethod = "DELETE")
    @ApiImplicitParam(name = "photoAlbumId", value = "相册id", required = true, dataTypeClass = String.class)
    @DeleteMapping("/admin/picture/album/{photoAlbumId}")
    public ResponseCode<?> deletePhotoAlbumById(@PathVariable String photoAlbumId) {
        photoAlbumService.deletePhotoAlbumById(photoAlbumId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取相册列表", httpMethod = "GET")
    @GetMapping("/albums")
    public ResponseCode<PageResult<PhotoAlbumFrontDto>> listPhotoAlbumFront(ConditionVo condition) {
        return ResponseCode.ok(photoAlbumService.listPhotoAlbumFront(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改相册", httpMethod = "PUT")
    @PutMapping("/admin/album")
    public ResponseCode<?> updatePhotoAlbum(@Validated PhotoAlbumVo photoAlbum) {
        photoAlbumService.updatePhotoAlbum(photoAlbum);
        return ResponseCode.ok("修改成功");
    }
}
