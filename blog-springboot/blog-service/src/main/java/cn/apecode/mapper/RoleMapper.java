package cn.apecode.mapper;

import cn.apecode.dto.RoleDto;
import cn.apecode.dto.RoleResourceDto;
import cn.apecode.entity.Role;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * @description: 查询账号角色
     * @param id
     * @return {@link List<String>}
     * @auther apecode
     * @date 2022/5/27 18:44
    */
    List<String> ListRoleByUserId(Integer id);

    /**
     * @description: 加载资源角色信息
     * @return {@link List<RoleResourceDto>}
     * @auther apecode
     * @date 2022/5/28 19:39
    */
    List<RoleResourceDto> listRoleResource();

    /**
     * @description: 查询角色列表
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List<RoleDto>}
     * @auther apecode
     * @date 2022/6/16 20:00
    */
    List<RoleDto> listRole(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);
}
