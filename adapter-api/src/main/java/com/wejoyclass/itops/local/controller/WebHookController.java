package com.wejoyclass.itops.local.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.core.util.CtrlUtil;
import com.wejoyclass.core.util.RespEntity;
import com.wejoyclass.itops.local.service.WebHookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "webhook接收数据Controller")
@RequestMapping("webhook")
public class WebHookController {

    @Autowired
    WebHookService webHookService;

    @ApiOperation("通过webhook接收报警数据")
    @PostMapping("/warning")
    public RespEntity webhook(@RequestBody JSONObject warningJSON){
        return CtrlUtil.exe(result -> webHookService.publishWarning(warningJSON));
    }
}
