package com.wejoyclass.itops.local.controller;

import com.wejoyclass.itops.local.util.ZabbixUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "模板相关Controller")
@RequestMapping("adapter")
public class TemplateController {

    @Autowired
    ZabbixUtil zabbixUtil;

    @ApiOperation("获取指定key的topn数据")
    @GetMapping("/templates")
    public List<Map<String,String>> getTemplates(){
        return zabbixUtil.getTemplates();
    }
}
