package cn.apecode.service;

import cn.apecode.common.utils.ResponseCode;
import cn.apecode.dto.LabelOptionDto;
import cn.apecode.dto.UserMenuDto;
import cn.apecode.entity.Menu;
import cn.apecode.vo.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台菜单 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface MenuService extends IService<Menu> {

    /**
     * @return {@link ResponseCode<UserMenuDto>}
     * @description: 获取当前用户菜单
     * @auther apecode
     * @date 2022/6/6 21:00
     */
    List<UserMenuDto> listUserMenusByUserId();

    /**
     * @return {@link List<UserMenuDto>}
     * @description: 获取菜单列表
     * @auther apecode
     * @date 2022/6/18 16:07
     */
    List<UserMenuDto> listMenu();

    /**
     * @param menu
     * @description: 保存或更新菜单
     * @auther apecode
     * @date 2022/6/18 17:05
     */
    void saveOrUpdateMenu(MenuVo menu);

    /**
     * @param id
     * @description: 删除菜单
     * @auther apecode
     * @date 2022/6/18 18:00
     */
    void deleteMenu(String id);

    /**
     * @return {@link List<LabelOptionDto>}
     * @description: 获取角色菜单选项
     * @auther apecode
     * @date 2022/6/18 22:06
     */
    List<LabelOptionDto> listRoleMenuOption();
}
