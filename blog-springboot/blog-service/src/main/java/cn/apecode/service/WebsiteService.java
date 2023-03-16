package cn.apecode.service;

import cn.apecode.dto.BlogBackInfoDto;
import cn.apecode.dto.BlogHomeInfoDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.entity.Website;
import cn.apecode.vo.WebsiteConfigVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 博客设置 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface WebsiteService extends IService<Website> {

    /**
     * @description: 获取后台信息
     * @return {@link BlogBackInfoDto}
     * @auther apecode
     * @date 2022/7/13 23:29
    */
    BlogBackInfoDto getBlogBackInfo();

    /**
     * @description: 修改关于我信息
     * @param aboutMe
     * @auther apecode
     * @date 2022/7/14 15:00
    */
    void updateAboutMe(String aboutMe);

    /**
     * @description: 获取关于我信息
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/14 15:09
    */
    String getAboutMe();

    /**
     * @description: 更新网站配置
     * @param websiteConfig
     * @auther apecode
     * @date 2022/7/14 15:27
    */
    void updateWebsiteConfigure(WebsiteConfigVo websiteConfig);

    /**
     * @description: 获取网站配置
     * @return {@link WebsiteConfigVo}
     * @auther apecode
     * @date 2022/7/14 16:58
    */
    WebsiteConfigVo getWebsiteConfigure();

    /**
     * @description: 上传博客配置图片
     * @param file
     * @return {@link UploadFileInfoDto}
     * @auther apecode
     * @date 2022/7/14 17:05
    */
    UploadFileInfoDto uploadWebSiteConfigPic(MultipartFile file);

    /**
     * @description: 获取博客信息
     * @return {@link BlogHomeInfoDto}
     * @auther apecode
     * @date 2022/7/14 17:46
    */
    BlogHomeInfoDto getBlogInfo();
}
