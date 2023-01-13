package cn.apecode.blog.service;

import cn.apecode.blog.dto.MailLogBackDto;
import cn.apecode.blog.entity.MailLog;
import cn.apecode.blog.vo.ConditionVo;
import cn.apecode.blog.vo.DeleteVo;
import cn.apecode.blog.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 邮件发送记录 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface MailLogService extends IService<MailLog> {

    /**
     * @description: 获取邮件日志
     * @param condition
     * @return {@link PageResult<MailLogBackDto>}
     * @auther apecode
     * @date 2022/7/13 19:17
    */
    PageResult<MailLogBackDto> listMailLog(ConditionVo condition);

    /**
     * @description: 删除邮件日志
     * @param delete
     * @auther apecode
     * @date 2022/7/13 19:47
    */
    void deleteMailLog(DeleteVo delete);
}
