package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.PhotoAlbumInfoDto;
import cn.apecode.blog.dto.PictureBackDto;
import cn.apecode.blog.dto.PictureFrontDto;
import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.service.PictureService;
import cn.apecode.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 图片 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "图片模块")
@RequiredArgsConstructor
@RestController
public class PictureController {

    private final PictureService pictureService;

    @ApiOperation(value = "根据相册路径获取图片列表（后台）", httpMethod = "GET")
    @GetMapping("/admin/pictures")
    public ResponseCode<PageResult<PictureBackDto>> listPictureByPhotoAlbumId(ConditionVo condition) {
        return ResponseCode.ok(pictureService.listPictureByPhotoAlbumId(condition));
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "保存图片", httpMethod = "POST")
    @PostMapping("/admin/picture")
    public ResponseCode<?> savePicture(@Validated @RequestBody PictureVo picture) {
        pictureService.savePicture(picture);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新图片信息", httpMethod = "PUT")
    @PutMapping("/admin/picture")
    public ResponseCode<?> updatePicture(@Validated PictureInfoVo pictureInfo) {
        pictureService.updatePicture(pictureInfo);
        return ResponseCode.ok("修改成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除图片", httpMethod = "DELETE")
    @DeleteMapping("/admin/picture")
    public ResponseCode<?> deletePicture(@RequestBody List<String> pictureIds) {
        pictureService.deletePicture(pictureIds);
        return ResponseCode.ok("删除成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "移动图片相册", httpMethod = "PUT")
    @PutMapping("/admin/pictures/album")
    public ResponseCode<?> updatePictureAlbum(@Validated @RequestBody PictureVo picture) {
        pictureService.updatePictureAlbum(picture);
        return ResponseCode.ok("修改成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新图片删除状态", httpMethod = "PUT")
    @PutMapping("/admin/pictures/delete")
    public ResponseCode<?> updatePictureDelete(@Validated @RequestBody DeleteVo delete) {
        pictureService.updatePictureDelete(delete);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "上传图片", httpMethod = "POST")
    @ApiImplicitParam(value = "上传图片", name = "file", required = true, dataTypeClass = MultipartFile.class)
    @PostMapping(value = "/admin/picture/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> uploadPicture(@RequestPart("file") MultipartFile file) {
        return ResponseCode.ok(pictureService.uploadPicture(file), "上传成功");
    }

    @ApiOperation(value = "根据相册路径获取图片列表", httpMethod = "GET")
    @ApiImplicitParam(name = "photoAlbumId", value = "相册id", required = true, dataTypeClass = String.class)
    @GetMapping("/album/{photoAlbumId}/pictures")
    public ResponseCode<PageResultWithObject<PictureFrontDto, PhotoAlbumInfoDto>> listPictureByPhotoAlbumId(@PathVariable String photoAlbumId) {
        return ResponseCode.ok(pictureService.listPictureByPhotoAlbumId(photoAlbumId));
    }

    @ApiOperation(value = "获取回收站图片", httpMethod = "GET")
    @GetMapping("/admin/pictures/delete")
    public ResponseCode<PageResult<PictureBackDto>> listDeletePicture() {
        return ResponseCode.ok(pictureService.listDeletePicture());
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除图片", httpMethod = "DELETE")
    @DeleteMapping("/admin/pictures/delete")
    public ResponseCode<?> deletePicPermanently(@RequestBody List<String> path) {
        pictureService.deletePicPermanently(path);
        return ResponseCode.ok("删除成功");
    }
}
