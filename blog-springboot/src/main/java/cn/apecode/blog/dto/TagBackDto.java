package cn.apecode.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 后台标签
 * @author: apecode
 * @date: 2022-06-25 19:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台标签", description = "后台标签展示")
public class TagBackDto {

    @ApiModelProperty(value = "标签id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "标签名", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "文章数量", name = "articleCount", dataType = "Integer")
    private Integer articleCount;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
