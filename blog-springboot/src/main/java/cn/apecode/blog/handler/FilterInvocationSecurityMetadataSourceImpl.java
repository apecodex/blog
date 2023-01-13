package cn.apecode.blog.handler;

import cn.apecode.blog.dto.RoleResourceDto;
import cn.apecode.blog.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * @description: 接口拦截规则
 * @author: apecode
 * @date: 2022-05-28 19:30
 **/
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static List<RoleResourceDto> roleResourceDtoList;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * @description: 加载资源角色信息
     * @auther apecode
     * @date 2022/5/28 19:39
    */
    @PostConstruct
    private void loadDataSource() {
        roleResourceDtoList = roleMapper.listRoleResource();
    }

    /**
     * @description: 清空接口角色信息
     * @auther apecode
     * @date 2022/5/28 19:52
    */
    public void clearDataSource() { roleResourceDtoList = null; }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 修改接口角色关系后重新加载
        if (CollectionUtils.isEmpty(roleResourceDtoList)) this.loadDataSource();
        FilterInvocation invocation = (FilterInvocation) object;
        String method = invocation.getRequest().getMethod();
        String uri = invocation.getRequest().getRequestURI();
        // 匹配路径
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (RoleResourceDto roleResourceDto : roleResourceDtoList) {
            if (antPathMatcher.match(roleResourceDto.getUrl(), uri) && roleResourceDto.getRequestMethod().equals(method)) {
                List<String> roleList = roleResourceDto.getRoleList();
                // 禁止
                if (CollectionUtils.isEmpty(roleList)) SecurityConfig.createList("disable");
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        // 匿名接口放行
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
