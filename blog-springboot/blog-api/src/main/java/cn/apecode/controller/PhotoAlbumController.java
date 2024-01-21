package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.PhotoAlbumBackDto;
import cn.apecode.dto.PhotoAlbumFrontDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.common.enums.FilePathEnum;
import cn.apecode.service.PhotoAlbumService;
import cn.apecode.strategy.context.UploadFileStrategyContext;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.PhotoAlbumVo;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "相册模块")
@RequiredArgsConstructor
@RestController
public class PhotoAlbumController {

    private final PhotoAlbumService photoAlbumService;
    private final UploadFileStrategyContext uploadFileStrategyContext;

    @ApiOperation(value = "获取后台相册列表", httpMethod = "GET")
    @GetMapping("/admin/albums")
    @Encrypt
    public ResponseCode<PageResult<PhotoAlbumBackDto>> listPhotoAlbumBack(ConditionVo condition) {
        return ResponseCode.ok(photoAlbumService.listPhotoAlbumBack(condition));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "添加相册", httpMethod = "POST")
    @PostMapping("/admin/album")
    @Decrypt
    public ResponseCode<?> savePhotoAlbum(@Validated @RequestBody PhotoAlbumVo photoAlbum) {
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
    @Encrypt
    public ResponseCode<PageResult<PhotoAlbumFrontDto>> listPhotoAlbumFront(ConditionVo condition) {
        return ResponseCode.ok(photoAlbumService.listPhotoAlbumFront(condition));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改相册", httpMethod = "PUT")
    @PutMapping("/admin/album")
    @Decrypt
    public ResponseCode<?> updatePhotoAlbum(@Validated @RequestBody PhotoAlbumVo photoAlbum) {
        photoAlbumService.updatePhotoAlbum(photoAlbum);
        return ResponseCode.ok("修改成功");
    }
}
