package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.LabelOptionDto;
import cn.apecode.blog.dto.UserMenuDto;
import cn.apecode.blog.entity.Menu;
import cn.apecode.blog.entity.RoleMenu;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.MenuMapper;
import cn.apecode.blog.mapper.RoleMenuMapper;
import cn.apecode.blog.service.MenuService;
import cn.apecode.blog.utils.BeanCopyUtils;
import cn.apecode.blog.utils.CommonUtils;
import cn.apecode.blog.utils.SecurityUtils;
import cn.apecode.blog.utils.UserUtils;
import cn.apecode.blog.vo.MenuVo;
import cn.apecode.blog.vo.ResponseCode;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * @return {@link ResponseCode<UserMenuDto>}
     * @description: 获取当前用户菜单
     * @auther apecode
     * @date 2022/6/6 21:00
     */
    @Override
    public List<UserMenuDto> listUserMenusByUserId() {
        // 根据用户id获取菜单
        List<Menu> menuList = menuMapper.listUserMenusByUserId(UserUtils.getLoginUser().getId());
        // 获取父菜单
        List<Menu> listCatalog = listCatalog(menuList);
        // 获取子菜单
        Map<Integer, List<Menu>> listChildMap = listChildMap(menuList);
        return getUserMenu(listCatalog, listChildMap);
    }

    /**
     * @return {@link List<UserMenuDto>}
     * @description: 获取菜单列表
     * @auther apecode
     * @date 2022/6/18 16:07
     */
    @Override
    public List<UserMenuDto> listMenu() {
        // 获取所有菜单
        List<Menu> menuList = menuMapper.selectList(Wrappers.<Menu>lambdaQuery());
        // 获取父菜单
        List<Menu> listCatalog = listCatalog(menuList);
        // 获取子菜单
        Map<Integer, List<Menu>> listChildMap = listChildMap(menuList);
        return getUserMenu(listCatalog, listChildMap);
    }

    /**
     * @param menu
     * @description: 保存或更新菜单
     * @auther apecode
     * @date 2022/6/18 17:06
     */
    @Override
    public void saveOrUpdateMenu(MenuVo menu) {
        Menu newMenu = BeanCopyUtils.copyObject(menu, Menu.class);
        if (StringUtils.isNoneBlank(menu.getId())) {
            Integer id = SecurityUtils.decrypt(menu.getId());
            if (Objects.isNull(id)) throw new BizException("菜单id有误");
            boolean exists = menuMapper.exists(Wrappers.<Menu>lambdaQuery().eq(Menu::getId, id));
            if (!exists) throw new BizException("菜单不存在");
            newMenu.setId(id);
        }
        if (StringUtils.isNoneBlank(menu.getParentId())) {
            Integer parentId = SecurityUtils.decrypt(menu.getParentId());
            if (Objects.isNull(parentId)) throw new BizException("父菜单id有误");
            boolean exists = menuMapper.exists(Wrappers.<Menu>lambdaQuery().eq(Menu::getId, parentId));
            if (!exists) throw new BizException("父菜单不存在");
            newMenu.setParentId(parentId);
        }
        newMenu.setUpdateTime(CommonUtils.getLocalDateTime());
        this.saveOrUpdate(newMenu);
    }

    /**
     * @param menuId
     * @description: 删除菜单
     * @auther apecode
     * @date 2022/6/18 18:00
     */
    @Override
    public void deleteMenu(String menuId) {
        Integer id = SecurityUtils.decrypt(menuId);
        if (Objects.isNull(id)) throw new BizException("菜单id有误");
        boolean exists = roleMenuMapper.exists(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getMenuId, id));
        if (exists) {
            throw new BizException("删除失败，此菜单有角色绑定");
        }
        // 通过id删除子菜单
        List<Integer> menuIdList = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().select(Menu::getId).eq(Menu::getParentId, id))
                .stream().map(Menu::getId).collect(Collectors.toList());
        menuIdList.add(id);
        menuMapper.deleteBatchIds(menuIdList);
    }

    /**
     * @return {@link List<LabelOptionDto>}
     * @description: 获取角色菜单选项
     * @auther apecode
     * @date 2022/6/18 22:07
     */
    @Override
    public List<LabelOptionDto> listRoleMenuOption() {
        // 获取所有菜单
        List<Menu> menuList = menuMapper.selectList(Wrappers.<Menu>lambdaQuery().select(Menu::getId, Menu::getTitle, Menu::getParentId, Menu::getOrderNum));
        // 菜单目录
        List<Menu> listCatalog = listCatalog(menuList);
        // 子菜单
        Map<Integer, List<Menu>> listChildMap = listChildMap(menuList);
        return listCatalog.stream().map(parentMenu -> {
            List<LabelOptionDto> labelOptionDtoList = new ArrayList<>();
            List<Menu> children = listChildMap.get(parentMenu.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                labelOptionDtoList = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDto.builder()
                                .id(SecurityUtils.encrypt(String.valueOf(menu.getId())))
                                .label(menu.getTitle())
                                .build()).collect(Collectors.toList());
            }
            return LabelOptionDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(parentMenu.getId())))
                    .label(parentMenu.getTitle())
                    .children(CollectionUtils.isEmpty(labelOptionDtoList) ? null : labelOptionDtoList)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * @param listCatalog
     * @param listChildMap
     * @return {@link ResponseCode<UserMenuDto>}
     * @description: 拼装菜单
     * @auther apecode
     * @date 2022/6/6 22:23
     */
    private List<UserMenuDto> getUserMenu(List<Menu> listCatalog, Map<Integer, List<Menu>> listChildMap) {
        List<UserMenuDto> menuDtoList = listCatalog.stream().map(item -> {
            UserMenuDto userMenuDto = BeanCopyUtils.copyObject(item, UserMenuDto.class);
            userMenuDto.setId(SecurityUtils.encrypt(String.valueOf(item.getId())));
            // 获取目录下的菜单
            if (Objects.nonNull(listChildMap.get(item.getId()))) {
                List<UserMenuDto> childrenMenuDtoList = listChildMap.get(item.getId()).stream().map(childMenu -> {
                    UserMenuDto menuDto = BeanCopyUtils.copyObject(childMenu, UserMenuDto.class);
                    menuDto.setId(SecurityUtils.encrypt(String.valueOf(childMenu.getId())));
                    menuDto.setParentId(SecurityUtils.encrypt(String.valueOf(childMenu.getParentId())));
                    return menuDto;
                }).collect(Collectors.toList());
                userMenuDto.setChildren(childrenMenuDtoList.isEmpty() ? null : childrenMenuDtoList);
                listChildMap.remove(item.getId());
            }
            return userMenuDto;
        }).sorted(Comparator.comparing(UserMenuDto::getOrderNum)).collect(Collectors.toList());
        // 如果还有剩余的，加在后面
        if (CollectionUtils.isNotEmpty(listChildMap)) {
            List<Menu> childrenList = new ArrayList<>();
            listChildMap.values().forEach(childrenList::addAll);
            List<UserMenuDto> childrenUserMenuDtos = childrenList.stream()
                    .map(item -> {
                        UserMenuDto userMenuDto = BeanCopyUtils.copyObject(item, UserMenuDto.class);
                        userMenuDto.setId(SecurityUtils.encrypt(String.valueOf(item.getId())));
                        userMenuDto.setParentId(SecurityUtils.encrypt(String.valueOf(item.getParentId())));
                        return userMenuDto;
                    })
                    .sorted(Comparator.comparing(UserMenuDto::getOrderNum))
                    .collect(Collectors.toList());
            menuDtoList.addAll(childrenUserMenuDtos);
        }
        return menuDtoList;
    }

    /**
     * @param menuList
     * @return {@link Map<Integer,List<Menu>>}
     * @description: 获取目录下菜单列表
     * @auther apecode
     * @date 2022/6/6 21:56
     */
    private Map<Integer, List<Menu>> listChildMap(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * @param menuList
     * @return {@link List<Menu>}
     * @description: 获取目录列表
     * @auther apecode
     * @date 2022/6/6 21:42
     */
    private List<Menu> listCatalog(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }
}
