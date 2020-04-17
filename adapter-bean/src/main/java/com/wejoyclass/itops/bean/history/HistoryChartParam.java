package com.wejoyclass.itops.bean.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryChartParam {
    @ApiModelProperty("id")
    String id;

    @ApiModelProperty("监控项名")
    String name;
}
