package com.wejoyclass.itops.local.service;

import com.alibaba.fastjson.JSONObject;

public interface WebHookService {

    public void publishWarning(JSONObject warningObject);
}
