package cn.apecode.blog.service;

import cn.apecode.blog.dto.CategoryBackDto;
import cn.apecode.blog.dto.CategoryFrontDto;
import cn.apecode.blog.dto.CategoryOptionDto;
import cn.apecode.blog.entity.Category;
import cn.apecode.blog.vo.CategoryVo;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章分类 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface CategoryService extends IService<Category> {

    /**
     * @description: 添加或修改分类
     * @param category
     * @auther apecode
     * @date 2022/6/26 16:31
    */
    void saveOrUpdateCategory(CategoryVo category);

    /**
     * @description: 删除分类
     * @param categoryId
     * @auther apecode
     * @date 2022/6/26 16:46
    */
    void deleteCategory(String categoryId);

    /**
     * @description: 搜索文章分类
     * @param condition
     * @return {@link List<CategoryOptionDto>}
     * @auther apecode
     * @date 2022/6/26 19:34
    */
    List<CategoryOptionDto> listCategoryFromSearch(ConditionVo condition);

    /**
     * @description: 获取后台分类列表
     * @param condition
     * @return {@link PageResult<CategoryBackDto>}
     * @auther apecode
     * @date 2022/7/4 18:54
    */
    PageResult<CategoryBackDto> listCategoryBack(ConditionVo condition);

    /**
     * @description: 获取分类列表
     * @return {@link CategoryFrontDto}
     * @auther apecode
     * @date 2022/7/4 18:57
    */
    CategoryFrontDto listCategoryFront();
}
