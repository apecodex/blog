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
 * 说说的图片和视频
 * </p>
 *
 * @author apecode
 * @since 2022-07-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_talk_picture_video")
@ApiModel(value = "TalkPictureVideo对象", description = "说说的图片和视频")
public class TalkPictureVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("说说id")
    private Integer talkId;

    @ApiModelProperty("图片或视频地址")
    private String src;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("图片顺序")
    private Integer orderNum;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
