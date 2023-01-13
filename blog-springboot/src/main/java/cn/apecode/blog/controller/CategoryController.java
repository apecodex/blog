package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.CategoryBackDto;
import cn.apecode.blog.dto.CategoryFrontDto;
import cn.apecode.blog.dto.CategoryOptionDto;
import cn.apecode.blog.service.CategoryService;
import cn.apecode.blog.vo.CategoryVo;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类", httpMethod = "POST")
    @PostMapping("/admin/category")
    public ResponseCode<?> saveOrUpdateCategory(@Validated CategoryVo category) {
        categoryService.saveOrUpdateCategory(category);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除分类", httpMethod = "DELETE")
    @DeleteMapping("/admin/category")
    public ResponseCode<?> deleteCategory(String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "搜索文章分类", httpMethod = "GET")
    @GetMapping("/admin/category/search")
    public ResponseCode<List<CategoryOptionDto>> listCategoryFromSearch(ConditionVo condition) {
        return ResponseCode.ok(categoryService.listCategoryFromSearch(condition));
    }

    @ApiOperation(value = "获取后台分类列表", httpMethod = "GET")
    @GetMapping("/admin/categories")
    public ResponseCode<PageResult<CategoryBackDto>> listCategoryBack(ConditionVo condition) {
        return ResponseCode.ok(categoryService.listCategoryBack(condition));
    }

    @ApiOperation(value = "获取分类列表", httpMethod = "GET")
    @GetMapping("/categories")
    public ResponseCode<CategoryFrontDto> listCategoryFront() {
        return ResponseCode.ok(categoryService.listCategoryFront());
    }

}
