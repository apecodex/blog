package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.LoginLogBackDto;
import cn.apecode.blog.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 登录日志 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    /**
     * @description: 获取登录日志
     * @param limitCurrent
     * @param size
     * @return {@link List<LoginLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:48
    */
    List<LoginLogBackDto> listLoginLog(@Param("current") Long limitCurrent, @Param("size") Long size);
}
