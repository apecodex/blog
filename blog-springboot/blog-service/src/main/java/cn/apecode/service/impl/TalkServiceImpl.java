package cn.apecode.service.impl;

import cn.apecode.service.RedisService;
import cn.apecode.common.utils.IpUtils;
import cn.apecode.common.utils.UserUtils;
import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.BeanCopyUtils;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.*;
import cn.apecode.entity.Comment;
import cn.apecode.entity.Talk;
import cn.apecode.entity.TalkPictureVideo;
import cn.apecode.mapper.CommentMapper;
import cn.apecode.mapper.TalkMapper;
import cn.apecode.mapper.TalkPictureVideoMapper;
import cn.apecode.service.TalkPictureVideoService;
import cn.apecode.service.TalkService;
import cn.apecode.strategy.context.UploadFileStrategyContext;
import cn.apecode.utils.PageUtils;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.TalkVo;
import cn.apecode.vo.TopVo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.RedisPrefixConst.TALK_LIKE_COUNT;
import static cn.apecode.common.constant.RedisPrefixConst.TALK_USER_LIKE;
import static cn.apecode.common.enums.FilePathEnum.PICTURE_VIDEO;

/**
 * <p>
 * 说说 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@RequiredArgsConstructor
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    private final UploadFileStrategyContext uploadFileStrategyContext;
    private final TalkMapper talkMapper;
    private final TalkPictureVideoMapper talkPictureVideoMapper;
    private final TalkPictureVideoService talkPictureVideoService;
    private final RedisService redisService;
    private final HttpServletRequest request;
    private final CommentMapper commentMapper;

    /**
     * @param talkVo
     * @description: 保存或修改说说
     * @auther apecode
     * @date 2022/7/6 18:34
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateTalk(TalkVo talkVo) {
        Talk talk = BeanCopyUtils.copyObject(talkVo, Talk.class);
        // 修改条件
        if (StringUtils.isNotBlank(talkVo.getId())) {
            Integer id = SecurityUtils.decrypt(talkVo.getId());
            if (Objects.isNull(id)) throw new BizException("说说id有误");
            // 判断说说是否存在
            Talk t = talkMapper.selectOne(Wrappers.<Talk>lambdaQuery().eq(Talk::getId, id));
            if (Objects.isNull(t)) throw new BizException("说说不存在");
            if (!t.getUserId().equals(UserUtils.getLoginUser().getUserInfoId()))
                throw new BizException("失败，无法修改其他人的说说");
            talk.setId(id);
        } else {
            // 新增
            talk.setUserId(UserUtils.getLoginUser().getUserInfoId());
            talk.setUpdateTime(CommonUtils.getLocalDateTime());
        }
        this.saveOrUpdate(talk);
        // 删除所有配图
        talkPictureVideoMapper.delete(Wrappers.<TalkPictureVideo>lambdaQuery().eq(TalkPictureVideo::getTalkId, talk.getId()));
        // 保存配图
        if (CollectionUtils.isNotEmpty(talkVo.getFiles())) {
            saveTalkPictureVideo(talk.getId(), talkVo.getFiles());
        }
    }

    /**
     * @description: 保存配图
     * @param id
     * @param files
     * @auther apecode
     * @date 11/9/2022 PM9:11
    */
    @Transactional(rollbackFor = Exception.class)
    public void saveTalkPictureVideo(Integer id, List<String> files) {
        ArrayList<TalkPictureVideo> talkPictureVideos = new ArrayList<>();
        // 删除该说说所有配图
        talkPictureVideoMapper.delete(Wrappers.<TalkPictureVideo>lambdaQuery().eq(TalkPictureVideo::getTalkId, id));
        for (int i = 0; i < files.size(); i++) {
            String fileName = files.get(i).substring(files.get(i).lastIndexOf("/") + 1);
            TalkPictureVideo pictureVideo = TalkPictureVideo.builder()
                    .talkId(id)
                    .src(files.get(i))
                    .fileName(fileName.substring(0, fileName.lastIndexOf(".")))
                    .orderNum(i + 1)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
            talkPictureVideos.add(pictureVideo);
        }
        talkPictureVideoService.saveBatch(talkPictureVideos);
    }

    /**
     * @param talkId
     * @description: 删除说说
     * @auther apecode
     * @date 2022/7/7 14:16
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTalk(String talkId) {
        Integer id = SecurityUtils.decrypt(talkId);
        if (Objects.isNull(id)) throw new BizException("说说id有误");
        // 删除图片或视频
        talkPictureVideoMapper.delete(Wrappers.<TalkPictureVideo>lambdaQuery().eq(TalkPictureVideo::getTalkId, id));
        // 删除说说
        talkMapper.deleteById(id);
    }

    /**
     * @param condition
     * @return {@link PageResult<TalkBackDto>}
     * @description: 查看后台说说
     * @auther apecode
     * @date 2022/7/7 14:39
     */
    @Override
    public PageResult<TalkBackDto> listTalkBack(ConditionVo condition) {
        Long count = talkMapper.selectCount(Wrappers.<Talk>lambdaQuery()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Talk::getContent, condition.getKeywords())
                .eq(Objects.nonNull(condition.getStatus()), Talk::getStatus, condition.getStatus())
        );
        if (count == 0) return new PageResult<>();
        List<TalkBackDto> talkBackDtoList = talkMapper.listTalkBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        List<String> talkIds = talkBackDtoList.stream().map(TalkBackDto::getId).collect(Collectors.toList());
        // 根据说说id获取评论数量
        Map<String, Integer> replyCountMap = commentMapper.listTalkCommentCountByTalkId(talkIds).stream().collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        talkBackDtoList.stream().peek(talk -> {
            talk.setCommentCount(replyCountMap.get(talk.getId()));
            Double likeCount = redisService.zScore(TALK_LIKE_COUNT, Integer.valueOf(talk.getId()));
            if (Objects.nonNull(likeCount)) {
                talk.setLikeCount(likeCount.intValue());
            }
            talk.setId(SecurityUtils.encrypt(talk.getId()));
            talk.getPictureVideos().stream().peek(pv -> {
                pv.setId(SecurityUtils.encrypt(pv.getId()));
            }).collect(Collectors.toList());
        }).collect(Collectors.toList());
        return new PageResult<>(talkBackDtoList, count.intValue());
    }

    /**
     * @param talkId
     * @description: 点赞说说
     * @auther apecode
     * @date 2022/7/7 15:58
     */
    @Override
    public void talkLike(String talkId) {
        Integer id = SecurityUtils.decrypt(talkId);
        if (Objects.isNull(id)) throw new BizException("说说id有误");
        String talkUserLike = TALK_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        // 判断是否点过赞
        if (redisService.sIsMember(talkUserLike, id)) {
            // 点过则删除说说id
            redisService.sRemove(talkUserLike, id);
            // 说说点赞数-1
            redisService.zDecr(TALK_LIKE_COUNT, id, 1D);
        } else {
            // 未点过则新增说说id
            redisService.sAdd(talkUserLike, id);
            // 说说点赞数+1
            redisService.zIncr(TALK_LIKE_COUNT, id, 1D);
        }
    }

    /**
     * @return {@link PageResult<TalkFrontDto>}
     * @description: 查看说说列表
     * @auther apecode
     * @date 2022/7/7 16:44
     */
    @Override
    public PageResult<TalkFrontDto> listTalkFront() {
        // 说说总数
        Long count = talkMapper.selectCount(Wrappers.<Talk>lambdaQuery().eq(Talk::getStatus, 1));
        List<TalkFrontDto> talkFrontDtoList = talkMapper.listTalkFront(PageUtils.getLimitCurrent(), PageUtils.getSize());
        List<String> talkIds = talkFrontDtoList.stream().map(TalkFrontDto::getId).collect(Collectors.toList());
        // 根据说说id获取评论数量
        Map<String, Integer> replyCountMap = commentMapper.listTalkCommentCountByTalkId(talkIds).stream().collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        talkFrontDtoList.stream().peek(talk -> {
            talk.setCommentCount(replyCountMap.get(talk.getId()));
            Double likeCount = redisService.zScore(TALK_LIKE_COUNT, Integer.valueOf(talk.getId()));
            if (Objects.nonNull(likeCount)) {
                talk.setLikeCount(likeCount.intValue());
            }
            talk.setId(SecurityUtils.encrypt(talk.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(talkFrontDtoList, count.intValue());
    }

    /**
     * @param talkId
     * @return {@link TalkFrontDto}
     * @description: 根据id查看说说
     * @auther apecode
     * @date 2022/7/7 17:02
     */
    @Override
    public TalkFrontDto getTalkById(String talkId) {
        Integer id = SecurityUtils.decrypt(talkId);
        if (Objects.isNull(id)) throw new BizException("说说id有误");
        TalkFrontDto talkFrontDto = talkMapper.getTalkById(id);
        if (Objects.isNull(talkFrontDto)) throw new BizException("说说不存在");
        // 获取评论数
        Long commentCount = commentMapper.selectCount(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getIsDelete, 0)
                .eq(Comment::getIsReview, 0)
                .eq(Comment::getType, 2)
                .eq(Comment::getTopicId, id)
                .isNull(Comment::getParentId));
        talkFrontDto.setCommentCount(commentCount.intValue());
        // 获取点赞数
        Double likeCount = redisService.zScore(TALK_LIKE_COUNT, Integer.valueOf(talkFrontDto.getId()));
        if (Objects.nonNull(likeCount)) {
            talkFrontDto.setLikeCount(likeCount.intValue());
        }
        talkFrontDto.setId(SecurityUtils.encrypt(talkFrontDto.getId()));
        // 设置访问用户经纬度
        String rectangleEncrypt = IpUtils.getNowUserRectangleEncrypt(request);
        if (StringUtils.isNotBlank(rectangleEncrypt)) {
            talkFrontDto.setRectangle(rectangleEncrypt);
        }
        return talkFrontDto;
    }

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 上传说说配图
     * @auther apecode
     * @date 11/9/2022 PM5:10
     */
    @Override
    public UploadFileInfoDto uploadTalkWithPicture(MultipartFile file) {
        return uploadFileStrategyContext.executeUploadFileStrategy(file, PICTURE_VIDEO.getPath());
    }

    /**
     * @description: 根据id获取后台说说
     * @param talkId
     * @return {@link TalkBackOnlyDto}
     * @auther apecode
     * @date 12/9/2022 PM2:41
    */
    @Override
    public TalkBackOnlyDto getTalkBackOnlyById(String talkId) {
        Integer id = SecurityUtils.decrypt(talkId);
        if (Objects.isNull(id)) throw new BizException("说说id有误");
        TalkBackOnlyDto talkBackOnlyDto = talkMapper.getTalkBackOnlyById(id);
        if (Objects.isNull(talkBackOnlyDto)) throw new BizException("说说不存在");
        talkBackOnlyDto.setId(SecurityUtils.encrypt(talkBackOnlyDto.getId()));
        talkBackOnlyDto.getPictureVideos().stream().peek((pv) -> pv.setId(SecurityUtils.encrypt(pv.getId()))).collect(Collectors.toList());
        return talkBackOnlyDto;
    }

    /**
     * @description: 修改说说顶置
     * @param top
     * @auther apecode
     * @date 13/9/2022 PM1:59
    */
    @Override
    public void updateTalkTop(TopVo top) {
        Integer id = SecurityUtils.decrypt(top.getId());
        if (Objects.isNull(id)) throw new BizException("说说id有误");
        Talk talk = Talk.builder()
                .id(id)
                .isTop(top.getIsTop())
                .build();
        talkMapper.updateById(talk);
    }
}
