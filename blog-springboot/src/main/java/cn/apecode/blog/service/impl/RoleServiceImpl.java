package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.RoleDto;
import cn.apecode.blog.dto.UserRoleDto;
import cn.apecode.blog.entity.Role;
import cn.apecode.blog.entity.RoleMenu;
import cn.apecode.blog.entity.RoleResource;
import cn.apecode.blog.entity.UserRole;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import cn.apecode.blog.mapper.RoleMapper;
import cn.apecode.blog.mapper.UserRoleMapper;
import cn.apecode.blog.service.RoleMenuService;
import cn.apecode.blog.service.RoleResourceService;
import cn.apecode.blog.service.RoleService;
import cn.apecode.blog.utils.CommonUtils;
import cn.apecode.blog.utils.PageUtils;
import cn.apecode.blog.utils.SecurityUtils;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.RoleVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleResourceService roleResourceService;
    private final RoleMenuService roleMenuService;
    private final UserRoleMapper userRoleMapper;
    private final FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    /**
     * @description: 保存或更新角色
     * @param roleVo
     * @auther apecode
     * @date 2022/6/16 15:26
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVo roleVo) {
        Role role = Role.builder()
                .roleAuth(roleVo.getRoleAuth())
                .roleDesc(roleVo.getRoleDesc())
                .isEnable(roleVo.getIsEnable())
                .build();
        Integer roleId = null;
        if (StringUtils.isNotBlank(roleVo.getRoleId())) {
            roleId = SecurityUtils.decrypt(roleVo.getRoleId());
            if (Objects.isNull(roleId)) throw new BizException("角色id有误");
            role.setId(roleId);
        } else {
            boolean exists = roleMapper.exists(Wrappers.<Role>lambdaQuery().select(Role::getId).eq(Role::getRoleAuth, roleVo.getRoleAuth()));
            if (exists) {
                throw new BizException("角色名已存在");
            }
            role.setUpdateTime(CommonUtils.getLocalDateTime());
        }
        // 保存或更新角色
        this.saveOrUpdate(role);
        // 更新资源
        if (Objects.nonNull(roleVo.getResourceIdList())) {
            if (Objects.nonNull(roleId)) {
                roleResourceService.remove(Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getRoleId, roleId));
            }
            List<RoleResource> roleResourceList = roleVo.getResourceIdList().stream()
                    .map(resourceId -> {
                        Integer rId = SecurityUtils.decrypt(resourceId);
                        if (Objects.isNull(rId)) throw new BizException("资源id有误");
                        return RoleResource.builder()
                                .roleId(role.getId())
                                .resourceId(rId)
                                .updateTime(CommonUtils.getLocalDateTime())
                                .build();
                    }).collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource();
        }
        // 更新菜单
        if (Objects.nonNull(roleVo.getMenuIdList())) {
            if (Objects.nonNull(roleId)) {
                roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
            }
            List<RoleMenu> roleMenuList = roleVo.getMenuIdList().stream()
                    .map(menuId -> {
                        Integer mId = SecurityUtils.decrypt(menuId);
                        if (Objects.isNull(mId)) throw new BizException("菜单id有误");
                        return RoleMenu.builder()
                                .roleId(role.getId())
                                .menuId(mId)
                                .updateTime(CommonUtils.getLocalDateTime())
                                .build();
                    }).collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    /**
     * @description: 查询角色列表
     * @param condition
     * @return {@link PageResult<RoleDto>}
     * @auther apecode
     * @date 2022/6/16 19:57
    */
    @Override
    public PageResult<RoleDto> listRole(ConditionVo condition) {
        // 查询角色列表
        List<RoleDto> roleDtoList = roleMapper.listRole(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        roleDtoList.stream().peek(role -> {
            role.setId(SecurityUtils.encrypt(role.getId()));
            List<String> resourceIds = role.getResourceIdList().stream().map(SecurityUtils::encrypt).collect(Collectors.toList());
            List<String> menuIds = role.getMenuIdList().stream().map(SecurityUtils::encrypt).collect(Collectors.toList());
            role.setResourceIdList(resourceIds);
            role.setMenuIdList(menuIds);
        }).collect(Collectors.toList());
        // 查询总量
        Long count = roleMapper.selectCount(Wrappers.<Role>lambdaQuery().like(StringUtils.isNoneBlank(condition.getKeywords()), Role::getRoleDesc, condition.getKeywords()));
        return new PageResult<>(roleDtoList, count.intValue());
    }

    /**
     * @description: 删除角色
     * @param roleId
     * @auther apecode
     * @date 2022/6/16 23:45
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoleByRoleId(String roleId) {
        Integer id = SecurityUtils.decrypt(roleId);
        if (Objects.isNull(id)) throw new BizException("角色id有误");
        boolean exists = userRoleMapper.exists(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, id));
        if (exists) {
            throw new BizException("删除失败，此角色下有用户绑定");
        }
        // 删除角色资源
        roleResourceService.remove(Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getRoleId, id));
        // 删除角色菜单
        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id));
        // 删除角色
        roleMapper.deleteById(id);
    }

    /**
     * @description: 查询用户角色选项
     * @return {@link List<UserRoleDto>}
     * @auther apecode
     * @date 2022/6/17 0:08
    */
    @Override
    public List<UserRoleDto> listUserRole() {
        List<Role> roleList = roleMapper.selectList(Wrappers.<Role>lambdaQuery().select(Role::getId, Role::getRoleDesc).eq(Role::getIsEnable, true));
        return roleList.stream().map(role -> UserRoleDto.builder()
                .id(SecurityUtils.encrypt(String.valueOf(role.getId())))
                .roleDesc(role.getRoleDesc())
                .build()).collect(Collectors.toList());
    }
}
