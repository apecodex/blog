package cn.apecode.service;

import cn.apecode.dto.OperationLogBackDto;
import cn.apecode.entity.OperationLog;
import cn.apecode.vo.DeleteVo;
import cn.apecode.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作记录 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * @description: 获取操作日志
     * @return {@link PageResult<OperationLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:14
    */
    PageResult<OperationLogBackDto> listOperationLog();

    /**
     * @description: 删除操作日志
     * @param delete
     * @auther apecode
     * @date 2022/7/10 16:33
    */
    void deleteOperationLog(DeleteVo delete);
}
