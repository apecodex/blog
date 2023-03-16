package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 查询条件
 * @author: apecode
 * @date: 2022-06-09 11:50
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询条件", description = "查询条件")
public class ConditionVo {

    @ApiModelProperty(name = "current", value = "页码", dataType = "Long")
    private Long current;

    @ApiModelProperty(name = "size", value = "条数", dataType = "Long")
    private Long size;

    @ApiModelProperty(name = "keywords", value = "搜索内容", dataType = "String")
    private String keywords;

    @ApiModelProperty(name = "email", value = "邮件", dataType = "String")
    private String email;

    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "String")
    private String categoryId;

    @ApiModelProperty(name = "tagId", value = "标签id", dataType = "String")
    private String tagId;

    @ApiModelProperty(name = "albumPath", value = "相册路径", dataType = "String")
    private String albumPath;

    @ApiModelProperty(name = "articleId", value = "文章id", dataType = "String")
    private String articleId;

    @ApiModelProperty(name = "loginType", value = "登录类型", dataType = "Integer")
    private Integer loginType;

    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "status", value = "状态", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "LocalDateTime")
    private String startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private String endTime;

    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Boolean")
    private Boolean isDelete;

    @ApiModelProperty(name = "isReview", value = "是否审核", dataType = "Boolean")
    private Boolean isReview;

}
