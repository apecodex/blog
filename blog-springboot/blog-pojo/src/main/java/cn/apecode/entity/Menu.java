package cn.apecode.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台菜单
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_menu")
@Builder
@ApiModel(value = "Menu对象", description = "后台菜单")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("后台菜单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("菜单标识")
    private String name;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("菜单路径")
    private String path;

    @ApiModelProperty("组件")
    private String component;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("父id")
    private Integer parentId;

    @ApiModelProperty("排序")
    private Integer orderNum;

    @ApiModelProperty("是否启用（1是/0否）")
    private Boolean isEnable;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
