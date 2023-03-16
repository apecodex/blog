package cn.apecode.mapper;

import cn.apecode.dto.OperationLogBackDto;
import cn.apecode.entity.OperationLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 操作记录 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * @description: 获取操作日志
     * @param limitCurrent
     * @param size
     * @return {@link List<OperationLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:23
    */
    List<OperationLogBackDto> listOperationLog(@Param("current") Long limitCurrent, @Param("size") Long size);
}
