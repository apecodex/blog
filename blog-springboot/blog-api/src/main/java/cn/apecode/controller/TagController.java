package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.TagBackDto;
import cn.apecode.dto.TagDto;
import cn.apecode.dto.TagFrontDto;
import cn.apecode.service.TagService;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.vo.TagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 文章标签 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "标签模块")
@RequiredArgsConstructor
@RestController
public class TagController {

    private final TagService tagService;

    @OptLog(optType = SAVE)
    @ApiOperation(value = "添加标签", httpMethod = "POST")
    @PostMapping("/admin/tags")
    @Decrypt
    public ResponseCode<List<TagDto>> saveTag(@RequestBody List<String> tagsName) {
        return ResponseCode.ok(tagService.saveTags(tagsName));
    }

    @ApiOperation(value = "查询后台标签列表", httpMethod = "GET")
    @GetMapping("/admin/tags")
    @Encrypt
    public ResponseCode<PageResult<TagBackDto>> listTagBack(ConditionVo condition) {
        return ResponseCode.ok(tagService.listTagBack(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除标签", httpMethod = "DELETE")
    @ApiImplicitParam(value = "标签id", name = "tagId", required = true, dataTypeClass = String.class)
    @NotNull(message = "标签id不能为空")
    @DeleteMapping("/admin/tags")
    public ResponseCode<?> deleteTag(@RequestBody String tagId) {
        tagService.deleteTag(tagId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "搜索文章标签", httpMethod = "GET")
    @GetMapping("/tags/search")
    public ResponseCode<List<TagDto>> listTagFromSearch(ConditionVo condition) {
        return ResponseCode.ok(tagService.listTagFromSearch(condition));
    }

    @ApiOperation(value = "查询标签列表", httpMethod = "GET")
    @GetMapping("/tags")
    @Encrypt
    public ResponseCode<TagFrontDto> listTagFront() {
        return ResponseCode.ok(tagService.listTagFront());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改标签", httpMethod = "PUT")
    @PutMapping("/admin/tags")
    @Decrypt
    public ResponseCode<?> updateTag(@Validated @RequestBody TagVo tag) {
        tagService.updateTag(tag);
        return ResponseCode.ok("修改成功");
    }

}
