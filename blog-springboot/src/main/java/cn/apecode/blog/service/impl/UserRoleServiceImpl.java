package cn.apecode.blog.service.impl;

import cn.apecode.blog.entity.UserRole;
import cn.apecode.blog.mapper.UserRoleMapper;
import cn.apecode.blog.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户权限 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
