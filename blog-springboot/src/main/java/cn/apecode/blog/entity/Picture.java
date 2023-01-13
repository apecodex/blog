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
 * 图片
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
@TableName("t_picture")
@ApiModel(value = "Picture对象", description = "图片")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户Id")
    private Integer userId;

    @ApiModelProperty("相册id")
    private Integer albumId;

    @ApiModelProperty("图片名称")
    private String pictureName;

    @ApiModelProperty("图片路径")
    private String pictureSrc;

    @ApiModelProperty("图片宽度")
    private Integer width;

    @ApiModelProperty("图片高度")
    private Integer height;

    @ApiModelProperty("图片大小")
    private String size;

    @ApiModelProperty("逻辑删除(0不删除/1删除)")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
