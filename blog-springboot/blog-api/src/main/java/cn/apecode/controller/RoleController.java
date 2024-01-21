package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.RoleDto;
import cn.apecode.dto.UserRoleDto;
import cn.apecode.service.RoleService;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.vo.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "角色模块")
@RequiredArgsConstructor
@RestController
public class RoleController {

    private final RoleService roleService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色", httpMethod = "POST")
    @PostMapping("/admin/role")
    @Decrypt
    public ResponseCode<?> saveOrUpdateRole(@Validated @RequestBody RoleVo roleVo) {
        roleService.saveOrUpdateRole(roleVo);
        return ResponseCode.ok("保存成功");
    }

    @ApiOperation(value = "查询角色列表", httpMethod = "GET")
    @GetMapping("/admin/roles")
    @Encrypt
    public ResponseCode<PageResult<RoleDto>> listRole(ConditionVo condition) {
        return ResponseCode.ok(roleService.listRole(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色", httpMethod = "DELETE")
    @DeleteMapping("/admin/role")
    public ResponseCode<?> deleteRoleByRoleId(@RequestBody String roleId) {
        roleService.deleteRoleByRoleId(roleId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "查询用户角色选项", httpMethod = "GET")
    @GetMapping("/admin/user/roles")
    @Encrypt
    public ResponseCode<List<UserRoleDto>> listUserRole() {
        return ResponseCode.ok(roleService.listUserRole());
    }

}
