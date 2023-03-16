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
 * 资源信息
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_resource")
@Builder
@ApiModel(value = "Resource对象", description = "资源信息")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("资源信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("资源名称")
    private String name;

    @ApiModelProperty("资源路径")
    @TableField(value = "url", updateStrategy = FieldStrategy.IGNORED)
    private String url;

    @ApiModelProperty("请求方式")
    @TableField(value = "requestMethod", updateStrategy = FieldStrategy.IGNORED)
    private String requestMethod;

    @ApiModelProperty("父资源id")
    private Integer parentId;

    @ApiModelProperty("是否启用（1是/0否）")
    private Boolean isEnable;

    @ApiModelProperty("是否匿名访问（1是/0否）")
    private Boolean isAnonymous;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
