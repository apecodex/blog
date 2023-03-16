package cn.apecode.mapper;

import cn.apecode.dto.TagBackDto;
import cn.apecode.entity.Tag;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章标签 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * @description: 查询后台标签列表
     * @param page
     * @param condition
     * @return {@link IPage<TagBackDto>}
     * @auther apecode
     * @date 2022/6/25 19:50
    */
    IPage<TagBackDto> listTagBack(Page<Tag> page, @Param("condition") ConditionVo condition);

    /**
     * @description: 查询文章标签
     * @param id
     * @return {@link List<String>}
     * @auther apecode
     * @date 2022/7/5 13:58
    */
    List<String> listTagNameByArticleId(@Param("id") Integer id);

}
