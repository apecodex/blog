package cn.apecode.service.impl;

import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.OperationLogBackDto;
import cn.apecode.entity.OperationLog;
import cn.apecode.mapper.OperationLogMapper;
import cn.apecode.service.OperationLogService;
import cn.apecode.utils.PageUtils;
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
 * 操作记录 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    /**
     * @description: 获取操作日志
     * @return {@link PageResult<OperationLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:14
    */
    @Override
    public PageResult<OperationLogBackDto> listOperationLog() {
        Long count = operationLogMapper.selectCount(Wrappers.<OperationLog>lambdaQuery());
        if (count.intValue() == 0) {
            return new PageResult<>();
        }
        List<OperationLogBackDto> operationLogBackDtoList = operationLogMapper.listOperationLog(PageUtils.getLimitCurrent(), PageUtils.getSize());
        operationLogBackDtoList.stream().peek(operationLog -> {
            operationLog.setId(SecurityUtils.encrypt(operationLog.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(operationLogBackDtoList, count.intValue());
    }

    /**
     * @description: 删除操作日志
     * @param delete
     * @auther apecode
     * @date 2022/7/10 16:33
    */
    @Override
    public void deleteOperationLog(DeleteVo delete) {
        if (!delete.getIdList().isEmpty()) {
            List<Integer> ids = delete.getIdList().stream().map(operationLogId -> {
                Integer id = SecurityUtils.decrypt(operationLogId);
                if (Objects.isNull(id)) throw new BizException("日志id有误");
                return id;
            }).collect(Collectors.toList());
            operationLogMapper.deleteBatchIds(ids);
        }
    }
}
