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
 * 文章评论
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_comment")
@ApiModel(value = "Comment对象", description = "文章评论")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @ApiModelProperty("内容")
    private String commentContent;

    @ApiModelProperty("父评论id")
    private Integer parentId;

    @ApiModelProperty("评论类型(1文章/2说说)")
    private Integer type;

    @ApiModelProperty("回复用户id")
    private Integer replyUserId;

    @ApiModelProperty("回复评论id")
    private Integer replyCommentId;

    @ApiModelProperty("经纬度")
    private String rectangle;

    @ApiModelProperty("评论IP")
    private String ipAddress;

    @ApiModelProperty("评论来源")
    private String ipSource;

    @ApiModelProperty("访问浏览器")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("是否审核(0否/1是)")
    private Boolean isReview;

    @ApiModelProperty("是否删除(0不删除/1删除)")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
