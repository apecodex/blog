package cn.apecode.service;

import cn.apecode.dto.TagBackDto;
import cn.apecode.dto.TagDto;
import cn.apecode.dto.TagFrontDto;
import cn.apecode.entity.Tag;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.TagVo;
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
