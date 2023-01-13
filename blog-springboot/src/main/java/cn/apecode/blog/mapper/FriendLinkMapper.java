package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.FriendLinkBackDto;
import cn.apecode.blog.dto.FriendLinkFrontDto;
import cn.apecode.blog.entity.FriendLink;
import cn.apecode.blog.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 友情链接 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {

    /**
     * @description: 获取后台友链列表
     * @param page
     * @param condition
     * @return {@link IPage<FriendLinkBackDto>}
     * @auther apecode
     * @date 2022/6/24 22:31
    */
    IPage<FriendLinkBackDto> listFriendLinkBack(Page<FriendLink> page, @Param("condition") ConditionVo condition);

    /**
     * @description: 获取友链列表
     * @param page
     * @return {@link IPage<FriendLinkFrontDto>}
     * @auther apecode
     * @date 2022/6/24 22:59
    */
    IPage<FriendLinkFrontDto> listFriendLinkFront(Page<FriendLink> page);
}
