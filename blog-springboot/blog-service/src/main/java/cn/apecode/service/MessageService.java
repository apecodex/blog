package cn.apecode.service;

import cn.apecode.dto.MessageBackDto;
import cn.apecode.dto.MessageFrontDto;
import cn.apecode.entity.Message;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.MessageVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.ReviewVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 留言 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface MessageService extends IService<Message> {

    /**
     * @description: 获取后台留言列表
     * @param condition
     * @return {@link PageResult<MessageBackDto>}
     * @auther apecode
     * @date 2022/6/24 16:23
    */
    PageResult<MessageBackDto> listMessageBack(ConditionVo condition);

    /**
     * @description: 添加留言
     * @param message
     * @auther apecode
     * @date 2022/6/24 16:36
    */
    void saveMessage(MessageVo message);

    /**
     * @description: 删除留言
     * @param ids
     * @auther apecode
     * @date 2022/6/24 18:36
    */
    void deleteMessage(List<String> ids);

    /**
     * @description: 审核留言
     * @param review
     * @auther apecode
     * @date 2022/6/24 18:42
    */
    void updateMessageReview(ReviewVo review);

    /**
     * @description: 获取留言列表
     * @param condition
     * @return {@link PageResult<MessageFrontDto>}
     * @auther apecode
     * @date 2022/6/24 18:54
    */
    PageResult<MessageFrontDto> listMessageFront(ConditionVo condition);
}
