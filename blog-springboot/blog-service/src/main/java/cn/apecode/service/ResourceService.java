package cn.apecode.service;

import cn.apecode.dto.LabelOptionDto;
import cn.apecode.dto.ResourceDto;
import cn.apecode.entity.Resource;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.ResourceVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资源信息 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface ResourceService extends IService<Resource> {

    /**
     * @param condition
     * @return {@link List<ResourceDto>}
     * @description: 获取资源列表
     * @auther apecode
     * @date 2022/6/17 20:38
     */
    List<ResourceDto> listResource(ConditionVo condition);

    /**
     * @param resource
     * @description: 新增或修改资源
     * @auther apecode
     * @date 2022/6/17 22:54
     */
    void saveOrUpdateResource(ResourceVo resource);

    /**
     * @param resourceId
     * @description: 删除资源
     * @auther apecode
     * @date 2022/6/17 23:01
     */
    void deleteResource(String resourceId);

    /**
     * @return {@link List<LabelOptionDto>}
     * @description: 获取角色资源选项
     * @auther apecode
     * @date 2022/6/17 23:24
     */
    List<LabelOptionDto> listResourceOption();
}
