package cn.apecode.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章信息
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_article")
@ApiModel(value = "Article对象", description = "文章信息")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("文章缩略图")
    private String articleCover;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章类型(1原创/2转载/3翻译)")
    private Integer type;

    @ApiModelProperty("原文链接")
    private String originalUrl;

    @ApiModelProperty("是否置顶(0否/1是)")
    private Boolean isTop;

    @ApiModelProperty("状态值(1公开/2私密/3草稿)")
    private Integer status;

    @ApiModelProperty("逻辑删除（1是/0否）")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
