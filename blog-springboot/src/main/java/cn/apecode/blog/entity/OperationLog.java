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
 * 操作记录
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
@TableName("t_operation_log")
@ApiModel(value = "OperationLog对象", description = "操作记录")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作模块")
    private String optModule;

    @ApiModelProperty("操作类型")
    private String optType;

    @ApiModelProperty("操作url")
    private String optUrl;

    @ApiModelProperty("操作方法")
    private String optMethod;

    @ApiModelProperty("操作描述")
    private String optDesc;

    @ApiModelProperty("请求参数")
    private String requestParam;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("返回数据")
    private String responseData;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("操作ip")
    private String idAddress;

    @ApiModelProperty("操作地址")
    private String ipSource;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
