package cn.apecode.service;

import cn.apecode.dto.FriendLinkBackDto;
import cn.apecode.dto.FriendLinkFrontDto;
import cn.apecode.entity.FriendLink;
import cn.apecode.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 友情链接 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * @description: 保存或修改友链
     * @param friendLink
     * @auther apecode
     * @date 2022/6/24 21:23
    */
    void saveOrUpdateFriendLink(FriendLinkVo friendLink);

    /**
     * @description: 删除友链
     * @param friendLinkId
     * @auther apecode
     * @date 2022/6/24 22:23
    */
    void deleteFriendLink(String friendLinkId);

    /**
     * @description: 获取后台友链列表
     * @param condition
     * @return {@link PageResult<FriendLinkBackDto>}
     * @auther apecode
     * @date 2022/6/24 22:29
    */
    PageResult<FriendLinkBackDto> listFriendLinkBack(ConditionVo condition);

    /**
     * @description: 获取友链列表
     * @return {@link PageResult<FriendLinkFrontDto>}
     * @auther apecode
     * @date 2022/6/24 22:58
    */
    PageResult<FriendLinkFrontDto> listFriendLinkFront();

    /**
     * @description: 用户保存或修改友链
     * @param friendLink
     * @auther apecode
     * @date 2022/6/24 23:21
    */
    void saveOrUpdateFriendLinkByUser(FriendLinkUserVo friendLink);

    /**
     * @description: 用户删除友链
     * @param friendLinkId
     * @auther apecode
     * @date 2022/6/24 23:37
    */
    void deleteFriendLinkById(String friendLinkId);

    /**
     * @description: 根据用户id获取用户友链
     * @return {@link List<FriendLinkBackDto>}
     * @auther apecode
     * @date 2022/6/25 0:12
    */
    List<FriendLinkBackDto> getFriendLinkByUserInfoId();

    /**
     * @description: 审核友链
     * @param review
     * @auther apecode
     * @date 2022/6/25 0:27
    */
    void reviewFriendLinkByIds(ReviewVo review);
}
