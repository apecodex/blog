package cn.apecode.mapper;

import cn.apecode.dto.MessageBackDto;
import cn.apecode.dto.MessageFrontDto;
import cn.apecode.entity.Message;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 留言 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * @description: 获取后台留言列表
     * @param page
     * @param condition
     * @return {@link IPage<MessageBackDto>}
     * @auther apecode
     * @date 2022/6/24 16:28
    */
    IPage<MessageBackDto> listMessageBack(Page<Message> page, @Param("condition") ConditionVo condition);

    /**
     * @description: 获取留言列表
     * @param page
     * @param condition
     * @return {@link IPage<MessageFrontDto>}
     * @auther apecode
     * @date 2022/6/24 18:58
    */
    IPage<MessageFrontDto> listMessageFront(Page<Message> page, @Param("condition") ConditionVo condition);
}
