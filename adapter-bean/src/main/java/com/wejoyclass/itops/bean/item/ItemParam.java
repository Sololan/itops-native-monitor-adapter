package com.wejoyclass.itops.bean.item;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemParam {
    @ApiModelProperty("key")
    String key;

    @ApiModelProperty("hostid")
    String hostId;
}
