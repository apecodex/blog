package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.VisitStatisticsDto;
import cn.apecode.blog.entity.DailyVisit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 日均访问量 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface DailyVisitMapper extends BaseMapper<DailyVisit> {

    /**
     * @description: 用户几天访问量统计列表
     * @param day
     * @return {@link List<VisitStatisticsDto>}
     * @auther apecode
     * @date 2022/7/14 0:24
    */
    List<VisitStatisticsDto> listAFewDaysVisit(@Param("day") int day);
}
