package cn.apecode.mapper;

import cn.apecode.dto.TagWithArticleCountDto;
import cn.apecode.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 文章所属标签 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    /**
     * @description: 获取前台标签及文章数量
     * @param
     * @return {@link List<TagWithArticleCountDto>}
     * @auther apecode
     * @date 20/3/2023 PM1:37
    */
    List<TagWithArticleCountDto> listTagFront();
}
