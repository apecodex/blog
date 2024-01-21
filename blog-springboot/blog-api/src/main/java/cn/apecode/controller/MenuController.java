package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.LabelOptionDto;
import cn.apecode.dto.UserMenuDto;
import cn.apecode.service.MenuService;
import cn.apecode.vo.MenuVo;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 后台菜单 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "菜单模块")
@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @ApiOperation(value = "获取当前用户菜单", httpMethod = "GET")
    @GetMapping("/admin/user/menus")
    @Encrypt
    public ResponseCode<List<UserMenuDto>> ListUserMenusByUserId() {
        return ResponseCode.ok(menuService.listUserMenusByUserId());
    }

    @ApiOperation(value = "获取菜单列表", httpMethod = "GET")
    @GetMapping("/admin/menus")
    @Encrypt
    public ResponseCode<List<UserMenuDto>> listMenu() {
        return ResponseCode.ok(menuService.listMenu());
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新菜单", httpMethod = "POST")
    @PostMapping("/admin/menu")
    @Decrypt
    public ResponseCode<?> saveOrUpdateMenu(@Validated @RequestBody MenuVo menu) {
        menuService.saveOrUpdateMenu(menu);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE")
    @NotNull(message = "菜单id不能为空")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataTypeClass = String.class)
    @DeleteMapping("/admin/menu/{id}")
    public ResponseCode<?> deleteMenu(@PathVariable String id) {
        menuService.deleteMenu(id);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取角色菜单选项", httpMethod = "GET")
    @GetMapping("/admin/role/menus")
    @Encrypt
    public ResponseCode<List<LabelOptionDto>> listRoleMenuOption() {
        return ResponseCode.ok(menuService.listRoleMenuOption());
    }

}
