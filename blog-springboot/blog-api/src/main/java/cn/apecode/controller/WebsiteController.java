package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.BlogBackInfoDto;
import cn.apecode.dto.BlogHomeInfoDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.service.WebsiteService;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.vo.WebsiteConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.common.constant.OptTypeConst.UPDATE;

/**
 * <p>
 * 博客设置 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "博客信息模块")
@RequiredArgsConstructor
@RestController
public class WebsiteController {

    private final WebsiteService websiteService;

    @ApiOperation(value = "获取后台信息", httpMethod = "GET")
    @GetMapping("/admin")
    @Encrypt
    public ResponseCode<BlogBackInfoDto> getBlogBackInfo() {
        return ResponseCode.ok(websiteService.getBlogBackInfo());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改关于我信息", httpMethod = "PUT")
    @PutMapping("/admin/about")
    public ResponseCode<?> updateAboutMe(String aboutMe) {
        websiteService.updateAboutMe(aboutMe);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "获取关于我信息", httpMethod = "GET")
    @GetMapping("/about")
    public ResponseCode<String> getAboutMe() {
        return ResponseCode.ok(websiteService.getAboutMe(), "获取成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新网站配置", httpMethod = "PUT")
    @PutMapping("/admin/website/config")
    @Decrypt
    public ResponseCode<?> updateWebsiteConfigure(@RequestBody WebsiteConfigVo websiteConfig) {
        websiteService.updateWebsiteConfigure(websiteConfig);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "获取网站配置", httpMethod = "GET")
    @GetMapping("/admin/website/config")
    @Encrypt
    public ResponseCode<WebsiteConfigVo> getWebsiteConfigure() {
        return ResponseCode.ok(websiteService.getWebsiteConfigure());
    }

    @ApiOperation(value = "上传博客配置图片", httpMethod = "POST")
    @ApiImplicitParam(value = "上传图片", name = "file", required = true, dataTypeClass = MultipartFile.class)
    @PostMapping(value = "/admin/config/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> uploadWebSiteConfigPic(@RequestPart("file") MultipartFile file) {
        return ResponseCode.ok(websiteService.uploadWebSiteConfigPic(file), "上传成功");
    }

    @ApiOperation(value = "获取博客信息", httpMethod = "GET")
    @GetMapping("/")
    @Encrypt
    public ResponseCode<BlogHomeInfoDto> getBlogInfo() {
        return ResponseCode.ok(websiteService.getBlogInfo());
    }
}
