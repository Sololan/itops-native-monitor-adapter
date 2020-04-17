package com.wejoyclass.itops.local.controller;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.item.ItemChartParam;
import com.wejoyclass.itops.bean.item.ItemParam;
import com.wejoyclass.itops.bean.item.ItemTopNParam;
import com.wejoyclass.itops.local.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "监控项相关Controller")
@RequestMapping("adapter")
public class ItemContorller {
    @Autowired
    ItemService itemService;

    @ApiOperation("获取指定key的topn数据")
    @PostMapping("/item/top")
    public ToNativeEntity getItemTopN(@RequestBody ItemTopNParam itemTopNParam){
        return itemService.getItemTopN(itemTopNParam);
    }

    @ApiOperation("根据key和hostid获取监控项数据")
    @PostMapping("/item/key/hostid")
    public ToNativeEntity getItemByKeyAndHostId(@RequestBody ItemParam itemParam){
        return itemService.getItemByKeyAndHostId(itemParam);
    }

    @ApiOperation("根据主机id获取监控项")
    @GetMapping("/host/{id}/items")
    public ToNativeEntity getItemsByHostId(@PathVariable("id") Long id){
        return itemService.getItemsByHostId(id);
    }

    @ApiOperation("根据监控项id获取监控值")
    @GetMapping("/item/{id}")
    public ToNativeEntity getItemsById(@PathVariable("id") Long id){
        return itemService.getItemsById(id);
    }

    @ApiOperation("根据监控项idList获取监控最新值")
    @PostMapping("/items")
    public ToNativeEntity getItems(@RequestBody List<ItemChartParam> itemChartParamList){
        return itemService.getItems(itemChartParamList);
    }

}
