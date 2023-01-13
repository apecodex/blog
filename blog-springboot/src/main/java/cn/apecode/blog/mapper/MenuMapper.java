package cn.apecode.blog.mapper;

import cn.apecode.blog.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台菜单 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * @description: 根据用户id获取菜单
     * @param id
     * @return {@link List<Menu>}
     * @auther apecode
     * @date 2022/6/6 21:13
    */
    List<Menu> listUserMenusByUserId(@Param("id") Integer id);
}
