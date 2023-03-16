package cn.apecode.mapper;

import cn.apecode.dto.TalkBackDto;
import cn.apecode.dto.TalkBackOnlyDto;
import cn.apecode.dto.TalkFrontDto;
import cn.apecode.entity.Talk;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 说说 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@Mapper
public interface TalkMapper extends BaseMapper<Talk> {

    /**
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List<TalkBackDto>}
     * @description: 查看后台说说
     * @auther apecode
     * @date 2022/7/7 15:13
     */
    List<TalkBackDto> listTalkBack(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);

    /**
     * @description: 查看说说列表
     * @return {@link List<TalkFrontDto>}
     * @auther apecode
     * @date 2022/7/7 16:50
    */
    List<TalkFrontDto> listTalkFront(@Param("current") Long limitCurrent, @Param("size") Long size);

    /**
     * @description: 根据id查看说说
     * @param id
     * @return {@link TalkFrontDto}
     * @auther apecode
     * @date 2022/7/7 17:14
    */
    TalkFrontDto getTalkById(@Param("id") Integer id);

    /**
     * @description: 根据id获取后台说说
     * @param talkId
     * @return {@link TalkBackOnlyDto}
     * @auther apecode
     * @date 12/9/2022 PM2:44
    */
    TalkBackOnlyDto getTalkBackOnlyById(Integer talkId);
}
