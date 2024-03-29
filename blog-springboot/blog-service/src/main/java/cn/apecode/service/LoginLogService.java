package cn.apecode.service;

import cn.apecode.dto.LoginLogBackDto;
import cn.apecode.entity.LoginLog;
import cn.apecode.vo.DeleteVo;
import cn.apecode.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录日志 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * @description: 获取登录日志
     * @return {@link PageResult<LoginLogBackDto>}
     * @auther apecode
     * @date 2022/7/10 16:46
    */
    PageResult<LoginLogBackDto> listLoginLog();

    /**
     * @description: 删除登录日志
     * @param delete
     * @auther apecode
     * @date 2022/7/10 16:55
    */
    void deleteLoginLog(DeleteVo delete);
}
