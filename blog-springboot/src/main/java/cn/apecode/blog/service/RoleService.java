package cn.apecode.blog.service;

import cn.apecode.blog.dto.RoleDto;
import cn.apecode.blog.dto.UserRoleDto;
import cn.apecode.blog.entity.Role;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.RoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface RoleService extends IService<Role> {

    /**
     * @description: 保存或更新角色
     * @param roleVo
     * @auther apecode
     * @date 2022/6/16 15:26
    */
    void saveOrUpdateRole(RoleVo roleVo);

    /**
     * @description: 查询角色列表
     * @param condition
     * @return {@link PageResult<RoleDto>}
     * @auther apecode
     * @date 2022/6/16 19:57
    */
    PageResult<RoleDto> listRole(ConditionVo condition);

    /**
     * @description: 删除角色
     * @param roleId
     * @auther apecode
     * @date 2022/6/16 23:45
    */
    void deleteRoleByRoleId(String roleId);

    /**
     * @description: 查询用户角色选项
     * @param
     * @return {@link List<UserRoleDto>}
     * @auther apecode
     * @date 2022/6/17 0:08
    */
     List<UserRoleDto> listUserRole();
}
