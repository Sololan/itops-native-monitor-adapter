package com.wejoyclass.itops.bean.item;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemChartParam {
    @ApiModelProperty("id")
    String id;

    @ApiModelProperty("name")
    String name;
}
