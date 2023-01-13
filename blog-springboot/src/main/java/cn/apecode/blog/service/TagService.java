package cn.apecode.blog.service;

import cn.apecode.blog.dto.TagBackDto;
import cn.apecode.blog.dto.TagDto;
import cn.apecode.blog.dto.TagFrontDto;
import cn.apecode.blog.entity.Tag;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.TagVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章标签 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface TagService extends IService<Tag> {

    /**
     * @description: 添加标签
     * @param tagsName
     * @return {@link List<TagDto>}
     * @auther apecode
     * @date 2022/6/25 17:43
    */
    List<TagDto> saveTags(List<String> tagsName);

    /**
     * @description: 查询后台标签列表
     * @param condition
     * @return {@link PageResult<TagBackDto>}
     * @auther apecode
     * @date 2022/6/25 19:47
    */
    PageResult<TagBackDto> listTagBack(ConditionVo condition);

    /**
     * @description: 删除标签
     * @param tagId
     * @auther apecode
     * @date 2022/6/25 20:00
    */
    void deleteTag(String tagId);

    /**
     * @description: 搜索文章标签
     * @param condition
     * @return {@link List<TagDto>}
     * @auther apecode
     * @date 2022/6/25 23:25
    */
    List<TagDto> listTagFromSearch(ConditionVo condition);

    /**
     * @description: 查询标签列表
     * @return {@link TagFrontDto}
     * @auther apecode
     * @date 2022/6/25 23:55
    */
    TagFrontDto listTagFront();

    /**
     * @description: 修改标签
     * @param tag
     * @auther apecode
     * @date 2022/6/26 0:07
     */
    void updateTag(TagVo tag);

}
