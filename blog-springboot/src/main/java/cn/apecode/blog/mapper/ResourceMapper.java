package cn.apecode.blog.mapper;

import cn.apecode.blog.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 资源信息 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

}
