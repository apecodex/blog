package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.LoginLogBackDto;
import cn.apecode.blog.entity.LoginLog;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.LoginLogMapper;
import cn.apecode.blog.service.LoginLogService;
import cn.apecode.blog.utils.PageUtils;
import cn.apecode.blog.utils.SecurityUtils;
import cn.apecode.blog.vo.DeleteVo;
import cn.apecode.blog.vo.PageResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    private final LoginLogMapper loginLogMapper;

    /**
     * @description: 获取登录日志
     * @return {@link PageResult<LoginLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:49
    */
    @Override
    public PageResult<LoginLogBackDto> listLoginLog() {
        Long count = loginLogMapper.selectCount(Wrappers.<LoginLog>lambdaQuery());
        if (count.intValue() == 0) return new PageResult<>();
        List<LoginLogBackDto> loginLogBackDtoList = loginLogMapper.listLoginLog(PageUtils.getLimitCurrent(), PageUtils.getSize());
        loginLogBackDtoList.stream().peek(loginLog -> {
            loginLog.setId(SecurityUtils.encrypt(loginLog.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(loginLogBackDtoList, count.intValue());
    }

    /**
     * @description: 删除登录日志
     * @param delete
     * @auther apecode
     * @date 2022/7/10 16:55
    */
    @Override
    public void deleteLoginLog(DeleteVo delete) {
        if (!delete.getIdList().isEmpty()) {
            List<Integer> ids = delete.getIdList().stream().map(loginLogId -> {
                Integer id = SecurityUtils.decrypt(loginLogId);
                if (Objects.isNull(id)) throw new BizException("日志id有误");
                return id;
            }).collect(Collectors.toList());
            loginLogMapper.deleteBatchIds(ids);
        }
    }
}
