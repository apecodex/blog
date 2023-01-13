package cn.apecode.blog.service.impl;

import cn.apecode.blog.entity.ArticleTag;
import cn.apecode.blog.mapper.ArticleTagMapper;
import cn.apecode.blog.service.ArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章所属标签 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
