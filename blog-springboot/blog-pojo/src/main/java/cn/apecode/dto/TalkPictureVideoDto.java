package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台说说配图", description = "后台说说配图")
public class TalkPictureVideoDto {

    @ApiModelProperty(value = "配图id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "配图地址", name = "src", dataType = "String")
    private String src;

    @ApiModelProperty(value = "配图名称", name = "fileName", dataType = "String")
    private String fileName;

}
