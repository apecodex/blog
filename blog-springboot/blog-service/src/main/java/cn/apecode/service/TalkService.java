package cn.apecode.service;

import cn.apecode.dto.TalkBackDto;
import cn.apecode.dto.TalkBackOnlyDto;
import cn.apecode.dto.TalkFrontDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.entity.Talk;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.TalkVo;
import cn.apecode.vo.TopVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 说说 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
public interface TalkService extends IService<Talk> {

    /**
     * @param talk
     * @description: 保存或修改说说
     * @auther apecode
     * @date 2022/7/6 18:34
     */
    void saveOrUpdateTalk(TalkVo talk);

    /**
     * @param talkId
     * @description: 删除说说
     * @auther apecode
     * @date 2022/7/7 14:16
     */
    void deleteTalk(String talkId);

    /**
     * @param condition
     * @return {@link PageResult<TalkBackDto>}
     * @description: 查看后台说说
     * @auther apecode
     * @date 2022/7/7 14:39
     */
    PageResult<TalkBackDto> listTalkBack(ConditionVo condition);

    /**
     * @param talkId
     * @description: 点赞说说
     * @auther apecode
     * @date 2022/7/7 15:58
     */
    void talkLike(String talkId);

    /**
     * @description: 查看说说列表
     * @return {@link PageResult<TalkFrontDto>}
     * @auther apecode
     * @date 2022/7/7 16:44
    */
    PageResult<TalkFrontDto> listTalkFront();

    /**
     * @description: 根据id查看说说
     * @param talkId
     * @return {@link TalkFrontDto}
     * @auther apecode
     * @date 2022/7/7 17:02
    */
    TalkFrontDto getTalkById(String talkId);

    /**
     * @description: 上传说说配图
     * @param file
     * @return {@link UploadFileInfoDto}
     * @auther apecode
     * @date 11/9/2022 PM5:09
    */
    UploadFileInfoDto uploadTalkWithPicture(MultipartFile file);

    /**
     * @description: 根据id获取后台说说
     * @param talkId
     * @return {@link TalkBackOnlyDto}
     * @auther apecode
     * @date 12/9/2022 PM2:41
    */
    TalkBackOnlyDto getTalkBackOnlyById(String talkId);

    /**
     * @description: 修改说说顶置
     * @param top
     * @auther apecode
     * @date 13/9/2022 PM1:59
    */
    void updateTalkTop(TopVo top);
}
