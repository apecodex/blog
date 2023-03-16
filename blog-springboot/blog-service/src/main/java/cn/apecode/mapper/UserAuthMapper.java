package cn.apecode.mapper;

import cn.apecode.dto.UserInfoBackDto;
import cn.apecode.entity.UserAuth;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户账号 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * @description: 获取后台用户数量
     * @param conditionVo
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/6/9 12:19
    */
    Integer userCount(@Param("conditionVo") ConditionVo conditionVo);

    /**
     * @description: 获取后台用户列表
     * @param limitCurrent
     * @param size
     * @param conditionVo
     * @return {@link List<UserInfoBackDto>}
     * @auther apecode
     * @date 2022/6/9 13:11
    */
    List<UserInfoBackDto> listUsers(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo conditionVo);
}
