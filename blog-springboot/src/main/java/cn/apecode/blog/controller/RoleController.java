package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.RoleDto;
import cn.apecode.blog.dto.UserRoleDto;
import cn.apecode.blog.service.RoleService;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.ResponseCode;
import cn.apecode.blog.vo.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色", httpMethod = "POST")
    @PostMapping("/admin/role")
    public ResponseCode<?> saveOrUpdateRole(@Validated @RequestBody RoleVo roleVo) {
        roleService.saveOrUpdateRole(roleVo);
        return ResponseCode.ok("保存成功");
    }

    @ApiOperation(value = "查询角色列表", httpMethod = "GET")
    @GetMapping("/admin/roles")
    public ResponseCode<PageResult<RoleDto>> listRole(ConditionVo condition) {
        return ResponseCode.ok(roleService.listRole(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除角色", httpMethod = "DELETE")
    @DeleteMapping("/admin/role")
    public ResponseCode<?> deleteRoleByRoleId(String roleId) {
        roleService.deleteRoleByRoleId(roleId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "查询用户角色选项", httpMethod = "GET")
    @GetMapping("/admin/user/roles")
    public ResponseCode<List<UserRoleDto>> listUserRole() {
        return ResponseCode.ok(roleService.listUserRole());
    }

}
