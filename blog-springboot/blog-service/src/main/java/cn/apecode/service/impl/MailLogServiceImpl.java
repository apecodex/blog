package cn.apecode.service.impl;

import cn.apecode.common.enums.EmailTypeEnum;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.MailLogBackDto;
import cn.apecode.entity.MailLog;
import cn.apecode.mapper.MailLogMapper;
import cn.apecode.service.MailLogService;
import cn.apecode.utils.PageUtils;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.DeleteVo;
import cn.apecode.vo.PageResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 邮件发送记录 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements MailLogService {

    private final MailLogMapper mailLogMapper;

    /**
     * @description: 获取邮件日志
     * @param condition
     * @return {@link PageResult<MailLogBackDto>}
     * @auther apecode
     * @date 2022/7/13 19:17
    */
    @Override
    public PageResult<MailLogBackDto> listMailLog(ConditionVo condition) {
        Long count = mailLogMapper.selectCount(Wrappers.<MailLog>lambdaQuery().eq(Objects.nonNull(condition.getType()), MailLog::getType, condition.getType()));
        if (count.intValue() == 0) return new PageResult<>();
        List<MailLogBackDto> mailLogBackDtoList = mailLogMapper.listMailLog(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        mailLogBackDtoList.stream().peek(mailLog -> {
            mailLog.setType(EmailTypeEnum.getMailTypeEnum(Integer.parseInt(mailLog.getType())).getDesc());
            mailLog.setTopicId(SecurityUtils.encrypt(mailLog.getTopicId()));
        }).collect(Collectors.toList());
        return new PageResult<>(mailLogBackDtoList, count.intValue());
    }

    @Override
    public void deleteMailLog(DeleteVo delete) {
        if (!delete.getIdList().isEmpty()) {
            mailLogMapper.deleteBatchIds(delete.getIdList());
        }
    }
}
