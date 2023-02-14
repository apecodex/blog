package cn.apecode.blog.schedule;

import cn.apecode.blog.entity.DailyVisit;
import cn.apecode.blog.mapper.DailyVisitMapper;
import cn.apecode.blog.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static cn.apecode.blog.constant.RedisPrefixConst.DAY_VISITOR;
import static cn.apecode.blog.enums.ZoneEnum.SHANGHAI;

/**
 * @description: 日访问量定时存储
 * @author: apecode
 * @date: 2022-07-18 00:20
 **/
@Component
@EnableScheduling
@RequiredArgsConstructor
public class Schedule {

    private final RedisService redisService;
    private final DailyVisitMapper dailyVisitMapper;

    /**
     * @description: 每晚零点保存当日访问数
     * @auther apecode
     * @date 2022/7/18 0:31
    */
    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveDayVisit() {
        Long dayVisitCount = redisService.sSize(DAY_VISITOR);
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())).plusDays(-1);
        DailyVisit dailyVisit = DailyVisit.builder()
                .viewsCount(dayVisitCount.intValue())
                .createTime(dateTime)
                .updateTime(dateTime)
                .build();
        dailyVisitMapper.insert(dailyVisit);
        // 清空昨日redis访客记录
        redisService.del(DAY_VISITOR);
    }
}
