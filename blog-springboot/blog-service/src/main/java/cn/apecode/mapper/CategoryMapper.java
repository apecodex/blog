package cn.apecode.mapper;

import cn.apecode.dto.CategoryArticleCountDto;
import cn.apecode.dto.CategoryBackDto;
import cn.apecode.dto.CategoryFrontDto;
import cn.apecode.entity.Category;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章分类 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * @description: 分页查询分类列表
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List<CategoryBackDto>}
     * @auther apecode
     * @date 2022/7/4 18:27
    */
    List<CategoryBackDto> listCategoryBack(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);

    /**
     * @description: 获取分类列表
     * @return {@link List<CategoryFrontDto> }
     * @auther apecode
     * @date 2022/7/4 19:08
    */
    List<CategoryArticleCountDto>  listCategoryFront();

    /**
     * @description: 获取后台首页分类列表
     * @return {@link List<CategoryArticleCountDto>}
     * @auther apecode
     * @date 2022/8/23 16:29
    */
    List<CategoryArticleCountDto> listCategoryHome();
}
