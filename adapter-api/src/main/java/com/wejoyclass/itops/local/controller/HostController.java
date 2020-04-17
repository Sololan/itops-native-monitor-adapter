package com.wejoyclass.itops.local.controller;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.host.HostParam;
import com.wejoyclass.itops.local.service.HostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "主机相关Controller")
@RequestMapping("adapter")
public class HostController {

    @Autowired
    HostService hostService;

    @ApiOperation("根据主机id获取主机信息")
    @GetMapping("/host/{id}")
    public ToNativeEntity getHostById(@PathVariable("id") Integer id){
        return hostService.getHostById(id);
    }

    @ApiOperation("创建主机")
    @PostMapping("/host")
    public ToNativeEntity addHost(@RequestBody HostParam hostParam){
        return hostService.addHost(hostParam);
    }

    @ApiOperation("禁用主机")
    @GetMapping("/host/{id}/forbid")
    public ToNativeEntity forbidHost(@PathVariable("id") Integer id){
        return hostService.forbidHost(id);
    }

    @ApiOperation("启用主机")
    @GetMapping("/host/{id}/unforbid")
    public ToNativeEntity unforbidHost(@PathVariable("id") Integer id){
        return hostService.unforbidHost(id);
    }

    @ApiOperation("删除主机")
    @PostMapping("/host/{id}/delete")
    public ToNativeEntity deleteHost(@PathVariable("id") Integer id){
        return hostService.deleteHost(id);
    }

    @ApiOperation("更新主机")
    @PostMapping("/host/{id}")
    public ToNativeEntity updateHost(@PathVariable("id") Integer id,@RequestBody HostParam hostParam){
        return hostService.updateHost(id,hostParam);
    }
}
