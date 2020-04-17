package com.wejoyclass.itops.bean.item;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemTopNParam {
    @ApiModelProperty("limit")
    Integer n;

    @ApiModelProperty("监控项key")
    String key;
}
