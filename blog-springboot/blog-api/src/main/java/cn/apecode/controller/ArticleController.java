package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.*;
import cn.apecode.service.ArticleService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 文章信息 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "文章模块")
@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章", httpMethod = "POST")
    @PostMapping("/admin/article")
    @Decrypt
    public ResponseCode<?> saveOrUpdateArticle(@Validated @RequestBody ArticleVo article) {
        articleService.saveOrUpdateArticle(article);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "恢复或删除文章", httpMethod = "PUT")
    @PutMapping("/admin/article")
    public ResponseCode<?> updateArticleDelete(@Validated @RequestBody DeleteVo delete) {
        articleService.updateArticleDelete(delete);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "物理删除文章", httpMethod = "DELETE")
    @DeleteMapping("/admin/article")
    public ResponseCode<?> deleteArticle(@RequestBody List<String> articleIds) {
        articleService.deleteArticle(articleIds);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "上传文章封面", httpMethod = "POST")
    @PostMapping(value = "/admin/article/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> uploadArticleCover(@ApiParam(value = "上传文章封面", required = true) @RequestPart("file") MultipartFile file) {
        return ResponseCode.ok(articleService.uploadArticleCover(file), "上传成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改文章置顶", httpMethod = "PUT")
    @PutMapping("/admin/article/top")
    public ResponseCode<?> updateArticleTop(@Validated TopVo top) {
        articleService.updateArticleTop(top);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "获取文章归档", httpMethod = "GET")
    @GetMapping("/article/archives")
    @Encrypt
    public ResponseCode<List<ArchiveMonthDto>> listArchives() {
        return ResponseCode.ok(articleService.listArchives());
    }

    @ApiOperation(value = "点赞文章", httpMethod = "POST")
    @ApiImplicitParam(value = "文章id", name = "articleId", required = true, dataTypeClass = String.class)
    @PostMapping("/articles/{articleId}/like")
    public ResponseCode<?> saveArticleLike(@PathVariable String articleId) {
        articleService.saveArticleLike(articleId);
        return ResponseCode.ok("保存成功");
    }

    @ApiOperation(value = "根据id获取文章", httpMethod = "GET")
    @ApiImplicitParam(value = "文章id", name = "articleId", dataTypeClass = String.class, required = true)
    @GetMapping("/article/{articleId}")
    @Encrypt
    public ResponseCode<ArticleDto> getArticleByArticleId(@PathVariable String articleId) {
        return ResponseCode.ok(articleService.getArticleByArticleId(articleId));
    }

    @ApiOperation(value = "获取首页文章", httpMethod = "GET")
    @GetMapping("/articles")
    @Encrypt
    public ResponseCode<PageResult<ArticleHomeDto>> listArticle() {
        return ResponseCode.ok(articleService.listArticle());
    }

    @ApiOperation(value = "根据id获取后台文章", httpMethod = "GET")
    @ApiImplicitParam(value = "文章id", name = "articleId", dataTypeClass = String.class, required = true)
    @GetMapping("/admin/article/{articleId}")
    @Encrypt
    public ResponseCode<ArticleVo> getArticleBackByArticleId(@PathVariable String articleId) {
        return ResponseCode.ok(articleService.getArticleBackByArticleId(articleId));
    }

    @ApiOperation(value = "获取后台文章", httpMethod = "GET")
    @GetMapping("/admin/articles")
    @Encrypt
    public ResponseCode<PageResult<ArticleBackDto>> listArticleBack(ConditionVo condition) {
        return ResponseCode.ok(articleService.listArticleBack(condition));
    }

    @ApiOperation(value = "搜索", httpMethod = "GET")
    @GetMapping("/search")
    public ResponseCode<SearchDto> search(String keywords) {
        return ResponseCode.ok(articleService.search(keywords));
    }

    @ApiOperation(value = "根据条件搜索文章", httpMethod = "GET")
    @GetMapping("/article/search/condition")
    public ResponseCode<PageResultWithObject<ConditionSearchArticleDto, String>> getArticlesByCondition(ConditionVo conditionVo) {
        return ResponseCode.ok(articleService.getArticlesByCondition(conditionVo));
    }
}
