package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.LabelOptionDto;
import cn.apecode.dto.ResourceDto;
import cn.apecode.service.ResourceService;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.ResourceVo;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 资源信息 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "资源模块")
@RequiredArgsConstructor
@RestController
public class ResourceController {

    private final ResourceService resourceService;

    @ApiOperation(value = "获取资源列表", httpMethod = "GET")
    @GetMapping(value = "/admin/resources")
    @Encrypt
    public ResponseCode<List<ResourceDto>> listResource(ConditionVo condition) {
        return ResponseCode.ok(resourceService.listResource(condition));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改资源", httpMethod = "POST")
    @PostMapping("/admin/resource")
    @Decrypt
    public ResponseCode<?> saveOrUpdateResource(@Validated @RequestBody ResourceVo resource) {
        resourceService.saveOrUpdateResource(resource);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除资源", httpMethod = "DELETE")
    @ApiImplicitParam(name = "resourceId", value = "资源id", required = true, dataTypeClass = String.class)
    @DeleteMapping("/admin/resource/{resourceId}")
    public ResponseCode<?> deleteResource(@PathVariable String resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取角色资源选项", httpMethod = "GET")
    @GetMapping("/admin/role/resources")
    @Encrypt
    public ResponseCode<List<LabelOptionDto>> listResourceOption() {
        return ResponseCode.ok(resourceService.listResourceOption());
    }
}
