package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@ApiModel(value = "带有其他数据的页面结果", description = "带有其他数据的页面结果")
public class PageResultWithObject<T, E> extends PageResult<T> {

    public PageResultWithObject(List<T> recordList, Integer count, E data) {
        super(recordList, count);
        this.data = data;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @ApiModelProperty(name = "name", value = "数据", dataType = "E")
    private E data;
}
