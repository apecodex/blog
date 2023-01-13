package cn.apecode.blog.mapper;

import cn.apecode.blog.entity.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

}
