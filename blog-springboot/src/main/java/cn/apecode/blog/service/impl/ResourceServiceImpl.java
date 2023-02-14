package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.LabelOptionDto;
import cn.apecode.blog.dto.ResourceDto;
import cn.apecode.blog.entity.Resource;
import cn.apecode.blog.entity.RoleResource;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.handler.FilterInvocationSecurityMetadataSourceImpl;
import cn.apecode.blog.mapper.ResourceMapper;
import cn.apecode.blog.mapper.RoleResourceMapper;
import cn.apecode.blog.service.ResourceService;
import cn.apecode.blog.utils.BeanCopyUtils;
import cn.apecode.blog.utils.CommonUtils;
import cn.apecode.blog.utils.SecurityUtils;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.ResourceVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源信息 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;
    private final RoleResourceMapper roleResourceMapper;
    private final FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    /**
     * @param condition
     * @return {@link List<ResourceDto>}
     * @description: 获取资源列表
     * @auther apecode
     * @date 2022/6/17 20:39
     */
    @Override
    public List<ResourceDto> listResource(ConditionVo condition) {
        // 获取所有资源
        List<Resource> resourceList = resourceMapper.selectList(Wrappers.<Resource>lambdaQuery().like(StringUtils.isNoneBlank(condition.getKeywords()), Resource::getName, condition.getKeywords()));
        // 获取所有模块
        List<Resource> parentResource = listResourceModule(resourceList);
        // 获取所有子资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子资源
        List<ResourceDto> resourceDtoList = parentResource.stream().map(parent -> {
            ResourceDto resourceDto = BeanCopyUtils.copyObject(parent, ResourceDto.class);
            resourceDto.setId(SecurityUtils.encrypt(String.valueOf(parent.getId())));
            if (Objects.nonNull(childrenMap.get(parent.getId()))) {
                List<ResourceDto> childrenList = childrenMap.get(parent.getId()).stream().map(resource -> {
                    ResourceDto res = BeanCopyUtils.copyObject(resource, ResourceDto.class);
                    res.setId(SecurityUtils.encrypt(String.valueOf(resource.getId())));
                    res.setParentId(SecurityUtils.encrypt(String.valueOf(resource.getParentId())));
                    return res;
                }).collect(Collectors.toList());
                resourceDto.setChildren(childrenList);
                childrenMap.remove(parent.getId());
            }
            return resourceDto;
        }).collect(Collectors.toList());
        // 如果还有剩余未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDto> childrenDtoList = childrenList.stream().map(resource -> {
                ResourceDto resourceDto = BeanCopyUtils.copyObject(resource, ResourceDto.class);
                resourceDto.setId(SecurityUtils.encrypt(String.valueOf(resource.getId())));
                resourceDto.setParentId(SecurityUtils.encrypt(String.valueOf(resource.getParentId())));
                return resourceDto;
            }).collect(Collectors.toList());
            resourceDtoList.addAll(childrenDtoList);
        }
        return resourceDtoList;
    }

    /**
     * @param resource
     * @description: 新增或修改资源
     * @auther apecode
     * @date 2022/6/17 22:54
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateResource(ResourceVo resource) {
        Resource re = BeanCopyUtils.copyObject(resource, Resource.class);
        // 如果有资源id，检查资源是否存在
        if (StringUtils.isNotBlank(resource.getId())) {
            Integer id = SecurityUtils.decrypt(resource.getId());
            if (Objects.isNull(id)) throw new BizException("资源id有误");
            boolean exists = resourceMapper.exists(Wrappers.<Resource>lambdaQuery().eq(Resource::getId, id));
            if (!exists) throw new BizException("资源id不存在");
            re.setId(id);
        }
        if (StringUtils.isNoneBlank(resource.getParentId())) {
            Integer parentId = SecurityUtils.decrypt(resource.getParentId());
            if (Objects.isNull(parentId)) throw new BizException("父资源id有误");
            boolean exists = resourceMapper.exists(Wrappers.<Resource>lambdaQuery().eq(Resource::getId, parentId));
            if (!exists) throw new BizException("父资源id不存在");
            re.setParentId(parentId);
        } else re.setParentId(null);
        re.setUpdateTime(CommonUtils.getLocalDateTime());
        re.setUrl(StringUtils.isBlank(resource.getUrl()) ? null : resource.getUrl());
        re.setRequestMethod(StringUtils.isBlank(resource.getRequestMethod()) ? null : resource.getRequestMethod());
        this.saveOrUpdate(re);
        // 重新加载角色资源信息
        filterInvocationSecurityMetadataSource.clearDataSource();
    }

    /**
     * @param resourceId
     * @description: 删除资源
     * @auther apecode
     * @date 2022/6/17 23:01
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteResource(String resourceId) {
        Integer id = SecurityUtils.decrypt(resourceId);
        if (Objects.isNull(id)) throw new BizException("资源id有误");
        boolean exists = roleResourceMapper.exists(Wrappers.<RoleResource>lambdaQuery().eq(RoleResource::getResourceId, id));
        if (exists) {
            throw new BizException("删除失败，该资源有角色绑定");
        }
        // 通过id删除子资源
        List<Integer> resourceIdList = resourceMapper.selectList(Wrappers.<Resource>lambdaQuery().select(Resource::getId).eq(Resource::getParentId, id))
                .stream().map(Resource::getId).collect(Collectors.toList());
        resourceIdList.add(id);
        resourceMapper.deleteBatchIds(resourceIdList);
    }

    /**
     * @return {@link List<LabelOptionDto>}
     * @description: 获取角色资源选项
     * @auther apecode
     * @date 2022/6/17 23:24
     */
    @Override
    public List<LabelOptionDto> listResourceOption() {
        // 获取所有资源
        List<Resource> resourceList = resourceMapper.selectList(Wrappers.<Resource>lambdaQuery().select(Resource::getId, Resource::getName, Resource::getParentId));
        // 获取所有模块
        List<Resource> listParentModule = listResourceModule(resourceList);
        // 获取子资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        return listParentModule.stream().map(resource -> {
            List<LabelOptionDto> labelOptionDtoList = new ArrayList<>();
            // 获取子资源
            List<Resource> children = childrenMap.get(resource.getId());
            // 添加子资源
            if (CollectionUtils.isNotEmpty(children)) {
                labelOptionDtoList = children.stream().map(r -> LabelOptionDto.builder()
                        .id(SecurityUtils.encrypt(String.valueOf(r.getId())))
                        .label(r.getName())
                        .build()).collect(Collectors.toList());
            }
            return LabelOptionDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(resource.getId())))
                    .label(resource.getName())
                    .children(labelOptionDtoList)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * @param resourceList
     * @return {@link Map<Integer,List<Resource>>}
     * @description: 获取所有子资源
     * @auther apecode
     * @date 2022/6/17 21:10
     */
    private Map<Integer, List<Resource>> listResourceChildren(List<Resource> resourceList) {
        return resourceList.stream().filter(resource -> Objects.nonNull(resource.getParentId())).collect(Collectors.groupingBy(Resource::getParentId));
    }

    /**
     * @param resourceList
     * @return {@link List<Resource>}
     * @description: 获取所有模块
     * @auther apecode
     * @date 2022/6/17 21:06
     */
    private List<Resource> listResourceModule(List<Resource> resourceList) {
        return resourceList.stream().filter(resource -> Objects.isNull(resource.getParentId())).collect(Collectors.toList());
    }
}
