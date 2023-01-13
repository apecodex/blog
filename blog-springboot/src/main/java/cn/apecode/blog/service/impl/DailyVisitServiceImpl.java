package cn.apecode.blog.service.impl;

import cn.apecode.blog.entity.DailyVisit;
import cn.apecode.blog.mapper.DailyVisitMapper;
import cn.apecode.blog.service.DailyVisitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日均访问量 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class DailyVisitServiceImpl extends ServiceImpl<DailyVisitMapper, DailyVisit> implements DailyVisitService {

}
