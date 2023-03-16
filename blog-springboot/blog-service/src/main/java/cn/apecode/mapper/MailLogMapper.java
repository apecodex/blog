package cn.apecode.mapper;

import cn.apecode.dto.MailLogBackDto;
import cn.apecode.entity.MailLog;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 邮件发送记录 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface MailLogMapper extends BaseMapper<MailLog> {

    /**
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List<MailLogBackDto>}
     * @description: 获取邮件日志
     * @auther apecode
     * @date 2022/7/13 19:05
     */
    List<MailLogBackDto> listMailLog(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);
}
