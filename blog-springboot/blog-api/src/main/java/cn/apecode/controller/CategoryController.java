package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.CategoryBackDto;
import cn.apecode.dto.CategoryFrontDto;
import cn.apecode.dto.CategoryOptionDto;
import cn.apecode.service.CategoryService;
import cn.apecode.vo.CategoryVo;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "分类模块")
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类", httpMethod = "POST")
    @PostMapping("/admin/category")
    @Decrypt
    public ResponseCode<?> saveOrUpdateCategory(@Validated @RequestBody CategoryVo category) {
        categoryService.saveOrUpdateCategory(category);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除分类", httpMethod = "DELETE")
    @DeleteMapping("/admin/category")
    public ResponseCode<?> deleteCategory(@RequestBody String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "搜索文章分类", httpMethod = "GET")
    @GetMapping("/admin/category/search")
    @Encrypt
    public ResponseCode<List<CategoryOptionDto>> listCategoryFromSearch(ConditionVo condition) {
        return ResponseCode.ok(categoryService.listCategoryFromSearch(condition));
    }

    @ApiOperation(value = "获取后台分类列表", httpMethod = "GET")
    @GetMapping("/admin/categories")
    @Encrypt
    public ResponseCode<PageResult<CategoryBackDto>> listCategoryBack(ConditionVo condition) {
        return ResponseCode.ok(categoryService.listCategoryBack(condition));
    }

    @ApiOperation(value = "获取分类列表", httpMethod = "GET")
    @GetMapping("/categories")
    @Encrypt
    public ResponseCode<CategoryFrontDto> listCategoryFront() {
        return ResponseCode.ok(categoryService.listCategoryFront());
    }

}
