package com.wejoyclass.itops.local.controller;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.history.HistoryParam;
import com.wejoyclass.itops.bean.history.HistoryChartParam;
import com.wejoyclass.itops.local.service.HistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "历史数据相关Controller")
@RequestMapping("adapter")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @ApiOperation("获取历史数据信息（暂时没用）")
    @PostMapping("/history")
    public ToNativeEntity getHost(@RequestBody HistoryParam historyParam){
        return historyService.getHistory(historyParam);
    }

    @ApiOperation("根据多个监控项id获取格式化的监控值历史数据")
    @PostMapping("/history/{time}")
    public ToNativeEntity getHistoryWithFormat(@PathVariable("time") Integer time,@RequestBody List<HistoryChartParam> historyChartParamList){
        return historyService.getHistoryWithFormat(time,historyChartParamList);
    }
}
