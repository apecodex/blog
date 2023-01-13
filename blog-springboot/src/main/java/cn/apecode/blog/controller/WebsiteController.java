package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.BlogBackInfoDto;
import cn.apecode.blog.dto.BlogHomeInfoDto;
import cn.apecode.blog.dto.UploadFileInfoDto;
import cn.apecode.blog.service.WebsiteService;
import cn.apecode.blog.vo.ResponseCode;
import cn.apecode.blog.vo.WebsiteConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.blog.constant.OptTypeConst.UPDATE;

/**
 * <p>
 * 博客设置 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "博客信息模块")
@RestController
public class WebsiteController {

    @Autowired
    private WebsiteService websiteService;

    @ApiOperation(value = "获取后台信息", httpMethod = "GET")
    @GetMapping("/admin")
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
    public ResponseCode<?> updateWebsiteConfigure(@RequestBody WebsiteConfigVo websiteConfig) {
        websiteService.updateWebsiteConfigure(websiteConfig);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "获取网站配置", httpMethod = "GET")
    @GetMapping("/admin/website/config")
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
    public ResponseCode<BlogHomeInfoDto> getBlogInfo() {
        return ResponseCode.ok(websiteService.getBlogInfo());
    }
}
