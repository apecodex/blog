package cn.apecode.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 消息通知
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
@TableName("t_notice")
@ApiModel(value = "Notice对象", description = "消息通知")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("通知id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("接收的用户id")
    private Integer userId;

    @ApiModelProperty("通知标题")
    private String title;

    @ApiModelProperty("通知内容")
    private String content;

    @ApiModelProperty("跳转的url")
    private String url;

    @ApiModelProperty("是否已阅(0否/1是)")
    private Boolean status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
